package edu.lehigh.lib.wayfinder.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;

@Provider
public class JaxRsFilterAuthentication implements ContainerRequestFilter {
	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	
	
	final static Logger logger = LogManager.getLogger(JaxRsFilterAuthentication.class);
	
	@Context
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws WebApplicationException {

		Claims claims = null;
	    try {
	    	claims = verifyToken();
	    	System.out.println("subject" + claims.getSubject());
	    }
	    catch(ExpiredJwtException e) {
	    	logger.fatal(e.getMessage());
	    	requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageSessionExpired).build());
	    }
	    catch(Exception e) {
	    	logger.fatal(e.getMessage());
	    	requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(WayFinderConstants.messageUnauthorized).build());
	    }

	}
	
	
	private Claims verifyToken() throws ExpiredJwtException, Exception{

		String mysecretkey = WayFinderConstants.secretKey;
		String jwt = getCookie(WayFinderConstants.tokenName);

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
	
	
	public void expireCookie(String cookieName) {
		Cookie[] cookies = servletRequest.getCookies();
		for(Cookie cookie : cookies){
		    if("access_token".equals(cookie.getName())){
		        cookie.setMaxAge(0); 
		        return;
		    }
		}

	}
	
	

	
	public String getCookie(String cookieName) {
		Cookie[] cookies = servletRequest.getCookies();
		String userId = null;
		for(Cookie cookie : cookies){
			if("access_token".equals(cookie.getName())){
				userId = cookie.getValue();
				System.out.println(userId);
				return userId;
			}
		}
		return null;
	}

}
