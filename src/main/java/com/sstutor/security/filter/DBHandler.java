package com.sstutor.security.filter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.List;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import com.sstutor.model.AppRole;
import com.sstutor.model.School_class_year;
import com.sstutor.model.User;
import com.sstutor.model.SchoolImage;
import com.sstutor.model.Master;


public class DBHandler {

	private static final Logger logger = LoggerFactory.getLogger(DBHandler.class);
	private static String url;
	private static String start_time;
	private static String end_time;


	public static int recordVerifyStats(DataSource dataSource, Long user, String ip, String device, String platform, 
										String request_url, int period)
	{
		Connection c = null;
		ResultSet res;
		int record_count = 0;
		
		try {
			
			if (dataSource == null)
				logger.error("There is no datasource");
			
			c = dataSource.getConnection();
			
			String sql = "select public.record_and_verify_stats(?,?,?,?,?,10) as op";
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, user);
			pstmt.setString(2, ip);
			pstmt.setString(3, device);
			pstmt.setString(4, platform);
			pstmt.setString(5, request_url);
			
			res = pstmt.executeQuery();
			
			while (res.next())
			{
				record_count = res.getInt("op");
			}

			pstmt.close();
			
			c.close();
			
		}
		catch (Exception e) {
				logger.error(e.getMessage());
				throw new BadCredentialsException("Record stats failed");
		}

