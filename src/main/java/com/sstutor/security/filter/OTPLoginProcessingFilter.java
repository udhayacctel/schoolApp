package com.sstutor.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstutor.model.User;
import com.sstutor.common.Utilities;
import com.sstutor.security.exception.AuthMethodNotSupportedException;

public class OTPLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger logger = LoggerFactory.getLogger(OTPLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    private DataSource dataSource;
    
    public OTPLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, 
            AuthenticationFailureHandler failureHandler, ObjectMapper mapper, DataSource ds) {
        super(defaultProcessUrl);
        logger.debug("OTPProcessingFilter constructor");
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.dataSource = ds;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    	logger.debug("OTPProcessingFilter attemptAuthentication:" + request.getHeader("X-FORWARDED-FOR"));
        if (!HttpMethod.POST.name().equals(request.getMethod()) || !Utilities.isAjax(request)) {
            logger.debug("Authentication method not supported. Request method: " + request.getMethod());
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        OTPLoginRequest loginRequest = objectMapper.readValue(request.getReader(), OTPLoginRequest.class);
        
        if (StringUtils.isBlank(loginRequest.getMobile()) || StringUtils.isBlank(loginRequest.getOtp())) {
            logger.error("User/pwd is blank");
        	throw new AuthenticationServiceException("Username or Password not provided");
        }

        //UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getMobile(), loginRequest.getOtp());
    	
    	User user = DBHandler.loginViaOTP(loginRequest.getMobile(), loginRequest.getOtp(), dataSource);
    	
		if (user.getId() != null && user.getId() > 0)
		{			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			Utilities.setAuthorities(authorities, user);
			logger.info("HELLO b4 setting the auth token in otploginprocess:" + user.getUser_token());
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
    	logger.debug("OTPLoginProcessingFilter successfulAuthentication");
        //successHandler.onAuthenticationSuccess(request, response, authResult);
    	SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
    	logger.debug("OTPLoginProcessingFilter unsuccessfulAuthentication");
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
