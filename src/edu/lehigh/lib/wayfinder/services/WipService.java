package edu.lehigh.lib.wayfinder.services;

import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
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
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ByteSource.Util;
import org.apache.shiro.util.SimpleByteSource;
import org.apache.shiro.web.util.WebUtils;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.LogMapper;
import edu.lehigh.lib.wayfinder.data.ProcessDataMapper;
import edu.lehigh.lib.wayfinder.data.ProcessMapper;
import edu.lehigh.lib.wayfinder.data.TaskMapper;
import edu.lehigh.lib.wayfinder.data.UserMapper;
import edu.lehigh.lib.wayfinder.data.WipMapper;
import edu.lehigh.lib.wayfinder.data.WipProcessDataMapper;
import edu.lehigh.lib.wayfinder.models.Task;
import edu.lehigh.lib.wayfinder.models.User;
import edu.lehigh.lib.wayfinder.models.Wip;
import edu.lehigh.lib.wayfinder.models.WipProcessData;
import edu.lehigh.lib.wayfinder.models.Log;
import edu.lehigh.lib.wayfinder.models.Process;
import edu.lehigh.lib.wayfinder.models.ProcessData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;





@Path ("/wip")
public class WipService {
	
	
	@Context
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	
	final static Logger logger = LogManager.getLogger(WipService.class);
	
