package edu.lehigh.lib.wayfinder.services;

import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.ProcessMapper;
import edu.lehigh.lib.wayfinder.data.TaskMapper;
import edu.lehigh.lib.wayfinder.data.UserMapper;
import edu.lehigh.lib.wayfinder.data.WipMapper;
import edu.lehigh.lib.wayfinder.data.WipProcessDataMapper;
import edu.lehigh.lib.wayfinder.models.Task;
import edu.lehigh.lib.wayfinder.models.User;
import edu.lehigh.lib.wayfinder.models.Wip;
import edu.lehigh.lib.wayfinder.models.WipProcessData;
import edu.lehigh.lib.wayfinder.models.Process;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;




@Path ("/process")
public class ProcessService {
	
	final static Logger logger = LogManager.getLogger(ProcessService.class);
	
	
	@Context
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	   

	@POST
	@Produces("application/json")
	@Path("/{accountId}/createProcess")
	public Response createProcessForAccount(@PathParam("accountId") String accountId,MultivaluedMap<String,String> formParams) {		
		SqlSession mysqlsession = null;
		try {
			
			String title = formParams.getFirst("title");
			String description = formParams.getFirst("description");
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			Timestamp createDate;
			Timestamp updateDate;
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
			Process proc = new Process();
			proc.setAccountId(Integer.parseInt(accountId));
			proc.setTitle(title);
			proc.setDescription(description);
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			createDate = updateDate = currentTimestamp;
			proc.setCreateDate(createDate);
			proc.setUpdateDate(updateDate);
			logger.info("Calling create process list with parameters: " + accountId + "," + title + ", " + description + "," + createDate.toString() + ", " + updateDate.toString());
			processMapper.createProcessForAccount(proc);
			mysqlsession.commit();
			return getProcessListForAccount(accountId);
		}
		catch(Exception e) {
			logger.info(e.toString()); 
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();		
	}
	
	@POST
	@Produces("application/json")
	@Path("/{accountId}/updateProcess/{processId}")
	public Response updateProcess(@PathParam("accountId") String accountId,
			                      @PathParam("processId") String processId,
			                      MultivaluedMap<String,String> formParams) {		
		SqlSession mysqlsession = null;
		try {
			
			String title = formParams.getFirst("title");
			String description = formParams.getFirst("description");
			
			ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
			Process proc = processMapper.getProcessById(processId);
			proc.setTitle(title);
			proc.setDescription(description);
			Timestamp updateDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			proc.setUpdateDate(updateDate);
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			logger.info("Calling update process list with parameters: " + processId + "," + title + ", " + description + "," + ", " + updateDate.toString());
			processMapper.updateProcess(proc);
			mysqlsession.commit();
			return getProcessListForAccount(accountId);
		}
		catch(Exception e) {
			logger.info(e.toString()); 
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();		
	}
	
	@POST
	@Produces("application/json")
	@Path("/{accountId}/deleteProcess/{processId}")
	public Response deleteProcess(@PathParam("accountId") String accountId,
			                      @PathParam("processId") String processId) {		
		SqlSession mysqlsession = null;
		try {
			
			ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
			Process proc = processMapper.getProcessById(processId); // fetch the process to make sure it exists

			Timestamp updateDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			proc.setUpdateDate(updateDate);
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			logger.info("Calling delete process with parameters: " + processId);
			processMapper.deleteProcess(proc.getId());
			mysqlsession.commit();
			return getProcessListForAccount(accountId);
		}
		catch(Exception e) {
			logger.info(e.toString()); 
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();		
	}
	
	@GET
	@Produces("application/json")
	@Path("/{accountId}/getProcess/{processId}")
	public Response getProcessById(@PathParam("processId") String processId) {
		SqlSession mysqlsession = null;
		try {
			
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
			Process proc = processMapper.getProcessById(processId);
			
			//JACKSON LIBRARY TO TRANSFORM OBJECT TO JSON
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    String json = objectMapper.writeValueAsString(proc);
			return Response.ok().entity(json).build();
		}
		catch(Exception e) {
			logger.info(e.toString());
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();

	}
	
	@GET
	@Produces("application/json")
	@Path("/{accountId}")
	public Response getProcessListForAccount(@PathParam("accountId") String accountId) {
		SqlSession mysqlsession = null;
		try {
			
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			ProcessMapper processMapper = mysqlsession.getMapper(ProcessMapper.class);
			List<Process> processList = processMapper.getProcessListForAccount(accountId);
			
			//JACKSON LIBRARY TO TRANSFORM OBJECT TO JSON
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    String json = objectMapper.writeValueAsString(processList);
			return Response.ok().entity(json).build();
		}
		catch(Exception e) {
			logger.info(e.toString());
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();

	}
	
	
	
	@GET
	@Produces("application/json")
	@Path("workinprogress/{processId}/status/{currentTask}")
	public Response getWorkInProgress(@PathParam("processId")String processId,@PathParam("currentTask") String currentTask) {
		SqlSession mysqlsession = null;
		try {
			
			String accountId = "1";  //hardcoding account for now...future?  json token?
			
			
			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			WipMapper wipMapper = mysqlsession.getMapper(WipMapper.class);
			List<Wip> wipProcessData = wipMapper.getWorkInProgress(accountId, processId, currentTask);
			
			//JACKSON LIBRARY TO TRANSFORM OBJECT TO JSON
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    String json = objectMapper.writeValueAsString(wipProcessData);
			return Response.ok().entity(json).build();
		}
		catch(Exception e) {
			logger.info(e.toString());
			
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();

	}
	
	
	
	
	@GET
	@Produces("application/json")
	@Path("tasks/{processId}")
	public Response getTasksForProcess(@PathParam("processId")String processId) {
		SqlSession mysqlsession = null;
		try {
			
			String accountId = "1";  //hardcoding account for now...future?  json token
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();
			
			TaskMapper taskMapper =mysqlsession.getMapper(TaskMapper.class);
			List<Task> taskList = taskMapper.getTaskListForProcess(accountId, processId);
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    String json = objectMapper.writeValueAsString(taskList);
			return Response.ok().entity(json).build();
		}
		catch(Exception e) {
			logger.info(e.toString());
		}
		finally {
			if (mysqlsession != null) mysqlsession.close();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(WayFinderConstants.messageServiceFailed).build();

	}
	


}
