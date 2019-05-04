package com.sstutor.security.token;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class ApiTokenExtractor {
	
	private static Logger logger = LoggerFactory.getLogger(ApiTokenExtractor.class);
    public static String HEADER_PREFIX = "Bearer ";
    public static String HEADER_PREFIX_ID = "BearerId ";

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        logger.debug("hello the extracted token is:" + header.substring(HEADER_PREFIX.length(), header.length()));
        return header.substring(HEADER_PREFIX.length(), header.length());
    }

    public String extractid(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < HEADER_PREFIX_ID.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        logger.debug("hello the extracted token is:" + header.substring(HEADER_PREFIX_ID.length(), header.length()));
        return header.substring(HEADER_PREFIX_ID.length(), header.length());
    }

}
