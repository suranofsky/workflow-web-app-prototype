package edu.lehigh.lib.wayfinder.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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


import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.TaskMapper;
import edu.lehigh.lib.wayfinder.data.WipMapper;
import edu.lehigh.lib.wayfinder.models.Task;
import edu.lehigh.lib.wayfinder.models.Wip;
import edu.lehigh.lib.wayfinder.models.WipProcessData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TaskController extends HttpServlet {
	
	

	
	final static Logger logger = LogManager.getLogger(TaskController.class);
	
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		
		 //EVENTUALLY - GET THE ACCOUNT ID FROM THE JSON TOKEN
		 String accountId = request.getParameter("accountId");
		 String processId = request.getParameter("processId");
		 String currentTaskId = request.getParameter("taskId");
		 
		 SqlSession mysqlsession = null; 
		 //SET UP SESSION W/DATABASE
		 String mysqlresource = WayFinderConstants.mybatisfile;
		 InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
		 SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
		 mysqlsession = mysqlsqlSessionFactory.openSession();
		 WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
		 TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
		 Task task = taskMapper.getTaskById(currentTaskId);
		 List<Task> tasks = taskMapper.getTaskListForProcess(processId, accountId);
			
		 //GET ALL OF THE 'WIPS' FOR THIS ACCOUNT/PROCESS/AND CURRENT TASK	
		 List<Wip> workInProgress = wipMapper.getWorkInProgress(accountId, processId, currentTaskId);
		 
		 //WANTED THE WIP KEY/VALUES AS A HASHMAP FOR UX.  IS THERE A BETTER WAY?
		 Iterator<Wip> i = workInProgress.iterator();
		 while (i.hasNext()) {
			 Wip wip = (Wip) i.next();
			 Map<String, String> hashmap = wip.getData().stream().collect(
				  Collectors.toMap(WipProcessData::getKey, WipProcessData::getValue));
			 wip.setKeyValues(hashmap);
		 }
	        
		 if (mysqlsession != null) mysqlsession.close();
		 
		 RequestDispatcher rd = null;
		 rd = request.getRequestDispatcher("WEB-INF/wip.jsp");
		 request.setAttribute("wiplist", workInProgress);
		 request.setAttribute("task", task);
		 request.setAttribute("tasklist", tasks);
		 rd.forward(request, response);	
	}
	


}
