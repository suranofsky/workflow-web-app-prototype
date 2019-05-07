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


import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;
import edu.lehigh.lib.wayfinder.data.TaskMapper;
import edu.lehigh.lib.wayfinder.models.Task;





@Path ("/task")
public class TaskService {
	
	final static Logger logger = LogManager.getLogger(TaskService.class);

	@Context
	private HttpServletRequest servletRequest;


	@POST
	@Produces("application/json")
	@Path("/{accountId}/{processId}/createTask")
	public Response createTaskForProcess(@PathParam("accountId") String accountId,
			@PathParam("processId") String processId,
			MultivaluedMap<String,String> formParams) {		
		SqlSession mysqlsession = null;
		try {

			int sequence = Integer.parseInt(formParams.getFirst("sequence"));
			String title = formParams.getFirst("title");
			String description = formParams.getFirst("description");
			int procId = Integer.parseInt(processId);
			int accId = Integer.parseInt(accountId);

			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();

			Timestamp createDate;
			Timestamp updateDate;
			//CREATE TASKMAPPER OBJECT - INTERFACE TO THE QUERIES
			TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
			Task t = new Task();
			t.setProcessId(procId);
			t.setSequence(sequence);
			t.setTitle(title);
			t.setDescription(description);
			t.setAccountId(accId);

			logger.info("Calling create task with parameters: " + accountId + "," + processId + ", " + title + ", " + description + "," + sequence);
			taskMapper.createTask(t);
			mysqlsession.commit();
			return getTaskListForProcess(processId);
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

	@POST
	@Produces("application/json")
	@Path("/{accountId}/{processId}/updateTask/{taskId}")
	public Response updateTask(@PathParam("accountId") String accountId,
			@PathParam("processId") String processId,
			@PathParam("taskId") String taskId,
			MultivaluedMap<String,String> formParams) {		
		SqlSession mysqlsession = null;
		try {

			String title = formParams.getFirst("title");
			String description = formParams.getFirst("description");
			int sequence = Integer.parseInt(formParams.getFirst("sequence"));

			TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
			Task t = taskMapper.getTaskById(taskId);
			t.setTitle(title);
			t.setDescription(description);
			t.setSequence(sequence);

			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();

			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			logger.info("Calling update task with parameters: " + processId + "," + title + ", " + description);
			taskMapper.updateTask(t);
			mysqlsession.commit();
			return getTaskListForProcess(processId);
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

	@POST
	@Produces("application/json")
	@Path("/{accountId}/{processId}/deleteTask/{taskId}")
	public Response deleteTask(@PathParam("accountId") String accountId,
			@PathParam("processId") String processId,
			@PathParam("processId") String taskId) {		
		SqlSession mysqlsession = null;
		try {

			TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
			Task t = taskMapper.getTaskById(taskId); // fetch the process to make sure it exists

			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();

			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			logger.info("Calling delete process with parameters: " + processId);
			taskMapper.deleteTask(t.getId());
			mysqlsession.commit();
			return getTaskListForProcess(processId);
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
	@Path("/{accountId}/{processId}/getTask/{taskId}")
	public Response getTaskById(@PathParam("accountId") String accountId,
			@PathParam("processId") String processId,
			@PathParam("taskId") String taskId) {
		SqlSession mysqlsession = null;
		try {


			//SET UP SESSION W/DATABASE
			String mysqlresource = WayFinderConstants.mybatisfile;
			InputStream mysqlinputStream = Resources.getResourceAsStream(mysqlresource);
			SqlSessionFactory mysqlsqlSessionFactory = new SqlSessionFactoryBuilder().build(mysqlinputStream);
			mysqlsession = mysqlsqlSessionFactory.openSession();

			//CREATE PROCESSMAPPER OBJECT - INTERFACE TO THE QUERIES
			TaskMapper taskMapper = mysqlsession.getMapper(TaskMapper.class);
			Task t = taskMapper.getTaskById(taskId);

			//JACKSON LIBRARY TO TRANSFORM OBJECT TO JSON
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			String json = objectMapper.writeValueAsString(t);
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
	public Response getTaskListForProcess(@PathParam("processId")String processId) {
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
