package com.sstutor.common;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sstutor.model.AppRole;
import com.sstutor.model.School_class_year;
import com.sstutor.model.User;
import com.sstutor.model.Template;


public class SecurityUtilities {

	
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtilities.class);
	
	
	//Get user's role
	//Whenever target user is a student and if the calling user is a teacher, use the student's class ID because teacher will have access at class level
	//If admin - everything allowed; no need to check further
	//If teacher - teacher allowed full access to the class they have access to
	
	//targetOwnerID - this is mainly needed for the case where targetEntity te is STUDENT; the owner is the group under which the 
	//    targetEntity is a part of; for a student this will be a class; this is needed because for a Teacher using the app, 
	//    the role for the teacher will have access at class level and we won't store all student ids.
	
	//Ensure that in the DB call in the controller class, that the targetID and targetOwnerID are always used in the where clause and 
	//    not just used for calling verifyAccess().
 
	//TargetDomain is what we are trying to get/update
	//TargetEntity is what the domain maps to - STUDENT, TEACHER, PARENT, CLASS, SCHOOL; the domain should be for one of these entities
	
	public static boolean verifyAccess(Authentication auth, RequestMethod rm, Constants.TargetDomain td, 
										Constants.TargetEntity te, Long targetID,
										Constants.TargetEntity teOwner, Long targetOwnerID
										)
	{
		
		if (auth == null || auth.getPrincipal() == null)
		{
			logger.error("Authentication object not set");
			return false;
		}
		User user = (User) auth.getPrincipal();
		
		AppRole role = user.getUserRole();
		boolean isParent = role.getParent();
		boolean isAdmin = role.getAdmin();
		boolean isTeacher = role.getTeacher();
		boolean isStudent = role.getStudent();
		
		boolean edit;
		
		if (isAdmin)
			return true;
		
		switch (rm)
		{
			case GET:
				edit = false;
				break;
			case PUT:
			case POST:
			case DELETE:
				edit = true;
				break;
			default:
				logger.error("Problem in verifying access - HTTP verb incorrect:" + rm);
				return false;
		}
		
		switch (td)
		{
			//Nedd school id to be passed for this
			case CLASS:
			case SUBJECT:
			case TEACHER:
			case PARENT_CHILD:
				return adminEditTeacherReadParentBlock(role, edit);
				
			case REFERENCE_TIMETABLE1:
				if (te == Constants.TargetEntity.CLASS)
				{
					boolean allowed = false;
					for (School_class_year scy: role.getClasses())
					{
						if (scy.getid() == targetID)
							allowed = true;
					}
					if (!allowed)
						return false;
				}
				else
					return false;
				
				return adminEditTeacherReadParentBlock(role, edit);
				
			case SCHOOL:
			case NOTIFICATION:
			//case DAILYDIARY: 	

				return adminEditTeacherReadParentRead(role, edit);
			
			//this is at class level
			case DAILYDIARY: 	
			case ATTENDANCE: 	
			case SUBJECT_CLASS: 				
			case EXAM: 									
			case TIMETABLE:
			case QUIZ:
			case STUDENT_LIST:
			case PARENT_TEACHER_MEETING:
			case CLASS_DIARY_UPDATES: 		
			case REFERENCE_TIMETABLE:
			//case TEMPLATE:

				logger.debug("HELLO the targetID is:" + targetID);
				HashSet<Long> temp = null; 
				boolean allowed = false;
				if (isParent)
				{
					for (User u: role.getStudents())
					{
						for (School_class_year scy: u.getUserRole().getClasses())
						{
							if (scy.getid() == targetID)
								allowed = true;
						}
					}
					//Parent can't edit these entities
					//The reason we put this check here and not rely on adminEditTeacherEditParentRead is because of person with dual role.
					// As a parent they may have access to different classes compared to as a teacher
					// TODO: CHECK WHETHER THIS PROBLEM CAN HAPPEN EVERYWHERE WE CALL adminEditTeacherEditParentRead()
					if (allowed && edit)
					{
						allowed = false;
					}
					logger.debug("Got parent");
				}
				if (isTeacher)
				{
					for (School_class_year scy: role.getClasses())
					{
						if (scy.getid() == targetID)
							allowed = true;
					}
				}
				
				if (!allowed)
					return false;
								
				return adminEditTeacherEditParentRead(role, edit);
				
			//These are at student level but for teacher we can check at class too
			//case ATTENDANCE: 	
			// the below logic is only for parent level
			case PARENT_TEACHER_MEETING1:
			
				
				boolean t = verifyStudentEntityAccess(te, role, targetID, teOwner, targetOwnerID);
				
				if (!t)
					return false;
				
				return adminEditTeacherEditParentRead(role, edit);
				
			//Everyone has update access here
			case CLASS_DIARY_UPDATES1: 		
			case TEMPLATE:
		 
				return verifyStudentEntityAccess(te, role, targetID, teOwner, targetOwnerID);					
				
		}
		
		return false;
	}
	
	
	public static boolean verifyStudentEntityAccess(Constants.TargetEntity te, AppRole role, Long targetID, 
												Constants.TargetEntity teOwner, Long targetOwnerID)
	{
		boolean isParent = role.getParent();
		boolean isTeacher = role.getTeacher();
		boolean allowed = false;
		
		//For parent, the student ids will be within the User object of the student within students object
		if (te == Constants.TargetEntity.STUDENT)
		{
			//if (isParent && !role.getStudentIDs().contains(targetID))
			if (isParent)
			{
				for (User u: role.getStudents())
				{
					if (u.getId() == targetID)
						allowed = true;
				}
			}
			if (!allowed && isTeacher)
			{
				if (teOwner == Constants.TargetEntity.CLASS)
				{
					for (School_class_year scy: role.getClasses())
					{
						if (scy.getid() == targetOwnerID)
							allowed = true;
					}
				}
			}
		}
		else if (te == Constants.TargetEntity.CLASS)
		{
			//Parent will always have a student in a particular class 
			// and hence parent with target entity of class should not occur
			if (isTeacher)
			{
				for (School_class_year scy: role.getClasses())
				{
					if (scy.getid() == targetOwnerID)
						allowed = true;
				}
			}
		}
		return allowed; 
	}
	
	public static boolean adminEditTeacherReadParentBlock(AppRole role, boolean edit)
	{ 
		if (role.getAdmin())
			return true;
		
		if (edit)
			return false;
		
		if (role.getTeacher())
			return true;
		else 
			return false;
		
	}
	
	public static boolean adminEditTeacherReadParentRead(AppRole role, boolean edit)
	{
		if (role.getAdmin())
			return true;
		
		if (edit)
			return false;
		else
			return true;
		
	}
	
	public static boolean adminEditTeacherEditParentRead(AppRole role, boolean edit)
	{
		if (role.getAdmin())
			return true;
		
		if (edit && !role.getTeacher())
			return false;
		else
			return true;
		
	}
	
}
