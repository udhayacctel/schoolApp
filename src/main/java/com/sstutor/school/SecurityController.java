package com.sstutor.school;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.sstutor.common.DBConnection;
import com.sstutor.exception.ExceptionDetail;
import com.sstutor.exception.GenericException;

import com.sstutor.model.User;
import com.sstutor.security.filter.DBHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	// Login where mobile number and OTP is sent
	// Send OTP to mobile
	@CrossOrigin()
	@RequestMapping(value = "/login/otp", method = RequestMethod.POST)
	public ResponseEntity<User> loginOTP(Authentication authentication) {

		
		
		Connection c = null;
		User user = (User) authentication.getPrincipal();
		System.out.println("HELLO YIPPEE IN END POINT CONTROLLER:" + user.getMobile() + "-token:" + user.getUser_token());

		try {

			if (user == null || user.getUser_token() == null || user.getUser_token().trim().length() <= 0) {
				throw new Exception("req. body missing");
			}

			String op = "{ }";

			c = dataSource.getConnection();

			String sql = "update user_record set is_login_valid = true where mobile = ?";
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt = c.prepareStatement(sql);
			pstmt.setString(1, user.getMobile());
			pstmt.execute();
			pstmt.close();
			c.close();
			return new ResponseEntity<User>(user, HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("loginOTP failed:" + e.getMessage());
		}
	}

	//Login with Mobile changes 
	@CrossOrigin()
	@RequestMapping(value = "/login/mobileno", method = RequestMethod.POST)
	public ResponseEntity<User> loginMobile(Authentication authentication) {
	
	//	Connection c = null;
		User user = (User) authentication.getPrincipal();
		logger.debug("returning the response - this is the end from our code");
		return new ResponseEntity<User>(user, HttpStatus.OK);
			
}

	
	
	// Signup process where user provides some pieces of info and gets an OTP
	// The information provided will be: Mobile number, dob (of child), mothers name
	// 
	@CrossOrigin()
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<User> signup(Authentication authentication) {

		//Connection c = null;
		User user = (User) authentication.getPrincipal();
		logger.debug("returning the response - this is the end from our code");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ExceptionDetail> myError(HttpServletRequest request, Exception exception) {
		System.out.println("SecurityController Exception:" + exception.getLocalizedMessage());
		ExceptionDetail error = new ExceptionDetail();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		error.setUrl(request.getRequestURL().toString());
		return new ResponseEntity<ExceptionDetail>(error, HttpStatus.BAD_REQUEST);
	}

}
