package com.sstutor.security.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstutor.common.Utilities;
import com.sstutor.model.AppRole;
import com.sstutor.model.User;
import com.sstutor.security.token.ApiAuthToken;
import com.sstutor.security.token.ApiTokenExtractor;
import com.sstutor.security.config.SecurityConfig;

public class ApiTokenAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger logger = LoggerFactory.getLogger(ApiTokenAuthFilter.class);

    
    private final AuthenticationFailureHandler failureHandler;
    
    private final ApiTokenExtractor userTokenExtractor;
    DataSource dataSource;
    
    public ApiTokenAuthFilter(AuthenticationFailureHandler failureHandler, 
            ApiTokenExtractor tokenExtractor, RequestMatcher matcher, DataSource ds) 
    {
        super(matcher);
        System.out.println("ApiTokenAuthFilter constructor");
        this.failureHandler = failureHandler;
        this.userTokenExtractor = tokenExtractor;
        dataSource = ds;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    	logger.debug("ApiTokenAuthFilter attemptAuthentication:" + request.getHeader("X-FORWARDED-FOR"));
    	logger.debug("ApiTokenAuthFilter attemptAuthenticationdevicxe:" + request.getHeader("X-DEVICE"));
    	logger.debug("ApiTokenAuthFilter attemptAuthentication platform:" + request.getHeader("X-PLATFORM"));
    	logger.debug("ApiTokenAuthFilter attemptAuthentication2:" + request.getRemoteAddr());
    	
    	String tokenPayload = request.getHeader(SecurityConfig.JWT_TOKEN_HEADER_PARAM);
    	String tokenPayloadid = request.getHeader(SecurityConfig.JWT_TOKEN_HEADER_PARAM_ID);
    	
    	logger.debug("ApiTokenAuthFilter attemptAuthentication3:" + tokenPayload);
    	logger.debug("ApiTokenAuthFilter attemptAuthentication3:" + tokenPayloadid);

    	logger.debug("Hello getURL:" + request.getRequestURI());
    	
    	String device = request.getHeader("X-DEVICE");
    	String platform = request.getHeader("user-agent");    //doesnt work request.getHeader("X-PLATFORM");
    		
    	//String ip = request.getHeader("X-FORWARDED-FOR");
    	
    	Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
          String name = (String) names.nextElement();
          Enumeration values = request.getHeaders(name); // support multiple values
          if (values != null) {
            while (values.hasMoreElements()) {
              String value = (String) values.nextElement();
              logger.debug(name + ": " + value);
            }
          }
        }
    	
        String ip = "";

        if (request != null) {
            ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null || "".equals(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || "".equals(ip)) {
                ip = request.getHeader("referer");
            }
        }
        
    	
    	logger.debug("USER INFO:" + device + "--platform:" + platform + "--ip:" + ip);
    	
        //return this.getAuthenticationManager().authenticate(new ApiAuthToken(userTokenExtractor.extract(tokenPayload)));
    	
    	//Ideally above should work but for some reason if we put that it never goes to calling authenticate() and just fails
    	//UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userTokenExtractor.extract(tokenPayload),null);
    	
    	User user = DBHandler.verifyToken(userTokenExtractor.extract(tokenPayload), userTokenExtractor.extractid(tokenPayloadid), dataSource);
    	int hits = 0;
    	if (user.getId() != null)
    		hits = DBHandler.recordVerifyStats(dataSource, user.getId(), ip, device, platform, request.getRequestURI(), 10);
    	else
    		hits = DBHandler.recordVerifyStats(dataSource, null, ip, device, platform, request.getRequestURI(), 10);
    	
		if (user.getId() != null && user.getId() > 0 && hits < 10)
		{			
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			Utilities.setAuthorities(authorities, user);
			
			//List<GrantedAuthority> authorities = user.getUserRoles().stream()
	         //       .map(ur -> new SimpleGrantedAuthority(ur.getRole_name()))
	         //       .collect(Collectors.toList());	
			//Along with this we have to verify that what the user is asking for is allowed
			//verifyRolePermission(user, request);
			
			return new UsernamePasswordAuthenticationToken(user, null, authorities);
		}
		else
		{
			logger.error("Didn't find any record in db");
			throw new BadCredentialsException("Authentication Failed");
		}

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
    	System.out.println("ApiTokenAuthFilter successfulAuthentication");
    	SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
    	System.out.println("ApiTokenAuthFilter unsuccessfulAuthentication");
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
    
    
    //We have entities like class, school, timetable etc. and for each of these there are multiple actions.
    //Based on the role we have to set the mapping as to what is allowed and what isn't
    //By default nothing is allowed - so everything has to be conditional permission
    
    //Pending we aren't checking if the current id is allowed access to the particular school or student or class here
    //WE NEED TO DO THAT - FOR NOW JUST PLACING the logic
    /*private boolean verifyRolePermission(User user, HttpServletRequest req)
    {
    	//logger.info("Verify role query string:" + getQueryString());
    	//logger.info("Verify role get request URL:" + getRequestURL());
    	//logger.info("Verify role request URI:" + getRequestURI());
    	
    	String requestedService = req.getRequestURI();
    	String httpMethod = req.getMethod();
    	
    	//User can have multiple roles; if the requested service is allowed for any role then pass through
    	for (AppRole appRole : user.getUserRoles())
    	{
    		
    	}
    	
    	
    	return false;
    }*/
    
}