		return record_count;
	}
	
	//public static long parseLong(String s) throws NumberFormatException;

	public static User verifyToken(String tokenPayload, String tokenPayloadid, DataSource dataSource)
	{
		
		System.out.println("In DBHandler:" + tokenPayload + tokenPayloadid);
		Long id = Long.parseLong(tokenPayloadid);
		
		Connection c = null;
		ResultSet res;
		Boolean found = false;
		
		User user = new User();
		
		try {
			
			if (dataSource == null)
				logger.error("There is no datasource");
			
			c = dataSource.getConnection();
			
			/*
			String sql = "select u.tg_id as id, user_token, email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id "
					+ " from user_record u, parent_child ps, student_class sc where u.tg_id = ps.parent_id and ps.student_id = sc.student_id "
					+ " and user_token = ? and is_login_valid = true"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id "
					+ " from user_record u, teacher_class tc where u.tg_id = tc.teacher_id "
					+ " and user_token = ? and is_login_valid = true"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id "
					+ " from user_record u, student_class sc where u.tg_id = sc.student_id "
					+ " and user_token = ? and is_login_valid = true";
			*/
			
			/*String sql = "select u.tg_id as id, u.user_token, u.email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id, u_student.name as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year"
					+ " from user_record u, parent_child ps, student_class sc, user_record u_student, school_class_year scy where u.tg_id = ps.parent_id and ps.student_id = sc.student_id and sc.class_id = scy.id and u_student.tg_id = ps.student_id "
					+ " and u.user_token = ? and u.is_login_valid = true"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year "
					+ " from user_record u, teacher_class tc, school_class_year scy where u.tg_id = tc.teacher_id and tc.class_id = scy.id"
					+ " and user_token = ? and is_login_valid = true"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year "
					+ " from user_record u, student_class sc, school_class_year scy  where u.tg_id = sc.student_id and sc.class_id = scy.id"
					+ " and user_token = ? and is_login_valid = true";*/
			
			String sql = "select u.tg_id as id, u.user_token, u.email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id, u_student.name as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time"
					+ "	from user_record u join parent_child ps on u.tg_id = ps.parent_id "
					+ "join student_class sc on ps.student_id = sc.student_id "
					+ "join user_record u_student on u_student.tg_id = ps.student_id "
					+ "join school_class_year scy on sc.class_id = scy.id"
					+ " left outer join school_images as si on si.tg_id = u.tg_id "
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ "where u.user_token = ? and u.tg_id = ? and u.is_login_valid = true"
					+ " UNION "
					+ "select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time "
					+ "from user_record u join teacher_class tc on u.tg_id = tc.teacher_id"
					+ " join school_class_year scy on tc.class_id = scy.id "
					+ "left outer join school_images as si on si.tg_id = u.tg_id"
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ " where user_token = ? and u.tg_id = ? and is_login_valid = true"
					+ " UNION "
					+ "  select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time "
					+ "from user_record u join student_class sc on u.tg_id = sc.student_id"
					+ " join school_class_year scy on sc.class_id = scy.id "
					+ "left outer join school_images as si on si.tg_id = u.tg_id "
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ "where user_token = ? and u.tg_id = ? and is_login_valid = true";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, tokenPayload);
			pstmt.setLong(2, id);
			pstmt.setString(3, tokenPayload);
			pstmt.setLong(4, id);
			pstmt.setString(5, tokenPayload);
			pstmt.setLong(6, id);

			System.out.println("db 1");
			res = pstmt.executeQuery();
			System.out.println("db 2");
			
			found = setRoles(res, user);
			
			pstmt.close();
			
			c.close();
			
		}
		catch (Exception e) {
				logger.error(e.getMessage());
				throw new BadCredentialsException("Verification failed");
		}

		if (found)
		{
			logger.debug("Returning auth token");
		}
		else
		{
			logger.error("No user found");
			throw new BadCredentialsException("Authentication Failed");
		}
		return user;
	}
	
	
	//If the user entered data is valid then generate a user token and update db
	//This token will be used in api calls
	public static User loginViaOTP(String mobile, String otp, DataSource dataSource)
	{
        List<String> us = new ArrayList<String>();

		logger.debug(otp);
		
		User user = new User();
		user.setMobile(mobile);
		
		//Connection c = null;
		ResultSet res;
		Boolean found = false;
		
		try (Connection c = dataSource.getConnection()) 
		{

/*			String sql = "select u.tg_id as id, user_token, email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id"
					+ " from user_record u, parent_child ps, student_class sc where u.tg_id = ps.parent_id and ps.student_id = sc.student_id "
					+ " and mobile = ? and otp = ? and now() <= otp_valid_till::timestamp"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id "
					+ " from user_record u, teacher_class tc where u.tg_id = tc.teacher_id "
					+ " and mobile = ? and otp = ? and now() <= otp_valid_till::timestamp"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id "
					+ " from user_record u, student_class sc where u.tg_id = sc.student_id "
					+ " and mobile = ? and otp = ? and now() <= otp_valid_till::timestamp";
*/
			
			/*String sql = "select u.tg_id as id, u.user_token, u.email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id, u_student.name as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url"
					+ " from user_record u, parent_child ps, student_class sc, user_record u_student, school_class_year scy, school_images si where u.tg_id = ps.parent_id and ps.student_id = sc.student_id and sc.class_id = scy.id and u_student.tg_id = ps.student_id and si.tg_id = u.tg_id"
					+ " and u.mobile = ? and u.otp = ? and now() <= u.otp_valid_till::timestamp"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url"
					+ " from user_record u, teacher_class tc, school_class_year scy, school_images si where u.tg_id = tc.teacher_id and tc.class_id = scy.id and si.tg_id = u.tg_id"
					+ " and mobile = ? and otp = ? and now() <= otp_valid_till::timestamp"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url"
					+ " from user_record u, student_class sc, school_class_year scy, school_images si where u.tg_id = sc.student_id and sc.class_id = scy.id and si.tg_id = u.tg_id"
					+ " and mobile = ? and otp = ? and now() <= otp_valid_till::timestamp";*/
			
			String sql = "select u.tg_id as id, u.user_token, u.email, u.name, ps.student_id as linked_id, 'PARENT' as role, sc.class_id as parent_class_id, u_student.name as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time "
					+ "from user_record u join parent_child ps on u.tg_id = ps.parent_id "
					+ "JOIN student_class sc ON ps.student_id = sc.student_id "
					+ "JOIN user_record u_student ON u_student.tg_id = ps.student_id "
					+ "JOIN school_class_year scy ON sc.class_id = scy.id "
					+ "left join school_images as si on u.tg_id = si.tg_id "
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ "where u.mobile = ? and u.otp = ? and now() <= u.otp_valid_till::timestamp"
					+ " UNION "
					+ " select u.tg_id as id, user_token, email, u.name, tc.class_id as linked_id, 'TEACHER' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time"
					+ " from user_record u join teacher_class tc on u.tg_id = tc.teacher_id "
					+ " join school_class_year scy on tc.class_id = scy.id"
					+ " left outer join school_images as si on si.tg_id = u.tg_id  "
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ "where mobile = ? and otp = ? and now() <= otp_valid_till::timestamp"
					+ " UNION "
					+ "select u.tg_id as id, user_token, email, u.name, sc.class_id as linked_id, 'STUDENT' as role, null as parent_class_id, null as parent_student_name, scy.standard as standard, scy.section as section, scy.acad_year as acad_year, si.url as url, scc.start_time as start_time, scc.end_time as end_time "
					+ "from user_record u join student_class sc on u.tg_id = sc.student_id"
					+ " join school_class_year scy on sc.class_id = scy.id"
					+ " left outer join school_images as si on si.tg_id = u.tg_id "
					+ " left outer join school_constants as scc on scy.school_id= scc.school_id "
					+ "where mobile = ? and otp = ? and now() <= otp_valid_till::timestamp";
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, mobile);
			pstmt.setString(2, otp);
			pstmt.setString(3, mobile);
			pstmt.setString(4, otp);
			pstmt.setString(5, mobile);
			pstmt.setString(6, otp);
			res = pstmt.executeQuery();
			
			found = setRoles(res, user);
			if (user.getUserRole() != null)
				logger.debug("we got some role set here");
			
			pstmt.close();
			
			if (found)
			{

		        UUID uuid = UUID.randomUUID();
		        String uuidToken = uuid.toString();
		        user.setUser_token(uuidToken);
		        logger.info("THE TOKEN WE UPDATE IS:" + user.getUser_token());
		        sql = "update user_record set user_token = ? where tg_id = ? ";
				pstmt = c.prepareStatement(sql);
				pstmt.setString(1, uuidToken);
				pstmt.setLong(2, user.getId());
				boolean t = pstmt.execute();
				logger.info("HELLO UPDATE:" + t);
				pstmt.close();
				
				logger.debug("Token generated and updated");
			}
			else
			{
				logger.error("User not found");
				throw new BadCredentialsException("Authentication Failed");
			}
			
		}
		catch (Exception e) {
				logger.error(e.getMessage());
				throw new BadCredentialsException("Authentication Failed for you");
		}
		
		return user;
	}
	

	
	public static Boolean setRoles(ResultSet res, User user) throws SQLException
	{
		AppRole appRole = new AppRole();
		
		appRole.setParent(false);
		appRole.setAdmin(false);
		appRole.setTeacher(false);
		appRole.setStudent(false);
		Boolean found = false;
		Boolean first = true;
		
		while (res.next()) {
			logger.debug("User found");
			if (first)
			{
				user.setId(res.getLong("id"));
				//user.setUser_token(res.getString("user_token"));
				user.setEmail(res.getString("email"));
				user.setName(res.getString("name"));
				user.seturl(res.getString("url"));
				user.setstart_time(res.getString("start_time"));
				user.setend_time(res.getString("end_time"));
				
				System.out.println("image url" + url + start_time + end_time);
				first = false;
			}
			switch (res.getString("role"))
			{
				//whenever parent we create a new student record and add to the list
				case "PARENT":
					logger.debug("HELLO adding parent class ID:" + res.getLong("parent_class_id"));
					
					appRole.setParent(true);
					
					//This class is the one for the current student linked to this parent
					School_class_year scy = new School_class_year();
					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("parent_class_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					
					//There is a chance of the same student appearing in 2 records - why? Because one student can be assigned to multiple academic year classes
					// So search first to see if this student is present or not and then append to that record or add new students entry
					boolean foundStudent = false;
					
					if (appRole.getParent())
					{
						Long current_student_id = res.getLong("linked_id");
						//Check for each student entry if the student id matches the id we have
						for (User u: appRole.getStudents())
						{
							//==0 means values =
							if (current_student_id.compareTo(u.getId()) == 0)
							{
								foundStudent = true;
								u.getUserRole().getClasses().add(scy);
							}
						}
					}
					
					if (!foundStudent)
					{
						User u = new User();
						
						u.setId(res.getLong("linked_id"));
						u.setName(res.getString("parent_student_name"));

						AppRole ar = new AppRole();
						ar.setStudent(true);
						ar.getClasses().add(scy);
						u.setUserRole(ar);
						appRole.getStudents().add(u);

					}
					
					break;
					
				//Simpler situation to just keep adding new school_class_year entries	
				case "TEACHER":

					appRole.setTeacher(true);
					
					scy = new School_class_year();

					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("linked_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					
					appRole.getClasses().add(scy);
					
					break;
					
					
				//This is also simple - just add classes
				case "STUDENT":
					appRole.setStudent(true);
					
					scy = new School_class_year();
					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("linked_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					appRole.getClasses().add(scy);					
					
					break;
			}
			found = true;
		}
		if (found)
		{
			user.setUserRole(appRole);
			logger.debug("user role set");
			
		}
		return found;
	}
	
	/*
	public static Boolean setRolesOtp(ResultSet res, User user) throws SQLException
	{
		AppRole appRole = new AppRole();
		
		appRole.setParent(false);
		appRole.setAdmin(false);
		appRole.setTeacher(false);
		appRole.setStudent(false);
		Boolean found = false;
		Boolean first = true;
		
		while (res.next()) {
			logger.debug("User found");
			ArrayList<User> us = new ArrayList<User>();
			User u1 = new User();
			if (first)
			{
				
				u1.setId(res.getLong("id"));
				//user.setUser_token(res.getString("user_token"));
				u1.setEmail(res.getString("email"));
				u1.setName(res.getString("name"));
				u1.seturl(res.getString("url"));
				//us.add(u1);
				
				System.out.println("image url" + url);
				first = false;
			}
			switch (res.getString("role"))
			{
				//whenever parent we create a new student record and add to the list
				case "PARENT":
					logger.debug("HELLO adding parent class ID:" + res.getLong("parent_class_id"));
					
					appRole.setParent(true);
					
					//This class is the one for the current student linked to this parent
					School_class_year scy = new School_class_year();
					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("parent_class_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					
					//There is a chance of the same student appearing in 2 records - why? Because one student can be assigned to multiple academic year classes
					// So search first to see if this student is present or not and then append to that record or add new students entry
					boolean foundStudent = false;
					
					if (appRole.getParent())
					{
						Long current_student_id = res.getLong("linked_id");
						//Check for each student entry if the student id matches the id we have
						for (User u: appRole.getStudents())
						{
							//==0 means values =
							if (current_student_id.compareTo(u.getId()) == 0)
							{
								foundStudent = true;
								u.getUserRole().getClasses().add(scy);
								u1.setUserRole(u.getUserRole().getClasses().add(scy));

							}
						}
					}
					
					if (!foundStudent)
					{
						User u = new User();
						
						u.setId(res.getLong("linked_id"));
						u.setName(res.getString("parent_student_name"));

						AppRole ar = new AppRole();
						ar.setStudent(true);
						ar.getClasses().add(scy);
						u.setUserRole(ar);
						appRole.getStudents().add(u);

					}
					
					break;
					
				//Simpler situation to just keep adding new school_class_year entries	
				case "TEACHER":

					appRole.setTeacher(true);
					
					scy = new School_class_year();

					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("linked_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					appRole.getClasses().add(scy);
					
					break;
					
					
				//This is also simple - just add classes
				case "STUDENT":
					appRole.setStudent(true);
					
					scy = new School_class_year();
					scy.setacad_year(res.getString("acad_year"));
					scy.setid(res.getLong("linked_id"));
					scy.setsection(res.getString("section"));
					scy.setstandard(res.getString("standard"));
					appRole.getClasses().add(scy);					
					
					break;
			}
			found = true;
		}
		if (found)
		{
			user.setUserRole(appRole);
			logger.debug("user role set");
			
		}
		return found;
	}*/

	
	//1. Verify if the given input combo exists in schools records
	//2. If it does then check if the mobile already is registered in app user db
	//2.a If yes then generate a OTP, update OTP field and send to user - blank the token field; set valid login to false 
	
	public static User signup(SignUpForm suf , DataSource dataSource)
	{
		logger.debug("signup()");
		
		ResultSet res;
		Boolean found = false;
		
		User user = new User();

		try (Connection c = dataSource.getConnection()) 
		{
			logger.debug("obtained connection");

			
			String sql = "select tg_id as id, user_token, email, name from user_record where mobile = ?  and name = ?";
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, suf.getMobile());
			pstmt.setString(2, suf.getName());
			res = pstmt.executeQuery();		
			logger.debug("master query executed");
			
			//Should only get one row here
			while (res.next())
			{
				user.setMobile(suf.getMobile());
				user.setName(suf.getName());
				user.setId(res.getLong("id"));
				found = true;
				logger.debug("obtained id:" + user.getId());
			}
			
			pstmt.close();
			
			if (found)
			{
				
				//Whenever otp generated set is_login_valid to false
				
				String otp = generateOTP().toString();

				logger.debug("going to update otp");
				sql = "update user_record set is_login_valid = false, "
						+ " otp = ?, otp_valid_till = now() + interval '30 minutes' where mobile = ?";
				pstmt = c.prepareStatement(sql);
				pstmt.setString(1, otp);
				pstmt.setString(2, suf.getMobile());
				logger.debug("going to update otp query executed:" + otp + ":" + suf.getMobile() + ";");
				
				if (pstmt.executeUpdate() > 0)
				{
					user.setOtp(otp);
					logger.debug("updated otp details");
				}
				else
					throw new BadCredentialsException("Updating failed");
				
				logger.debug("going to update otp query pstmt to be closed");
				pstmt.close();
			}
	
			
			c.close();
			
		}
		catch (Exception e) {
				logger.error("Exception:" + e.getMessage());
				throw new BadCredentialsException("Authentication Failed for you");
		}

		return user;
	}
	
	//1. Verify the mobile Number is existing in the code. 
	//2. If exists then publish the OTP for signing in.
	//2.a If yes then generate a OTP, update OTP field and send to user - blank the token field; set valid login to false 
	
	public static User loginMobile(SignUpForm suf , DataSource dataSource)
	{
		logger.debug("loginMobil()");
		
		ResultSet res;
		Boolean found = false;
		
		User user = new User();

		try (Connection c = dataSource.getConnection()) 
		{
			logger.debug("obtained connection");

			
			String sql = "select tg_id as id, user_token from user_record where mobile = ?";
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, suf.getMobile());
			res = pstmt.executeQuery();		
			logger.debug("master query executed");
			
			//Should only get one row here
			while (res.next())
			{
				user.setMobile(suf.getMobile());
				user.setId(res.getLong("id"));
				found = true;
				logger.debug("obtained id:" + user.getId());
			}
			
			pstmt.close();
			
			if (found)
			{
				
				//Whenever otp generated set is_login_valid to false
				
				String otp = generateOTP().toString();

				logger.debug("going to update otp");
				sql = "update user_record set is_login_valid = false, "
						+ " otp = ?, otp_valid_till = now() + interval '30 minutes' where mobile = ?";
				pstmt = c.prepareStatement(sql);
				pstmt.setString(1, otp);
				pstmt.setString(2, suf.getMobile());
				logger.debug("going to update otp query executed:" + otp + ":" + suf.getMobile() + ";");
				
				if (pstmt.executeUpdate() > 0)
				{
					user.setOtp(otp);
					logger.debug("updated otp details");
				}
				else
					throw new BadCredentialsException("Updating failed");
				
				logger.debug("going to update otp query pstmt to be closed");
				pstmt.close();
			}
	
			
			c.close();
			
		}
		catch (Exception e) {
				logger.error("Exception:" + e.getMessage());
				throw new BadCredentialsException("Authentication Failed for you");
		}

		return user;
	}
	
	public static Integer generateOTP()
	{
		Random r = new Random();
		return r.nextInt(5000) + 5000;
	}
	
}
