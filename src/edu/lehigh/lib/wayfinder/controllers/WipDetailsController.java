package edu.lehigh.lib.wayfinder.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.LogMapper;
import edu.lehigh.lib.wayfinder.data.ProcessMapper;
import edu.lehigh.lib.wayfinder.data.TaskMapper;
import edu.lehigh.lib.wayfinder.data.WipMapper;
import edu.lehigh.lib.wayfinder.models.Log;
import edu.lehigh.lib.wayfinder.models.Task;
import edu.lehigh.lib.wayfinder.models.Wip;
import edu.lehigh.lib.wayfinder.models.WipProcessData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class WipDetailsController extends HttpServlet {
	
	final static Logger logger = LogManager.getLogger(WipDetailsController.class);
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		 String accountId = "1";//TODO EVENTUALLY GET THIS FROM JSON TOKEN
		 String wipId = request.getParameter("wipid");
		 

		 SqlSession mysqlsession = null; 
		 //SET UP SESSION W/DATABASE
		 String mysqlresource = WayFinderConstants.mybatisfile;
		 InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
		 SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
		 mysqlsession = mysqlsqlSessionFactory.openSession();
		 WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
		 ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
		 TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
		 LogMapper logMapper = mysqlsession.getMapper(LogMapper.class);
			
			
		 Wip workInProgress = wipMapper.getWip(wipId,accountId);
		 
		 List<Task> tasks = taskMapper.getTaskListForProcess(new Integer(workInProgress.getProcessId()).toString(), accountId);
		 
		 //TODO FIX THIS
		 edu.lehigh.lib.wayfinder.models.Process process = processMapper.getProcessById(new Integer(workInProgress.getProcessId()).toString());
		 Task task = taskMapper.getTaskById(new Integer(workInProgress.getCurrentTaskId()).toString());

		//WANTED THE WIP KEY/VALUES AS A HASHMAP FOR UX.  TODO IS THERE A BETTER WAY?
		 Map<String, String> hashmap = workInProgress.getData().stream().collect(
			  Collectors.toMap(WipProcessData::getKey, WipProcessData::getValue));
		 workInProgress.setKeyValues(hashmap);
		 
		 //GRAB LOGS FOR THIS TASK
		 List<Log> logs = logMapper.getLogsForDisplayByWipId(workInProgress.getId());

		 Map<String, String> map = new HashMap<String, String>();

		 if (mysqlsession != null) mysqlsession.close();
		 
		 RequestDispatcher rd = null;
		 rd = request.getRequestDispatcher("WEB-INF/wipdetails.jsp");
		 request.setAttribute("wip", workInProgress);
		 request.setAttribute("task", task);
		 request.setAttribute("process", process);
		 request.setAttribute("tasklist", tasks);
		 request.setAttribute("logs",logs);
		 request.setAttribute("ebookinfo", map);
		 rd.forward(request, response);	
	}
	

}
