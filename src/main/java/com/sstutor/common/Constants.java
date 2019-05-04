package com.sstutor.common;


public class Constants {
	public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
	//public static final String DB_URL = "jdbc:postgresql://ec2-13-126-109-46.ap-south-1.compute.amazonaws.com:5432/postgres1";
	public static final String DB_UID = "postgres";
	public static final String DB_PD = "acctel123";
	
	public static enum TargetDomain
	{
		//BELOW ONLY ADMIN HAS EDIT

		CLASS, 					//READ - TEACHER
		SUBJECT, 				//TEACHER, STUDENT, PARENT; this is school level subject
		SCHOOL,					//READ - TEACHER
		REFERENCE_TIMETABLE1, 	//TEACHER
		TEACHER, 				
		PARENT_CHILD,			//TEACHER
		NOTIFICATION,			//A_LL
		
		//BELOW TEACHER ALSO HAS EDIT ACCESS IN ADDITION TO ADMIN
		//Parent & student read access is only at particular id level for few cases
		
		ATTENDANCE, 				//Only for student id they have access to
		SUBJECT_CLASS, 				//For the class they have access to
		EXAM, 						//Class level
		DAILYDIARY, 				//Class level
		TIMETABLE, 	 				//Class level
		PARENT_TEACHER_MEETING,		//Student level
		CLASS_DIARY_UPDATES, 		//Student level; update allowed
		QUIZ, 						//Class level
		STUDENT_LIST,				//Classlevel
		PARENT_TEACHER_MEETING1,		//temporary variable
		CLASS_DIARY_UPDATES1, 		//Student level; update allowed
		REFERENCE_TIMETABLE, 	//TEACHER
		TEMPLATE
	}
	
	public static enum TargetEntity
	{
		STUDENT, TEACHER, PARENT, CLASS, SCHOOL
	}
}
