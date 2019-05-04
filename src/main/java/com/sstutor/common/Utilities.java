package com.sstutor.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.sstutor.model.AppRole;
import com.sstutor.model.User;
import com.sstutor.security.config.ModifiedGrantedAuthority;

public class Utilities {
	
	private static Logger logger = LoggerFactory.getLogger(Utilities.class);
	
	private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
	private static final String X_REQUESTED_WITH = "X-Requested-With";

	private static final String CONTENT_TYPE = "Content-type";
	private static final String CONTENT_TYPE_JSON = "application/json";
	
	public static boolean isAjax(HttpServletRequest request) {
	    return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
	}

	public static boolean isAjax(SavedRequest request) {
	    return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
	}

	public static boolean isContentTypeJson(SavedRequest request) {
	    return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
	}
	
	public static boolean setAuthorities(List<GrantedAuthority> authorities, User user)
	{
		boolean found = false;
		//Is this really needed - because we have the whole access details in User object itself - maybe can just do a simple authority
		if (user.getUserRole().getParent())
		{
			ModifiedGrantedAuthority auth = new ModifiedGrantedAuthority("parent");
			//auth.setClassIDs(user.getUserRole().getStudentIDs());
			authorities.add(auth);
			found = true;
		}
		if (user.getUserRole().getTeacher())
		{
			ModifiedGrantedAuthority auth = new ModifiedGrantedAuthority("teacher");
			//auth.setClassIDs(user.getUserRole().getClassIDs());
			authorities.add(auth);
			found = true;
		}
		return found;
	}
}
