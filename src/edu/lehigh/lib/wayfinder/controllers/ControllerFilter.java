package edu.lehigh.lib.wayfinder.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.lehigh.lib.wayfinder.constants.WayFinderConstants;

public class ControllerFilter implements Filter {
	
	final static Logger logger = LogManager.getLogger(ControllerFilter.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
		
		Claims claims = null;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession(false);
        String loginURI = httpServletRequest.getContextPath() + "/login";
		
	    try {
	    	claims = verifyToken(httpServletRequest);
	    	System.out.println("subject" + claims.getSubject());
	    	httpServletResponse.addCookie(getAccessToken(httpServletRequest));
	    	filterChain.doFilter(httpServletRequest, httpServletResponse);
	    	
	    }
	    
	    catch(ExpiredJwtException e) {
	    	logger.fatal(e.getMessage());
	    	httpServletResponse.sendRedirect(loginURI);
	    	
	    	
	    }
	    catch(Exception e) {
	    	 logger.fatal(e.getMessage());
	    	 httpServletResponse.sendRedirect(loginURI);
	    }
	    
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	

	private Claims verifyToken(HttpServletRequest httpServletRequest) throws ExpiredJwtException, Exception{

		String mysecretkey = WayFinderConstants.secretKey;
		String jwt = getCookie(WayFinderConstants.tokenName,httpServletRequest);
		try {
			Claims claims = Jwts.parser()         
					.setSigningKey(DatatypeConverter.parseBase64Binary(mysecretkey))
					.parseClaimsJws(jwt).getBody();
			return claims;
		}

		catch(Exception ex) {
			expireCookie(WayFinderConstants.tokenName,httpServletRequest);
			throw ex;

		}

	}
	public String getCookie(String cookieName,HttpServletRequest httpServletRequest) {
		Cookie[] cookies = httpServletRequest.getCookies();
		String userId = null;
		for(Cookie cookie : cookies){
			if(WayFinderConstants.tokenName.equals(cookie.getName())){
				userId = cookie.getValue();
				System.out.println(userId);
				return userId;
			}
		}
		return null;
	}



	public void expireCookie(String cookieName,HttpServletRequest httpServletRequest) {
		Cookie[] cookies = httpServletRequest.getCookies();
		for(Cookie cookie : cookies){
			if(WayFinderConstants.tokenName.equals(cookie.getName())){
				cookie.setMaxAge(0); 
				return;
			}
		}

	}
	
	
	public Cookie getAccessToken(HttpServletRequest r) {
		Cookie[] cookies = r.getCookies();
		for(Cookie cookie : cookies){
		    if(WayFinderConstants.tokenName.equals(cookie.getName())){
		    	return cookie;
		    }
		}
		return null;
	}

}
