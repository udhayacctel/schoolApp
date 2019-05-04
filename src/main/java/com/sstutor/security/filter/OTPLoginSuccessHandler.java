package com.sstutor.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstutor.model.User;
import com.sstutor.security.config.SecurityConfig;

@Component("OTPLoginSuccessHandler")
public class OTPLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper mapper;
    
    private static final Logger logger = LoggerFactory.getLogger(OTPLoginSuccessHandler.class);

    @Autowired
    public OTPLoginSuccessHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        logger.debug("success authentication handler for OTP login");
    	User user = (User) authentication.getPrincipal();
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("email", user.getEmail());
        tokenMap.put("id", user.getId().toString());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     * 
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}

