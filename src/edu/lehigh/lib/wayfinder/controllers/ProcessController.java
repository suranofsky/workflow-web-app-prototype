package edu.lehigh.lib.wayfinder.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import edu.lehigh.lib.wayfinder.data.WipMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class ProcessController extends HttpServlet {
	
	
	final static Logger logger = LogManager.getLogger(ProcessController.class);
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		//GET SUMMARY FOR DISPLAY
        //HARDCODING PROCESS ID - 
        //TODO - WILL THERE BE A PAGE BEFORE THIS WHERE THEY SELECT THE PROCESS
        int processId = 1;
        int accountId = 1; //TODO
		//SET UP SESSION W/DATABASE
        
        //GET SUMMARY FOR THIS PROCESS.  CURRENTLY HARDCODED WITH ACCOUNT #1 AND PROCESS #1
        List<HashMap> summary = getSummary(accountId);
        
		
		 RequestDispatcher rd = null;
		 rd = request.getRequestDispatcher("WEB-INF/process.jsp");
		 request.setAttribute("summary", summary);
		 request.setAttribute("accountid", "1");
		 request.setAttribute("processid","1");
		 rd.forward(request, response);
	}
	
	
	private List getSummary(int accountId) {
		
		SqlSession mysqlsession = null;
        List<HashMap> summary = null;
        try {
			 String mysqlresource = WayFinderConstants.mybatisfile;
			 InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			 SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			 mysqlsession = mysqlsqlSessionFactory.openSession();
			 WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
			 summary = wipMapper.getTaskSummary(); //TODO PARMS ARE CURRENTLY HARDCODED
        }
        catch(Exception e) {
       	 //TODO SEND THEM TO AN ERROR PAGE
         mysqlsession.close();
       	 System.out.println(e.toString());
        }
        

        if (mysqlsession != null) mysqlsession.close();
        return summary;
    
		
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		doGet(request,response); 
	}

}
