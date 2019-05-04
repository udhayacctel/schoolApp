package com.sstutor.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstutor.model.User;
import com.sstutor.common.Utilities;
import com.sstutor.security.exception.AuthMethodNotSupportedException;

public class MobileLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger logger = LoggerFactory.getLogger(MobileLoginProcessingFilter.class);

    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    private DataSource dataSource;
    
    public MobileLoginProcessingFilter(String defaultProcessUrl,  
            AuthenticationFailureHandler failureHandler, ObjectMapper mapper, DataSource ds) {
        super(defaultProcessUrl);
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.dataSource = ds;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (!HttpMethod.POST.name().equals(request.getMethod()) || !Utilities.isAjax(request)) {
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        SignUpForm signupForm = objectMapper.readValue(request.getReader(), SignUpForm.class);
        
        logger.debug("User:" + signupForm.getMobile());
       // logger.debug("User:" + signupForm.getName());
        
        if (StringUtils.isBlank(signupForm.getMobile())) 
        {
        	throw new AuthenticationServiceException("Info missing for signup");
        }
        
    	User user = DBHandler.loginMobile(signupForm, dataSource);
    	
		if (user.getId() != null && user.getId() > 0)
		{			
			//List<GrantedAuthority> authorities = user.getUserRoles().stream()
	          //      .map(ur -> new SimpleGrantedAuthority(ur.getRole_name()))
	            //    .collect(Collectors.toList());
			//The grants we just put a empty list because we don't care about roles here - roles comes into pic only after login
			logger.debug("Going to return usernamepwdauthenticationtoken");
			return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
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
    	System.out.println("SignupProcessingFilter successfulAuthentication");
        //successHandler.onAuthenticationSuccess(request, response, authResult);
    	SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
    	System.out.println("SignupProcessingFilter unsuccessfulAuthentication");
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
