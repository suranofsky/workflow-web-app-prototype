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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.json.simple.JSONObject;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.WipMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AccountController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		 RequestDispatcher rd = null;
		 rd = request.getRequestDispatcher("WEB-INF/login.jsp");
		 request.setAttribute("messagestyle","visibility: hidden !important;");
		 rd.forward(request, response);	
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		 try {
			 String username = request.getParameter("email");  //change form field name to username?
			 String password = request.getParameter("password");
			 //LDAP LOGIN
			 UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			 Subject currentUser = SecurityUtils.getSubject();
			 currentUser.login(token);
			 System.out.println(currentUser.isAuthenticated());
			 System.out.println(currentUser.getPrincipal().toString());
			 
			 
			 if (currentUser.isAuthenticated()) {
				 //IN PROGRESS -- SET UP TOKEN (experimental)
				 //String mysecretkey = (String) getServletContext().getAttribute(Constants.KEY);
				 String mysecretkey = WayFinderConstants.secretKey;
				 SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		    	 long nowMillis = System.currentTimeMillis();
		    	 Date now = new Date(nowMillis);
		    	 byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(mysecretkey);

		         Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		         JwtBuilder builder = Jwts.builder()//.setId(id)  //NOT SURE WHAT THE ID IS FOR?
                         .setIssuedAt(now)
                         .setSubject(username)
                         .setIssuer("wayfinder")
                         .signWith(signatureAlgorithm, signingKey);

		         long ttlMillis = 1800000; 
		         
		         if (ttlMillis >= 0) {
		             long expMillis = nowMillis + ttlMillis;
		             Date exp = new Date(expMillis);
		             builder.setExpiration(exp);
		         }

		     	   

		         String securityToken = builder.compact();
		         Cookie myCookie = new Cookie(WayFinderConstants.tokenName, securityToken);
		         myCookie.setPath("/");
		         myCookie.setHttpOnly(true);
		         //myCookie.setSecure(true);  //only over https
		         response.addCookie(myCookie);

		         

			 } else {
				 RequestDispatcher rd = null;
				 rd = request.getRequestDispatcher("WEB-INF/login.jsp");
				 request.setAttribute("errorMessage",WayFinderConstants.messageUnableToLogin);
				 rd.forward(request, response);	 
				 return;
			 }
		 }
		 catch(AuthenticationException e) {

				 RequestDispatcher rd = null;
				 rd = request.getRequestDispatcher("WEB-INF/login.jsp");
				 request.setAttribute("errorMessage",WayFinderConstants.messageUnableToLogin);
				 rd.forward(request, response);	 
				 return;

		 }
		 
         
		 RequestDispatcher rd = null;
		 rd = request.getRequestDispatcher("/process");
		 rd.forward(request, response);
		 
	}
	
	


}