	@POST
	@Produces("application/json")
	@Path("/{accountId}/updateWip/{wipid}")
	public Response updateWip(@PathParam("accountId") String accountId,@PathParam("wipid") String wipId,MultivaluedMap<String,String> formParams) {		
		

		String status = formParams.getFirst("status");
		String title = formParams.getFirst("title");
		logger.info("new status: " + status);
		logger.info("title: " + title);
		
		///
		Claims claims = null;
	    try {
	    	claims = verifyToken();
	    	logger.info("subject" + claims.getSubject());
	    }
	    catch(ExpiredJwtException e) {
	    	logger.fatal(e.getMessage());
			return Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageSessionExpired).build();
	    }
	    catch(Exception e) {
	    	logger.fatal(e.getMessage());
			return Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageUnauthorized).build();
	    }
	    //
		
		List<Log> logs = null;
		String json = "";
		try {
			SqlSession mysqlsession = null;
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
			//GET WIP
			Wip wip = wipMapper.getWip(wipId, accountId);
			logger.info("old status was " + wip.getCurrentTaskId());
			logger.info("new status is " + status);
			//IF STATUS CHANGED, LOG UPDATE
			String currentStatus = new Integer(wip.getCurrentTaskId()).toString();
			LogMapper logMapper = mysqlsession.getMapper(LogMapper.class);
			if (!currentStatus.equalsIgnoreCase(status)) {
				Log log = new Log();
				log.setUpdatedDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
				log.setUserid(claims.getSubject());
				log.setNewStatus(status);
				log.setWipId(wip.getId());
				
				logMapper.insertLogRow(log);
			}
			wipMapper.updateWipStatus(status, wipId);
			mysqlsession.commit();
			logs = logMapper.getLogsForDisplayByWipId(wip.getId());
			
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    json = objectMapper.writeValueAsString(logs);
		
			mysqlsession.close();
		}
		catch(Exception e) {
			logger.fatal(e.toString());
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}

	    
		return Response.status(Response.Status.OK).entity(json).build();
		
	}
	   

	@POST
	@Produces("application/json")
	@Path("/{accountId}/createWip")
	public Response insertNewWip(@PathParam("accountId") String accountId,MultivaluedMap<String,String> formParams) {		
		SqlSession mysqlsession = null;
		try {
			

			Claims claims = null;
		    try {
		    	claims = verifyToken();
		    	logger.info("subject" + claims.getSubject());
		    }
		    catch(ExpiredJwtException e) {
		    	logger.fatal(e.getMessage());
				return Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageSessionExpired).build();
		    }
		    catch(Exception e) {
		    	logger.fatal(e.getMessage());
				return Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageUnauthorized).build();
		    }

		    
			
			//TODO: INPROGRESS
			String processId = formParams.getFirst("processId");
			String currentTaskId = formParams.getFirst("currenTaskId");
			
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
			
			
			Timestamp createDate;
			Timestamp updateDate;
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			createDate = updateDate = currentTimestamp;
			
			Wip wip = new Wip();
			wip.setCreateDate(createDate);
			wip.setUpdateDate(updateDate);

			
			logger.info("creating process: " + processId + " and currentTaskId: " + currentTaskId);
			
			wip.setProcessId(Integer.parseInt(processId));
			wip.setCurrentTaskId(Integer.parseInt(currentTaskId));
			wip.setAccountId(Integer.parseInt(accountId));
			
			wipMapper.insertWip(wip);
			
			//LOG THE 'CREATION' OF THIS NEW WIP:
			LogMapper logMapper = mysqlsession.getMapper(LogMapper.class);
			Log log = new Log();
			log.setAccountId(Integer.parseInt(accountId));
			log.setNewStatus(currentTaskId);
			log.setWipId(wip.getId());
			log.setUpdatedDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
			log.setUserid(claims.getSubject());
			logMapper.insertLogRow(log);
			
			mysqlsession.commit();
			
			wipMapper.getTaskSummary();
			
			logger.info("new wipid: " + wip.getId());
			

			ProcessDataMapper processDataMapper = mysqlsession.getMapper(ProcessDataMapper.class);
			ProcessData processData = new ProcessData();
			processData.setAccountId(1);
			processData.setProcessId(1);  //TODO REMOVE HARDCODING
			processData = processDataMapper.getProcessDataKeys(processData);
			
			WipProcessDataMapper wipProcessDataMapper = mysqlsession.getMapper(WipProcessDataMapper.class);
			
			
			logger.info(processData.getKeys().toString());
			Iterator<String> processDataKeyIterator = processData.getKeys().iterator();
			while (processDataKeyIterator.hasNext()) {
				String processDataKey = processDataKeyIterator.next();
				String value = formParams.getFirst(processDataKey);
				logger.info(processDataKey + ": " + value);
				wipProcessDataMapper.insertWipProcessData(wip.getId(), processDataKey, value);
			}
			mysqlsession.commit();
			mysqlsession.close();
			
			JSONObject json = new JSONObject();
			json.put("message", "request successfully submitted");
			return Response.status(Response.Status.OK).entity(json).build();
		}
		catch(Exception e) {
			logger.fatal(e.toString()); 
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();		
	}
	
	

	
	public String getCookie(String cookieName) {
		Cookie[] cookies = servletRequest.getCookies();
		String userId = null;
		for(Cookie cookie : cookies){
		    if(WayFinderConstants.tokenName.equals(cookie.getName())){
		        userId = cookie.getValue();
		        logger.info(userId);
		        return userId;
		    }
		}
		return null;
	}
	
	
	
	public void expireCookie(String cookieName) {
		Cookie[] cookies = servletRequest.getCookies();
		for(Cookie cookie : cookies){
		    if(WayFinderConstants.tokenName.equals(cookie.getName())){
		        cookie.setMaxAge(0); 
		        return;
		    }
		}

	}
	
	
	private Claims verifyToken() throws ExpiredJwtException, Exception{
		//TODO PUT THIS IN A FILE OR CONSTANT?
		String mysecretkey = WayFinderConstants.secretKey;
		String jwt = getCookie(WayFinderConstants.tokenName);
		//Claims claims = Jwts.parser()         
		//			   .setSigningKey(TextCodec.BASE64.encode(mysecretkey))
		//			   .parseClaimsJws(jwt).getBody();
		try {
			Claims claims = Jwts.parser()         
					   .setSigningKey(DatatypeConverter.parseBase64Binary(mysecretkey))
					   .parseClaimsJws(jwt).getBody();
			return claims;
		}
		catch(Exception ex) {
			expireCookie(WayFinderConstants.tokenName);
			throw ex;
		}
	}

}
