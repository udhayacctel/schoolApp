package com.sstutor.school;

import java.io.File;
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

import java.sql.Timestamp;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.sstutor.common.SecurityUtilities;
import com.sstutor.common.DBConnection;
import com.sstutor.common.Utilities;
import com.sstutor.common.Constants.TargetDomain;
import com.sstutor.common.Constants.TargetEntity;
import com.sstutor.exception.ExceptionDetail;
import com.sstutor.exception.GenericException;
import com.sstutor.model.Attendance;
import com.sstutor.model.Notification;
import com.sstutor.model.Subject;
import com.sstutor.model.Dailydiary;
import com.sstutor.model.ParentTeacherMeeting;
import com.sstutor.model.parent_child;
import com.sstutor.school.ClassController;
import com.sstutor.model.Teacher_class;
import com.sstutor.model.Subject_class;
import com.sstutor.model.WorkUpdates;
import com.sstutor.model.Master;
import com.sstutor.model.School_class_year;
import com.sstutor.model.School;
import com.sstutor.model.TimeTable;
import com.sstutor.model.Exam;
import com.sstutor.model.Exam_timetable;
import com.sstutor.model.ClassReferenceTime;
import com.sstutor.model.Quiz;
import com.sstutor.model.Student;
import com.sstutor.model.User_record;
import com.sstutor.model.Teacher;
import com.sstutor.model.SchoolImage;
import com.sstutor.model.School_constant;
import com.sstutor.model.Template;
import com.sstutor.model.Bus;
import com.sstutor.model.Payroll;
import com.sstutor.model.Events;
import com.sstutor.model.Quiz_structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/*public class ClassController {

	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/acad_year/{acad_year}/classes", method = RequestMethod.GET)
	public ResponseEntity<List<School_class_year>> getschool_class_year(@PathVariable Long school_id, @PathVariable String acad_year) {
		
		ArrayList<School_class_year> subjects = new ArrayList<School_class_year>();
		Connection c = null; 
		ResultSet res;

		try {
			c = DBConnection.getDBConnection();

			String sql = "select s.id, school_id, standard, section, acad_year, subject, c.id as subject_id "
					   + "from school_class_year as s left outer join class_subject as c on class_id=s.id "
					   + "where school_id=? and acad_year=? order by standard, section";
	
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, acad_year);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			School_class_year s = new School_class_year();
			s.setid(res.getLong("id"));
			s.setstandard(res.getString("standard"));
			s.setschool_id(res.getLong("school_id"));
			s.setsection(res.getString("section"));
			s.setacad_year(res.getString("acad_year"));
			s.setsubject(res.getString("subject"));
			s.setsubject_id(res.getLong("subject_id"));

			subjects.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<School_class_year>>(subjects, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}*/

public class ClassController {

	
	private static final Logger logger = LoggerFactory.getLogger(ClassController.class);
	private String student_class_id;
	
	//HELLO WRONG - We are getting subjects here???
	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/acad_year/{acad_year}/classes", method = RequestMethod.GET)
	public ResponseEntity<List<School_class_year>> getschool_class_year(@PathVariable Long school_id,
											@PathVariable String acad_year, Authentication auth) {
		
		ArrayList<School_class_year> subjects = new ArrayList<School_class_year>();
		Connection c = null;
		ResultSet res;

		System.out.println("Coming to Class Details Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Class Details for class");
		}
		
		try {
			c = DBConnection.getDBConnection();

			String sql = "select scy.id, standard, section, subject, cs.id as subject_id "
					   + "from school_class_year as scy left outer join class_subject as cs on class_id=scy.id where school_id=? and acad_year=? order by standard,section";
	
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, acad_year);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			School_class_year s = new School_class_year();
			s.setid(res.getLong("id"));
			s.setstandard(res.getString("standard"));
			s.setsection(res.getString("section"));
			s.setsubject(res.getString("subject"));
			s.setsubject_id(res.getLong("subject_id"));

			subjects.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<School_class_year>>(subjects, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/classes", method = RequestMethod.POST)
	public String addstandard(@PathVariable Long school_id, @RequestBody School_class_year std, Authentication auth) {

		/*System.out.println("Coming to Class Details Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem Posting Class Details for class");
		}*/

		try {
			if (std == null) {
				throw new Exception("Request body missing");
			}

			String op = "{ }";
			String standard = std.getstandard();
			String section = std.getsection();
			String acad_year = std.getacad_year();

			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into school_class_year(school_id, standard, section, acad_year) values (?, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, standard);
			pstmt.setString(3, section);
			pstmt.setString(4, acad_year);

			pstmt.execute();
			pstmt.close();
			c.close();
			return op;

		} 
		catch (Exception e) {
		throw new GenericException("Add standard failed:" + e.getMessage());
		
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/{id}/classes", method = RequestMethod.DELETE)
	public String deletestandard(@PathVariable Long school_id, @PathVariable Long id, Authentication auth) {
		
		String op = "{ }";
		Connection c = null;
		
		/*System.out.println("Coming to Class Details Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE, TargetDomain.CLASS, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem deleting Class Details for class");
		}*/


		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from school_class_year  where school_id=? and id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setLong(2, id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Remove standard failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/school/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<School>> getSchool(@PathVariable Long id, Authentication auth) {

		ArrayList<School> school = new ArrayList<School>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to school Code");
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.CLASS, TargetEntity.SCHOOL, id, null, null))
		{
		throw new GenericException("Authorization problem getting school for class");
		}

		try {
			c = DBConnection.getDBConnection();

			String sql = "select id, name,branch,city from school where id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			res = pstmt.executeQuery();

		while (res.next()) {
			School s = new School();
			s.setid(res.getLong("id"));
			s.setname(res.getString("name"));
			s.setbranch(res.getString("branch"));
			s.setcity(res.getString("city"));
			
			school.add(s);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<School>>(school, HttpStatus.OK);

			}
		
		catch (Exception e) {
		throw new GenericException("Getting school failed:" + e.getMessage());
		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/branch", method = RequestMethod.POST)
	public String school(@PathVariable Long school_id, @RequestBody School role, Authentication auth) {

		System.out.println("Coming to school Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.CLASS, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem Posting school for class");
		}

		try {
			if (role == null) {
				throw new Exception("Request body missing");
			}

			String op = "{ }";
			String name = role.getname();
			String branch = role.getbranch();
			String city = role.getcity();

			Connection c = null;

			c = DBConnection.getDBConnection();

			String sql = "insert into  school(name, branch, city) values (?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, branch);
			pstmt.setString(3, city);

			pstmt.execute();
			pstmt.close();
			c.close();
			return op;

		} 
		catch (Exception e) {
		throw new GenericException("Add school failed:" + e.getMessage());
		
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/subjects/{class_id}/classes", method = RequestMethod.GET)
	public ResponseEntity<List<Subject_class>> getSubject(@PathVariable Long class_id, Authentication auth) {

		ArrayList<Subject_class> subject_class = new ArrayList<Subject_class>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to subject Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SUBJECT_CLASS, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting subject for class");
		}

		try {
			c = DBConnection.getDBConnection();

			String sql = "select id, class_id, subject from class_subject where class_id = ?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			Subject_class s = new Subject_class();
			s.setid(res.getLong("id"));
			s.setclass_id(res.getLong("class_id"));
			s.setsubject(res.getString("subject"));
			
			 subject_class .add(s);
			}

			pstmt.close();
			c.close();
	
			return new ResponseEntity<List<Subject_class>>( subject_class , HttpStatus.OK);

			}
		
		catch (Exception e) {
		throw new GenericException("Getting subjects failed:" + e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/subject/class/{class_id}", method = RequestMethod.PUT)
	public String addsubject(@PathVariable ("class_id") Long cls_id, @RequestBody Subject[] p_subject, Authentication auth) {
			String op = "{ }";
			Connection c = null;
		
			/* cls is nothing but class_id */
			
			System.out.println("Coming to subject Code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.SUBJECT_CLASS, TargetEntity.CLASS, cls_id, null, null))
			{
			throw new GenericException("Authorization problem Posting subject for class");
			}

		try {
			Long[] class_id = new Long[p_subject.length];

			for (int i = 0; i < p_subject.length; i++) {
				class_id[i] = p_subject[i].getclass_id();
				 }

			c = DBConnection.getDBConnection();

			String sql = "select upsert_subject (?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setString(2, p_subject[0].getsubject());			

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing attendance for period failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/subject/{class_id}/delete", method = RequestMethod.PUT)
	public String deletesubject(@PathVariable ("class_id") Long cls_id,@RequestBody Subject[] p_subject, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			
			System.out.println("Coming to subject Code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.SUBJECT_CLASS, TargetEntity.CLASS, cls_id, null, null))
			{
			throw new GenericException("Authorization problem deleting subject for class");
			}

		try {
			Long[] class_id = new Long[p_subject.length];

			for (int i = 0; i < p_subject.length; i++) {
				class_id[i] = p_subject[i].getclass_id();
				System.out.println("Revathy" + class_id[i]);
		   }
			
			c = DBConnection.getDBConnection();

			String sql = "select upsert_delete_subject(?,?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setString(2, p_subject[0].getsubject());			

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete timetable failed:" + e.getMessage());
		}
		return op;

	}
	
	@CrossOrigin()
	@RequestMapping(value = "/exam/{school_id}", method = RequestMethod.GET)
	public ResponseEntity<List<Exam>> getexam(@PathVariable Long school_id, Authentication auth) {
		
		ArrayList<Exam> et = new ArrayList<Exam>();
		Connection c = null;
		ResultSet rst;
		
		System.out.println("Coming to Exam Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting exam for class");
		}

		try {
			c = DBConnection.getDBConnection();
			
			String sql = "select school_id, name, id from exam where school_id=? ";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, school_id);
			
			rst = pstm.executeQuery();
		while (rst.next()) {
			
			Exam et1 = new Exam();
			et1.setschool_id(rst.getLong("school_id"));
			et1.setname(rst.getString("name"));
			et1.setid(rst.getLong("id"));

			et.add(et1);
			}
			pstm.close();
			c.close();
		
			return new ResponseEntity<List<Exam>>(et, HttpStatus.OK);
		}
		
		catch (Exception e) {
		throw new GenericException("getting exam  details failed:"	+ e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/exam/{school_id}", method = RequestMethod.POST)
	public String addexam(@PathVariable Long school_id,@RequestBody Exam et, Authentication auth) {
		
	/*	System.out.println("Coming to Exam Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem Postiing exam for class");
		}*/

		try {
			if (et == null) {
				throw new Exception("req exam_timetable missing");
			}
			String op = "{ }";
			String name = et.getname();

			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into exam (school_id, name) values(?,?)";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, school_id);
			pstm.setString(2, name);

			pstm.execute();
			pstm.close();
			c.close();
		
			return op;
		}
		catch (Exception e) {
		throw new GenericException(" Insert exam failed:"	+ e.getMessage());

		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/exam/{school_id}/{id}/delete", method = RequestMethod.DELETE)
	public String removeExam(@PathVariable Long school_id, @PathVariable Long id, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			
			/*System.out.println("Coming to Exam Code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
			{
			throw new GenericException("Authorization problem deleting exam for class");
			}*/

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from exam where id = ?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete dailydiary failed:" + e.getMessage());
		}
		return op;
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/dailydiary/{student_id}/{class_id}/{activity}/{end_date}", method = RequestMethod.GET)
	public ResponseEntity<List<Dailydiary>> getdailydairyforparent( @PathVariable Long student_id, @PathVariable Long class_id,  @PathVariable String activity,
														  @PathVariable String end_date, Authentication auth) {

		ArrayList<Dailydiary> dailydairy = new ArrayList<Dailydiary>();
		Connection c = null;
		ResultSet res;

		System.out.println("Coming to Daily Diary WorkStatus Code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.DAILYDIARY, TargetEntity.STUDENT, student_id, TargetEntity.CLASS, class_id))
		{
		throw new GenericException("Authorization problem getting daily diary WorkStatus for class");
		}

		
		try {
			c = DBConnection.getDBConnection();

			String sql = "select  end_date, title, message, activity, c.id, c.class_id, subject_id, s.school_id, s.standard,"
					+ "s.section,s.acad_year,cs.subject, created_by, modified_by, created_by_date, modified_by_date, cd.status, cd.student_id from class_diary as c "
					+ "inner join school_class_year as s on c.class_id=s.id inner join class_subject as cs on c.subject_id=cs.id "
					+ "left outer join class_diary_updates as cd on cd.class_diary_id=c.id and cd.student_id=? where c.class_id=? and activity=? and end_date>=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, student_id);
			pstmt.setLong(2, class_id);
			pstmt.setString(3, activity);
			pstmt.setString(4, end_date);

			res = pstmt.executeQuery();

		while (res.next()) {

			Dailydiary s = new Dailydiary();
			s.setid(res.getLong("id"));
			s.setclass_id(res.getLong("class_id"));
		    s.setend_date(res.getString("end_date"));
			s.settitle(res.getString("title"));
			s.setmessage(res.getString("message"));
			s.setactivity(res.getString("activity"));
			s.setsubject_id(res.getLong("subject_id"));
			s.setschool_id(res.getLong("school_id"));
			s.setstandard(res.getString("standard"));
			s.setsection(res.getString("section"));
			s.setactivity(res.getString("activity"));
			s.setacad_year(res.getString("acad_year"));
			s.setsubject(res.getString("subject"));
			s.setcreated_by(res.getLong("created_by"));
			s.setmodified_by(res.getLong("modified_by"));
			s.setcreated_by_date(res.getString("created_by_date"));
			s.setmodified_by_date(res.getString("modified_by_date"));
			s.setstatus(res.getString("status"));
			s.setstudent_id(res.getLong("student_id"));

			dailydairy.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Dailydiary>>(dailydairy, HttpStatus.OK);

			}
		
		catch (Exception e) {
		throw new GenericException("Getting dailydiary failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/dailydiary/{class_id}/{activity}/{end_date}", method = RequestMethod.GET)
	public ResponseEntity<List<Dailydiary>> getdailydairy(@PathVariable Long class_id,  @PathVariable String activity,
														  @PathVariable String end_date,
														  Authentication auth) {

														  
		ArrayList<Dailydiary> dailydairy = new ArrayList<Dailydiary>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to Daily Diary code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.DAILYDIARY, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting daily diary for class");
		}

		try {
			c = DBConnection.getDBConnection();

			String sql = "select  end_date, title, message, activity, c.id, c.class_id, subject_id, s.school_id, s.standard,"
					+ "s.section,s.acad_year,cs.subject, created_by, modified_by, created_by_date, modified_by_date from class_diary as c "
					+ "inner join school_class_year as s on c.class_id=s.id inner join class_subject as cs on c.subject_id=cs.id "
					+ "where c.class_id=? and end_date>=? and activity=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
	//		pstmt.setString(2, activity);
	//		pstmt.setString(3, activity);
		
			pstmt.setLong(1, class_id);
			pstmt.setString(2, end_date);
			pstmt.setString(3, activity);
			res = pstmt.executeQuery();

		while (res.next()) {

			Dailydiary s = new Dailydiary();
			s.setid(res.getLong("id"));
			s.setclass_id(res.getLong("class_id"));
		    s.setend_date(res.getString("end_date"));
			s.settitle(res.getString("title"));
			s.setmessage(res.getString("message"));
			s.setactivity(res.getString("activity"));
			s.setsubject_id(res.getLong("subject_id"));
			s.setschool_id(res.getLong("school_id"));
			s.setstandard(res.getString("standard"));
			s.setsection(res.getString("section"));
			s.setactivity(res.getString("activity"));
			s.setacad_year(res.getString("acad_year"));
			s.setsubject(res.getString("subject"));
			s.setcreated_by(res.getLong("created_by"));
			s.setmodified_by(res.getLong("modified_by"));
			s.setcreated_by_date(res.getString("created_by_date"));
			s.setmodified_by_date(res.getString("modified_by_date"));
			String x = (res.getString("message"));
			System.out.println("Now the output is redirected!" + x );
			dailydairy.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Dailydiary>>(dailydairy, HttpStatus.OK);

			}
		
		catch (Exception e) {
		throw new GenericException("Getting dailydiary failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/dailydiary/{class_id}", method = RequestMethod.POST)
	public String addclassdiary(@PathVariable Long class_id, @RequestBody Dailydiary diary,
																Authentication auth) {

		System.out.println("Coming to Daily Diary Post code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.DAILYDIARY, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem posting daily diary for class");
		}

		try {
		if (diary == null) {
		throw new Exception("req. body missing");
			}

			String op = "{ }";
			String end_date = diary.getend_date();
			String title = diary.gettitle();
			String message = diary.getmessage();
			Long subject_id = diary.getsubject_id();
			String activity = diary.getactivity();
			Long created_by = diary.getcreated_by();
			//String created_by_date = diary.getcreated_by_date();

			Connection c = null;
			c = DBConnection.getDBConnection();
			
			String sql = "insert into class_diary(end_date, title, message, activity, class_id, subject_id, created_by, created_by_date) values (?, ?, ?, ?, ?, ?, ?, current_timestamp)";

			PreparedStatement pstmt = c.prepareStatement(sql);

			pstmt.setString(1, end_date);
			pstmt.setString(2, title);
			pstmt.setString(3, message);
			pstmt.setString(4, activity);
			pstmt.setLong(5, class_id);
			pstmt.setLong(6, subject_id);
			pstmt.setLong(7, created_by);
			//pstmt.setString(8, created_by_date);
		
			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Adding dailydiary failed:"	+ e.getMessage());
		}
	}

		
	@CrossOrigin()
	@RequestMapping(value = "/dailydiary/{class_id}/class/{id}", method = RequestMethod.PUT)
	public String updatedailydiary(@PathVariable Long class_id, @PathVariable Long id, @RequestBody Dailydiary diary, Authentication auth) {

		String op = "{ }";
		Connection c = null;

		System.out.println("Coming to Daily Diary Put code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.DAILYDIARY, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem Editing daily diary for class");
		}

		try {

			c = DBConnection.getDBConnection();

			String sql = "update class_diary set  end_date = ?, title = ?, message = ?, subject_id = ?,"
					+ " activity= ?,  class_id =?, modified_by =?, modified_by_date= current_timestamp"
					+ " where id = ?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, diary.getend_date());
			pstmt.setString(2, diary.gettitle());
			pstmt.setString(3, diary.getmessage());
			pstmt.setLong(4, diary.getsubject_id());
			pstmt.setString(5, diary.getactivity());
			pstmt.setLong(6,class_id);
			pstmt.setLong(7, diary.getmodified_by());
			//pstmt.setString(8, diary.getmodified_by_date());
			pstmt.setLong(8, id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update dailydiary for class failed:"+ e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/dailydiary/{class_id}/class/{id}", method = RequestMethod.DELETE)
	public String removeDiary(@PathVariable Long class_id, @PathVariable Long id, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			
			System.out.println("Coming to Daily Diary Delete code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE,  TargetDomain.DAILYDIARY, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem deleting daily diary for class");
			}

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from class_diary where id = ?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete dailydiary failed:" + e.getMessage());
		}
		return op;

	}
	

	@CrossOrigin()	
	@RequestMapping(value = "/teacher_schedule/{teacher_id}/{day}/{date}/{school_id}", method = RequestMethod.GET)
	public ResponseEntity<List<TimeTable>> getTeacherTimeTable( @PathVariable Long teacher_id, @PathVariable String day,
											 @PathVariable String date,@PathVariable Long school_id, Authentication auth) {
		ArrayList<TimeTable> tts = new ArrayList<TimeTable>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to Student list code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Student List for class");
		}
		
		try {

			c = DBConnection.getDBConnection();

			/*String sql = "select  date, c.id, start_time, end_time, period, attendance, day, active, c.class_id, "
					+ "teacher_id, subject_id,created_by, modified_by, created_by_date, modified_by_date, s.school_id,"
					+ " s.standard,s.section,s.acad_year,cs.subject from class_time_table as c "
					+ "inner join school_class_year as s on c.class_id=s.id "
					+ "inner join class_subject as cs on c.subject_id=cs.id "
					+ "where teacher_id =?  and (day=? or date=?) and active='True' order by period, date asc";*/
			
			String sql ="select  date, c.id, start_time, end_time, period, attendance, day, active, c.class_id, teacher_id, "
					+ "subject_id,created_by, modified_by, created_by_date, modified_by_date, s.school_id, s.standard, "
					+ "s.section,s.acad_year,cs.subject from class_time_table as c inner join school_class_year as s "
					+ "on c.class_id=s.id inner join class_subject as cs on c.subject_id=cs.id where Case "
					+ "When (Select Exists(select 1 from class_time_table where date = ?)) "
					+ "Then teacher_id =? and date=? and active=true else  teacher_id =? and day =? and active=true END order by period, date asc";

			PreparedStatement pstmt = c.prepareStatement(sql);
		
			pstmt.setString(1, date);
			pstmt.setLong(2, teacher_id);
			pstmt.setString(3, date);
			pstmt.setLong(4, teacher_id);
			pstmt.setString(5, day);

			res = pstmt.executeQuery();
		while (res.next()) {

			    TimeTable t = new TimeTable();
				t.setattendance(res.getBoolean("attendance"));
				t.setend_time(res.getString("end_time"));
				t.setid(res.getLong("id"));
				t.setperiod(res.getString("period"));
				t.setstart_time(res.getString("start_time"));
				t.setsubject_id(res.getLong("subject_id"));
				t.setteacher_id(res.getLong("teacher_id"));
				t.setday(res.getString("day"));
				t.setstandard(res.getString("standard"));
				t.setsection(res.getString("section"));
				t.setacad_year(res.getString("acad_year"));
				t.setsubject(res.getString("subject"));
				t.setdate(res.getString("date"));
				t.setschool_id(res.getLong("school_id"));
				t.setactive(res.getBoolean("active"));
				t.setclass_id(res.getLong("class_id"));
				t.setcreated_by(res.getLong("created_by"));
				t.setmodified_by(res.getLong("modified_by"));
				t.setcreated_by_date(res.getString("created_by_date"));
				t.setmodified_by_date(res.getString("modified_by_date"));

				tts.add(t);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<TimeTable>>(tts, HttpStatus.OK);

		} 
		catch (Exception e) {
		throw new GenericException("Get time table for class failed:" + e.getMessage());
		}
	}

	@CrossOrigin()	
	@RequestMapping(value = "/timetable/{class_id}/{day}/{date}", method = RequestMethod.GET)
	public ResponseEntity<List<TimeTable>> getTimeTable(@PathVariable Long class_id, @PathVariable String day,
														@PathVariable String date, Authentication auth) {

		ArrayList<TimeTable> tts = new ArrayList<TimeTable>();
		Connection c = null;
		ResultSet res;

		System.out.println("Coming to TimeTable code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting Timetable for class");
		}

		try {

			c = DBConnection.getDBConnection();

			/*String sql = "select c.class_id, attendance, end_time, period, start_time, subject_id, teacher_id, day, date,"
					+ "created_by, modified_by, created_by_date,  modified_by_date, c.id, s.standard, s.section, s.acad_year, "
					+ "s.school_id,cs.subject, name from class_time_table as c "
					+ "inner join school_class_year as s on c.class_id=s.id "
					+ "inner join user_record on teacher_id=tg_id "
					+ "inner join class_subject as cs on c.subject_id=cs.id where c.class_id =?  and (day=? or date=?) and active='True' order by period, date asc";
			*/		
			String sql ="select c.class_id, attendance, end_time, period, start_time, subject_id, teacher_id, day, date, active, "
					+ "created_by, modified_by, created_by_date,  modified_by_date, c.id, s.standard, s.section, s.acad_year, "
					+ "s.school_id,cs.subject, name from class_time_table as c "
					+ "inner join school_class_year as s on c.class_id=s.id "
					+ "inner join user_record on teacher_id=tg_id "
					+ "inner join class_subject as cs on c.subject_id=cs.id where "
					+ "Case When (Select Exists(select 1 from class_time_table where date =?)) Then c.class_id =? and date=?"
					+ " and active=true else c.class_id =? and day =? and active=true END order by period, date asc";
					
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setLong(2, class_id);
			pstmt.setString(3, date);
			pstmt.setLong(4, class_id);
			pstmt.setString(5, day);

			res = pstmt.executeQuery();
		while (res.next()) {
				
			TimeTable t = new TimeTable();
				t.setclass_id(res.getLong("class_id"));
				t.setattendance(res.getBoolean("attendance"));
				t.setend_time(res.getString("end_time"));
				t.setid(res.getLong("id"));
				t.setperiod(res.getString("period"));
				t.setstart_time(res.getString("start_time"));
				t.setsubject_id(res.getLong("subject_id"));
				t.setteacher_id(res.getLong("teacher_id"));
				t.setday(res.getString("day"));
				t.setdate(res.getString("date"));
				t.setstandard(res.getString("standard"));
				t.setsection(res.getString("section"));
				t.setacad_year(res.getString("acad_year"));
				t.setschool_id(res.getLong("school_id"));
				t.setsubject(res.getString("subject"));
				t.setcreated_by(res.getLong("created_by"));
				t.setmodified_by(res.getLong("modified_by"));
				t.setcreated_by_date(res.getString("created_by_date"));
				t.setmodified_by_date(res.getString("modified_by_date"));
				t.setname(res.getString("name"));

				tts.add(t);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<TimeTable>>(tts, HttpStatus.OK);

		} 
		catch (Exception e) {
		throw new GenericException("Get time table for class failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()	
	@RequestMapping(value = "/timetable/{class_id}/{day}/day", method = RequestMethod.GET)
	public ResponseEntity<List<TimeTable>> getTimeTableforday(@PathVariable Long class_id,
																@PathVariable String day, Authentication auth) {

		ArrayList<TimeTable> tts = new ArrayList<TimeTable>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to TimeTable code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting Timetable for class");
		}

		
		try {

			c = DBConnection.getDBConnection();

			String sql = "select c.class_id, attendance, end_time, period, start_time, subject_id, teacher_id, day,"
					+ "created_by, modified_by, created_by_date,  modified_by_date, c.id, s.standard, s.section, s.acad_year, "
					+ "s.school_id,cs.subject, name from class_time_table as c "
					+ "inner join school_class_year as s on c.class_id=s.id "
					+ "inner join user_record on teacher_id=tg_id "
					+ "inner join class_subject as cs on c.subject_id=cs.id where c.class_id =?  and day=? and date is null and active='True' order by period, date asc";


			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);
			pstmt.setString(2, day);

			res = pstmt.executeQuery();
		while (res.next()) {
				
			TimeTable t = new TimeTable();
				t.setclass_id(res.getLong("class_id"));
				t.setattendance(res.getBoolean("attendance"));
				t.setend_time(res.getString("end_time"));
				t.setid(res.getLong("id"));
				t.setperiod(res.getString("period"));
				t.setstart_time(res.getString("start_time"));
				t.setsubject_id(res.getLong("subject_id"));
				t.setteacher_id(res.getLong("teacher_id"));
				t.setday(res.getString("day"));
				//t.setday(res.getString("date"));
				t.setstandard(res.getString("standard"));
				t.setsection(res.getString("section"));
				t.setacad_year(res.getString("acad_year"));
				t.setschool_id(res.getLong("school_id"));
				t.setsubject(res.getString("subject"));
				t.setcreated_by(res.getLong("created_by"));
				t.setmodified_by(res.getLong("modified_by"));
				t.setcreated_by_date(res.getString("created_by_date"));
				t.setmodified_by_date(res.getString("modified_by_date"));
				t.setname(res.getString("name"));

				tts.add(t);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<TimeTable>>(tts, HttpStatus.OK);

		} 
		catch (Exception e) {
		throw new GenericException("Get time table for class failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()	
	@RequestMapping(value = "/timetable/{class_id}/{date}/date", method = RequestMethod.GET)
	public ResponseEntity<List<TimeTable>> getTimeTablefordate(@PathVariable Long class_id,	
																@PathVariable String date, Authentication auth) {

		ArrayList<TimeTable> tts = new ArrayList<TimeTable>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to TimeTable date code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting Timetable date for class");
		}

		
		try {

			c = DBConnection.getDBConnection();

			String sql = "select c.class_id, attendance, end_time, period, start_time, subject_id, teacher_id, date,"
					+ "created_by, modified_by, created_by_date,  modified_by_date, c.id, s.standard, s.section, s.acad_year, "
					+ "s.school_id,cs.subject, name from class_time_table as c "
					+ "inner join school_class_year as s on c.class_id=s.id "
					+ "inner join user_record on teacher_id=tg_id "
					+ "inner join class_subject as cs on c.subject_id=cs.id where c.class_id =?  and date=? and active='True' order by period, date asc";


			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);
			pstmt.setString(2, date);

			res = pstmt.executeQuery();
		while (res.next()) {
				
			TimeTable t = new TimeTable();
				t.setclass_id(res.getLong("class_id"));
				t.setattendance(res.getBoolean("attendance"));
				t.setend_time(res.getString("end_time"));
				t.setid(res.getLong("id"));
				t.setperiod(res.getString("period"));
				t.setstart_time(res.getString("start_time"));
				t.setsubject_id(res.getLong("subject_id"));
				t.setteacher_id(res.getLong("teacher_id"));
				//t.setday(res.getString("day"));
				t.setday(res.getString("date"));
				t.setstandard(res.getString("standard"));
				t.setsection(res.getString("section"));
				t.setacad_year(res.getString("acad_year"));
				t.setschool_id(res.getLong("school_id"));
				t.setsubject(res.getString("subject"));
				t.setcreated_by(res.getLong("created_by"));
				t.setmodified_by(res.getLong("modified_by"));
				t.setcreated_by_date(res.getString("created_by_date"));
				t.setmodified_by_date(res.getString("modified_by_date"));
				t.setname(res.getString("name"));

				tts.add(t);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<TimeTable>>(tts, HttpStatus.OK);

		} 
		catch (Exception e) {
		throw new GenericException("Get time table for class failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/timetable/{class_id}/{day}/day", method = RequestMethod.POST)
	public String timetable(@PathVariable Long class_id, @PathVariable String day, @RequestBody TimeTable pv,
																	Authentication auth) {
		
		System.out.println("Coming to TimeTable Post code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem Posting day Timetable for class");
		}

		try {
			if (pv == null) {
				throw new Exception("req. body missing");
			}
			String op = "{ }";
			
			String start_time = pv.getstart_time();
			String end_time = pv.getend_time();
			String period = pv.getperiod();
			Boolean attendance = pv.getattendance();
			Long subject_id = pv.getsubject_id();
			Long teacher_id = pv.getteacher_id();
			Long created_by = pv.getcreated_by();
			//String created_by_date = pv.getcreated_by_date();

			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into class_time_table(start_time, end_time, period, attendance, day, class_id, teacher_id, subject_id, created_by, created_by_date, active) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, 'True')";

			PreparedStatement pstmt = c.prepareStatement(sql);		

			pstmt.setString(1, start_time);
			pstmt.setString(2, end_time);
			pstmt.setString(3, period);
			pstmt.setBoolean(4, attendance);
			pstmt.setString(5, day);
			pstmt.setLong(6, class_id);
			pstmt.setLong(7, teacher_id);
			pstmt.setLong(8, subject_id);
			pstmt.setLong(9, created_by);
			//pstmt.setString(10, created_by_date);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;
			
		}
		catch (Exception e) {
		throw new GenericException("Adding timetable failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/timetable/{class_id}/{day}/{date}/date", method = RequestMethod.POST)
	public String timetablefordate(@PathVariable Long class_id, @PathVariable String day, 
						@PathVariable String date, @RequestBody TimeTable pv, Authentication auth) {
				
		System.out.println("Coming to TimeTable code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem date Timetable for class");
		}

			try {
				if (pv == null) {
					throw new Exception("req. body missing");
			}
			String op = "{ }";
			
			String start_time = pv.getstart_time();
			String end_time = pv.getend_time();
			String period = pv.getperiod();
			Boolean attendance = pv.getattendance();
			Long subject_id = pv.getsubject_id();
			Long teacher_id = pv.getteacher_id();
			Long created_by = pv.getcreated_by();
			//String date = pv.getdate();

			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into class_time_table(start_time, end_time, period, attendance, day, class_id, teacher_id, "
					+ "subject_id, created_by,date, created_by_date, active) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?, current_timestamp, 'True')";

			PreparedStatement pstmt = c.prepareStatement(sql);		

			pstmt.setString(1, start_time);
			pstmt.setString(2, end_time);
			pstmt.setString(3, period);
			pstmt.setBoolean(4, attendance);
			pstmt.setString(5, day);
			pstmt.setLong(6, class_id);
			pstmt.setLong(7, teacher_id);
			pstmt.setLong(8, subject_id);
			pstmt.setLong(9, created_by);
			pstmt.setString(10, date);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;
			
		}
		catch (Exception e) {
		throw new GenericException("Adding timetable failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/timetable/{class_id}/class/{id}", method = RequestMethod.PUT)
	public String updatetimetable(@PathVariable Long class_id, @PathVariable Long id, 
									@RequestBody TimeTable tt, Authentication auth) {
			String op = "{ }";
			Connection c = null;
			
			System.out.println("Coming to TimeTable Edit code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem Editing Timetable for class");
			}
			
		try {

			c = DBConnection.getDBConnection();

			String sql = "update class_time_table SET  active='False', modified_by=?, modified_by_date= current_timestamp, active_till_date=? where class_id=? and id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			
			pstmt.setLong(1, tt.getmodified_by());
			//pstmt.setString(2, tt.getmodified_by_date());
			pstmt.setString(2, tt.getactive_till_date());
			pstmt.setLong(3, class_id);
			pstmt.setLong(4, id);
			
			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update timetable for class failed:"+ e.getMessage());
		}
	}

/*
	@CrossOrigin()
	@RequestMapping(value = "/timetable/{class_id}/class/{id}/delete", method = RequestMethod.DELETE)
	public String deletetimetable(@PathVariable Long class_id, @PathVariable Long id, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			
			System.out.println("Coming to TimeTable Delete code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE,  TargetDomain.TIMETABLE, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem Deleting Timetable for class");
			}

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from class_time_table  where id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete timetable failed:" + e.getMessage());
		}
		return op;

	}*/
	
	@CrossOrigin()
	@RequestMapping(value = "/exam_timetable/{class_id}/{exam_id}", method = RequestMethod.GET)
	public ResponseEntity<List<Exam_timetable>> gettable(@PathVariable Long class_id, @PathVariable Long exam_id,
															Authentication auth) {
			
			ArrayList<Exam_timetable> et = new ArrayList<Exam_timetable>();
			Connection c = null;
			ResultSet rst;
			
			System.out.println("Coming to Exam TimeTable code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.EXAM, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Exam TimeTable for class");
			}
			
		try {
			c = DBConnection.getDBConnection();
			
			String sql = "select c.class_id, date, start_time, end_time, exam_id, name, subject_id, c.id, syllabus, created_by, modified_by, created_by_date, "
					+ " modified_by_date, s.school_id, s.standard,s.section,s.acad_year,cs.subject "
					+ "from exam_timetable as c inner join school_class_year as s on c.class_id=s.id "
					+ "inner join class_subject as cs on c.subject_id=cs.id inner join exam as e on c.exam_id=e.id where c.class_id=? and exam_id=? order by date ";

			PreparedStatement pstm = c.prepareStatement(sql);

			pstm.setLong(1, class_id);
			pstm.setLong(2, exam_id);

			rst = pstm.executeQuery();
		while (rst.next()) {
			
			Exam_timetable et1 = new Exam_timetable();
			et1.setclass_id(rst.getLong("class_id"));
			et1.setdate(rst.getString("date"));
			et1.setstart_time(rst.getString("start_time"));
			et1.setend_time(rst.getString("end_time"));
			et1.setexam_id(rst.getLong("exam_id"));
			et1.setsubject_id(rst.getLong("subject_id"));
			et1.setid(rst.getLong("id"));
			et1.setsyllabus(rst.getString("syllabus"));
			et1.setstandard(rst.getString("standard"));
			et1.setsection(rst.getString("section"));
			et1.setacad_year(rst.getString("acad_year"));
			et1.setschool_id(rst.getLong("school_id"));
			et1.setsubject(rst.getString("subject"));
			et1.setcreated_by(rst.getLong("created_by"));
			et1.setmodified_by(rst.getLong("modified_by"));
			et1.setcreated_by_date(rst.getString("created_by_date"));
			et1.setmodified_by_date(rst.getString("modified_by_date"));
			et1.setname(rst.getString("name"));

			et.add(et1);
			}
			pstm.close();
			c.close();
		return new ResponseEntity<List<Exam_timetable>>(et, HttpStatus.OK);
		}
		catch (Exception e) {
		throw new GenericException("getting exam timetable details failed:"	+ e.getMessage());
		}
	}

	
	/*@CrossOrigin()
	@RequestMapping(value = "/exam_timetable/class", method = RequestMethod.PUT)
	public String storeAttendanceForPeriod( @RequestBody Exam_timetable[] p_timetable) {

			String op = "{ }";
			Connection c = null;
		
		try {
			Long[] class_id = new Long[p_timetable.length];
			
			for (int i = 0; i < p_timetable.length; i++) {
				class_id[i] = p_timetable[i].getclass_id();
				System.out.println("class id is"+ class_id);

			}
				c = DBConnection.getDBConnection();

			String sql = "select from upsert_exam_timetable(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);

			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setLong(2, p_timetable[0].getsubject_id());
			pstmt.setLong(3,  p_timetable[0].getexam_id());
			pstmt.setString(4, p_timetable[0].getdate());
			pstmt.setString(5, p_timetable[0].getstart_time());
			pstmt.setString(6, p_timetable[0].getend_time());
			pstmt.setString(7, p_timetable[0].getsyllabus());
			pstmt.setLong(8, p_timetable[0].getcreated_by());
			pstmt.setLong(9, p_timetable[0].getmodified_by());
			pstmt.setString(10, p_timetable[0].getcreated_by_date());
			pstmt.setString(11, p_timetable[0].getmodified_by_date());
			Long clss = p_timetable[0].getclass_id();
			System.out.println("class id "+ clss);
			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing attendance for period failed:" + e.getMessage());
		}
	}*/
	
	@CrossOrigin()
	@RequestMapping(value = "/exam_timetable/{update_ind}/{update_subject_id}/class/{cls_id}", method = RequestMethod.PUT)
	public String storeAttendanceForPeriod( @PathVariable String update_ind, @PathVariable Long update_subject_id,@PathVariable Long cls_id,
											@RequestBody Exam_timetable[] p_timetable, Authentication auth) {
		/*cls_id is nothing but class_id*/
		
			String op = "{ }";
			Connection c = null;
		
			System.out.println("Coming to Daily Diary code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.EXAM, TargetEntity.CLASS, cls_id, null, null))
			{
			throw new GenericException("Authorization problem getting daily diary for class");
			}
			
		try {
			Long[] class_id = new Long[p_timetable.length];
			
			for (int i = 0; i < p_timetable.length; i++) {
				class_id[i] = p_timetable[i].getclass_id();
				System.out.println("class id is"+ class_id[i]);

			}
				c = DBConnection.getDBConnection();

			String sql = "select from exam_timetable(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);

			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setLong(2, p_timetable[0].getsubject_id());
			pstmt.setLong(3,  p_timetable[0].getexam_id());
			pstmt.setString(4, p_timetable[0].getdate());
			pstmt.setString(5, p_timetable[0].getstart_time());
			pstmt.setString(6, p_timetable[0].getend_time());
			pstmt.setString(7, p_timetable[0].getsyllabus());
			pstmt.setLong(8, p_timetable[0].getcreated_by());
			pstmt.setLong(9, p_timetable[0].getmodified_by());
			pstmt.setString(10, p_timetable[0].getcreated_by_date());
			pstmt.setString(11, p_timetable[0].getmodified_by_date());
			pstmt.setString(12, update_ind);
			pstmt.setLong(13, update_subject_id);

			System.out.println("update ;ind" + update_ind);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing TimeTable for Exam failed:" + e.getMessage());
		}
	}


	
	@CrossOrigin()
	@RequestMapping(value = "/exam/{exam_id}/subject/{subject_id}/class/{cls_id}", method = RequestMethod.PUT)
	public String deleteexamtimetable(@PathVariable Long exam_id, @PathVariable Long subject_id,@PathVariable Long cls_id, 
									@RequestBody Exam_timetable[] p_timetable, Authentication auth) {
				
		/* cls_id is nothing but class_id */
			String op = "{ }";
			Connection c = null;

			System.out.println("Coming to Exam Timetable code");
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.EXAM, TargetEntity.CLASS, cls_id, null, null))
			{
			throw new GenericException("Authorization problem getting Exam Timetable for class");
			}
			
		try {
			Long[] class_id = new Long[p_timetable.length];

			for (int i = 0; i < p_timetable.length; i++) {
				class_id[i] = p_timetable[i].getclass_id();
		   }
			
			c = DBConnection.getDBConnection();

			String sql = "select upsert_delete_examtimetable(?,?,?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setLong(2, subject_id);			
			pstmt.setLong(3, exam_id);
			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete timetable failed:" + e.getMessage());
		}
		return op;

	}

	
	@CrossOrigin()
	@RequestMapping(value = "/notifications/{school_id}/{to_date}/{recepient}", method = RequestMethod.GET)
	public ResponseEntity<List<Notification>> getNotification(@PathVariable Long school_id,  @PathVariable String to_date,
															  @PathVariable String recepient, Authentication auth) {
			
		ArrayList<Notification> nArr = new ArrayList<Notification>();
		Connection c = null;
		ResultSet res;
		System.out.println("Coming to Notification code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.NOTIFICATION, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Notification for class");
		}
		try {
			c = DBConnection.getDBConnection();

			String sql = "select school_id, id, from_date ,to_date, title, message, recepient, created_by, modified_by,"
					+ " created_by_date, modified_by_date from notifications where school_id = ? and to_date>= ? and recepient=? order by to_date desc limit 7";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, to_date);
			pstmt.setString(3, recepient);

			res = pstmt.executeQuery();

		while (res.next()) {
				Notification n = new Notification();
				n.setid(res.getLong("id"));
				n.setfrom_date(res.getString("from_date"));
				n.setto_date(res.getString("to_date"));
				n.settitle(res.getString("title"));
				n.setmessage(res.getString("message"));
				n.setrecepient(res.getString("recepient"));
				n.setschool_id(res.getLong("school_id"));
				n.setcreated_by(res.getLong("created_by"));
				n.setmodified_by(res.getLong("modified_by"));
				n.setcreated_by_date(res.getString("created_by_date"));
				n.setmodified_by_date(res.getString("modified_by_date"));

				nArr.add(n);
			}
			pstmt.close();
			res.close();
			c.close();

		return new ResponseEntity<List<Notification>>(nArr, HttpStatus.OK);
		}
		catch (Exception e) {
		throw new GenericException("Getting notifications for school failed:" + e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/notifications/{school_id}", method = RequestMethod.POST)
	public String addNotificationToSchool(@PathVariable Long school_id,  @RequestBody Notification notification, Authentication auth) {

		System.out.println("Coming to Notification Post code"   );
		
		// we have commented temporarly post notification
		/*if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.NOTIFICATION, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem posting Notification for class");
		}*/
		
		try {
		if (notification == null) {
		throw new Exception("req. notifications missing");
			}
		
			String op = "{ }";
			String from_date = notification.getfrom_date();
			String to_date = notification.getto_date();
			String title = notification.gettitle();
			String message = notification.getmessage();
			Long created_by = notification.getcreated_by();
			//String created_by_date = notification.getcreated_by_date();
			String recepient = notification.getrecepient();

			Connection c = null;
			c = DBConnection.getDBConnection();
			
			String sql = "insert into notifications (from_date, to_date, title, message, school_id, created_by, created_by_date, recepient) VALUES ( ?, ?, ?, ?, ?, ?, current_timestamp, ?)";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			
			pstm.setString(1, from_date);
			pstm.setString(2, to_date);
			pstm.setString(3, title);
			pstm.setString(4, message);
			pstm.setLong(5, school_id);
			pstm.setLong(6, created_by);
			//pstm.setString(7, created_by_date);
			pstm.setString(7, recepient);

			pstm.execute();
			pstm.close();
			c.close();
		return op;
		} 
		catch (Exception e) {
		throw new GenericException(" Insert notification failed:" + e.getMessage());

		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/notifications/{school_id}/school/{id}", method = RequestMethod.PUT)
	public String updatenotification(@PathVariable Long school_id, @PathVariable Long id, @RequestBody Notification nn, Authentication auth) {

		String op = "{ }";
		Connection c = null;
		// we have commented temporarly post notification. we changed on code based on schools

		/*System.out.println("Coming to Notification PUT code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.NOTIFICATION, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem Editing Notification for class");
		}*/
		
		try {

			c = DBConnection.getDBConnection();

			String sql = "update notifications set from_date =? ,to_date =?, title=?, message=?, modified_by=?,"
					+ " modified_by_date =current_timestamp, recepient = ? where id = ? ";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, nn.getfrom_date());
			pstmt.setString(2, nn.getto_date());
			pstmt.setString(3, nn.gettitle());
			pstmt.setString(4, nn.getmessage());
			pstmt.setLong(5, nn.getmodified_by());
			//pstmt.setString(6, nn.getmodified_by_date());
			pstmt.setString(6, nn.getrecepient());
			pstmt.setLong(7, id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update notifications for class failed:"	+ e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/notifications/{school_id}/school/{id}", method = RequestMethod.DELETE)
	public String removenotificationFromClass(@PathVariable Long school_id, @PathVariable Long id, Authentication auth) {

			String op = "{ }";
			Connection c = null;

			/*System.out.println("Coming to Notification Delete code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE,  TargetDomain.NOTIFICATION, TargetEntity.SCHOOL, school_id, null, null))
			{
			throw new GenericException("Authorization problem Deleting Notification for class");
			}*/
			
		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from notifications where id = ?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete notification failed:"+ e.getMessage());
		}
		return op;
	}
	
		
	@CrossOrigin()
	@RequestMapping(value = "/parent_teacher_meeting/{student_id}/class/{class_id}", method = RequestMethod.GET)
	public ResponseEntity<List<ParentTeacherMeeting>> getparentmeeting(@PathVariable Long student_id, @PathVariable Long class_id,
																		Authentication auth) {
		
			Connection c = null;
			ResultSet rst;
			ArrayList<ParentTeacherMeeting> pt = new ArrayList<ParentTeacherMeeting>();
			
			System.out.println("Coming to ParentTeacher Meeting code"   );

			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.PARENT_TEACHER_MEETING, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting ParentTeacher Meeting for class");
			}

		try {
			c = DBConnection.getDBConnection();
			
			String sql = "select  s.school_id, s.standard,s.section,s.acad_year, student_id, class_id, teacher_id, "
					+ "teacher_message, student_message, date, created_by, modified_by, created_by_date, modified_by_date "
					+ "from parent_teacher_meeting as p inner join school_class_year as s on class_id=s.id where student_id=?";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, student_id);
			//pstm.setLong(2, class_id);
			
			rst = pstm.executeQuery();
			
		while (rst.next()) {

			ParentTeacherMeeting log = new ParentTeacherMeeting();
			log.setclass_id(rst.getLong("class_id"));
			log.setstudent_id(rst.getLong("student_id"));
			log.setteacher_message(rst.getString("teacher_message"));
			log.setstudent_message(rst.getString("student_message"));
			log.setteacher_id(rst.getLong("teacher_id"));
			log.setdate(rst.getString("date"));
			log.setstandard(rst.getString("standard"));
			log.setsection(rst.getString("section"));
			log.setacad_year(rst.getString("acad_year"));
			log.setschool_id(rst.getLong("school_id"));
			log.setcreated_by(rst.getLong("created_by"));
			log.setmodified_by(rst.getLong("modified_by"));
			log.setcreated_by_date(rst.getTimestamp("created_by_date"));
			log.setmodified_by_date(rst.getTimestamp("modified_by_date"));

			pt.add(log);

		}
			pstm.close();
			c.close();
			return new ResponseEntity<List<ParentTeacherMeeting>>(pt, HttpStatus.OK);

		}
		catch (Exception e) {
		throw new GenericException("getting parent meeting details failed:"+ e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/parent_teacher_meeting/{class_id}/student/{student_id}", method = RequestMethod.POST)
	public String adddetails(@PathVariable Long class_id,@PathVariable Long student_id, 
								@RequestBody ParentTeacherMeeting log, Authentication auth) {

		System.out.println("Coming to ParentTeacher Meeting code"   );

		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.PARENT_TEACHER_MEETING, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem Posting ParentTeacher Meeting for class");
		}

		try {
			if (log == null) {
				throw new Exception("req login missing");
			}

			String op = "{ }";
			
			//Long student_id = log.getstudent_id();
			Long teacher_id = log.getteacher_id();
			String teacher_message = log.getteacher_message();
			String student_message = log.getstudent_message();
			String date = log.getdate();
			Long created_by = log.getcreated_by();
			//Timestamp created_by_date = log.getcreated_by_date();
		
			Connection c = null;
			c = DBConnection.getDBConnection();
			
			String sql = "insert into parent_teacher_meeting(class_id, student_id, teacher_id, teacher_message, student_message, date, created_by, created_by_date) values(?, ?, ?, ?, ?, ?, ?, current_timestamp)";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, class_id);
			pstm.setLong(2, student_id);
			pstm.setLong(3, teacher_id);
			pstm.setString(4, teacher_message);
			pstm.setString(5, student_message);
			pstm.setString(6, date);
			pstm.setLong(7, created_by);
			//Date now = new java.util.Date();
			//Timestamp created_by_date = new java.sql.Timestamp(now.getTime());
			//pstm.setTimestamp(8, created_by_date);
			pstm.execute();
			pstm.close();
			c.close();
		return op;
		}
		catch (Exception e) {
		throw new GenericException(" Insert parentmeet failed:" + e.getMessage());

		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/parent_teacher_meeting/{id}", method = RequestMethod.PUT)
	public String updateparentmeeting(@PathVariable Long id,
									   @RequestBody ParentTeacherMeeting pm) {

			String op = "{ }";
			Connection c = null;

		try {

			c = DBConnection.getDBConnection();

			String sql = "update parent_teacher_meeting set student_message=?, teacher_message=?, class_id=?, student_id=?, teacher_id=?, date=?, modified_by=?, modified_by_date=current_timestamp where id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, pm.getstudent_message());
			pstmt.setString(2, pm.getteacher_message());
			pstmt.setLong(3, pm.getclass_id());
			pstmt.setLong(4, pm.getstudent_id());
			pstmt.setLong(5, pm.getteacher_id());
			pstmt.setString(6, pm.getdate());
			pstmt.setLong(7, pm.getmodified_by());
			//pstmt.setTimestamp(8, pm.getmodified_by_date());
			pstmt.setLong(8, id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update parentteachermeeting for class failed:" + e.getMessage());
		}
	}
	
	
	@CrossOrigin()
	@RequestMapping(value = "/parent_teacher_meeting/{id}", method = RequestMethod.DELETE)
		public String deleteparentteachermeeting(@PathVariable Long id) {

			String op = "{ }";
			Connection c = null;

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from parent_teacher_meeting where id=?;";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete parentteachermeeting failed:"+ e.getMessage());
		}
		return op;

	}


	@CrossOrigin()
	@RequestMapping(value = "/classes/{class_id}/referencetimes", method = RequestMethod.GET)
	public ResponseEntity<List<ClassReferenceTime>> periodReference( @PathVariable Long class_id, Authentication auth) {
	
		Connection c = null;
		ResultSet res;
		ArrayList<ClassReferenceTime> times = new ArrayList<ClassReferenceTime>();

		System.out.println("Coming to Period Reference code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.REFERENCE_TIMETABLE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting Period Reference for class");
		}
		try {
			c = DBConnection.getDBConnection();

			String sql = "select start_time, end_time, period_type, description, class_id, s.school_id, s.standard,"
					+ "s.section,s.acad_year, c.id from period_reference as c inner join school_class_year as s on class_id=s.id "
					+ "where class_id=? order by period_type asc;";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);

			res = pstmt.executeQuery();
		while (res.next()) {
			ClassReferenceTime t = new ClassReferenceTime();
				t.setend_time(res.getString("end_time"));
				t.setperiod_type(res.getString("period_type"));
				t.setstart_time(res.getString("start_time"));
				t.setdescription(res.getString("description"));
				t.setstandard(res.getString("standard"));
				t.setsection(res.getString("section"));
				t.setacad_year(res.getString("acad_year"));
				t.setschool_id(res.getLong("school_id"));
				t.setclass_id(res.getLong("class_id"));
				t.setid(res.getLong("id"));


				times.add(t);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<ClassReferenceTime>>(times,HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("Get reference times failed:"
					+ e.getMessage());
		}

	}
	

	@CrossOrigin()
	@RequestMapping(value = "/period_reference/{class_id}/class", method = RequestMethod.PUT)
	public String storePeriod( @PathVariable ("class_id") Long class_id1, @RequestBody ClassReferenceTime[] p_referencetime,
								Authentication auth) {

			String op = "{ }";
			Connection c = null;
		
			System.out.println("Coming to Period Reference code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.REFERENCE_TIMETABLE, TargetEntity.CLASS, class_id1, null, null))
			{
			throw new GenericException("Authorization problem getting Period Reference for class");
			}

		try {
			Long[] class_id = new Long[ p_referencetime.length];
			
			for (int i = 0; i <  p_referencetime.length; i++) {
				class_id[i] =  p_referencetime[i].getclass_id();
				System.out.println("class id is"+ class_id);

			}
				c = DBConnection.getDBConnection();

			String sql = "select from upsert_period_reference(?, ?, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);

			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setString(2,  p_referencetime[0].getstart_time());
			pstmt.setString(3,  p_referencetime[0].getend_time());
			pstmt.setString(4,  p_referencetime[0].getperiod_type());
			pstmt.setString(5,  p_referencetime[0].getdescription());
			Long clss =  p_referencetime[0].getclass_id();
			System.out.println("class id "+ clss);
			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing attendance for period failed:" + e.getMessage());
		}
	}
	
	
	@CrossOrigin()
	@RequestMapping(value = "/classreference/{class_id}/delete", method = RequestMethod.PUT)
	public String deleteclassreferencetime(@PathVariable ("class_id") Long cls_id, @RequestBody ClassReferenceTime[] p_referencetime, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			System.out.println("Coming to Period Reference code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.REFERENCE_TIMETABLE, TargetEntity.CLASS, cls_id, null, null))
			{
			throw new GenericException("Authorization problem getting Period Reference for class");
			}

		try {
			Long[] class_id = new Long[p_referencetime.length];

			for (int i = 0; i < p_referencetime.length; i++) {
				class_id[i] = p_referencetime[i].getclass_id();
		   }
			
			c = DBConnection.getDBConnection();

			String sql = "select upsert_delete_period(?,?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			Array cls = c.createArrayOf("BIGINT", class_id);
			pstmt.setArray(1, cls);
			pstmt.setString(2, p_referencetime[0].getperiod_type());			
			
			pstmt.execute();
			pstmt.close();
			c.close();

		} 
		catch (Exception e) {
		throw new GenericException("Delete timetable failed:" + e.getMessage());
		}
		return op;

	}

	@CrossOrigin()
	@RequestMapping(value = "/classes/{class_id}/teacher", method = RequestMethod.GET)
	public ResponseEntity<List<Teacher_class>> getTeacherClass(@PathVariable Long class_id, Authentication auth) {
			
			Connection c = null;
			ResultSet res;
			ArrayList<Teacher_class> teacher = new ArrayList<Teacher_class>();

			System.out.println("Coming to Teacher list code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.TEACHER, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Teacher List for class");
			}
		try {

			c = DBConnection.getDBConnection();

			String sql = "select class_id, teacher_id, teacher_number, name from teacher_class "
						+ "left outer join user_record on teacher_id = tg_id where class_id=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);

			res = pstmt.executeQuery();
		
			while (res.next()) {
				Teacher_class s = new Teacher_class();
			
				s.setteacher_id(res.getLong("teacher_id"));
				s.setclass_id(res.getLong("class_id"));
				s.setname(res.getString("name"));
				s.setteacher_number(res.getString("teacher_number"));
				teacher.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Teacher_class>>(teacher, HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("Getting teacher for class failed:"	+ e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/student/{class_id}/class", method = RequestMethod.GET)
	public ResponseEntity<List<Student>> getStudents(@PathVariable Long class_id, Authentication auth) {
			Connection c = null;
			ResultSet res;
			ArrayList<Student> students = new ArrayList<Student>();
			
			System.out.println("Coming to Student list code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.STUDENT_LIST, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Student List for class");
			}
		try {

			c = DBConnection.getDBConnection();

			String sql = "select tg_id, name, student_roll_no, student_id, class_id, s.standard, s.section, s.school_id, "
					+ "s.acad_year from student_class as c inner join school_class_year as s on class_id=s.id "
					+ "left outer join user_record on student_id = tg_id where class_id=? order by name asc";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);

			res = pstmt.executeQuery();
		
			while (res.next()) {
				Student s = new Student();
				s.settg_id(res.getLong("tg_id"));
				s.setclass_id(res.getLong("class_id"));
				s.setstudent_roll_no(res.getString("student_roll_no"));
				s.setname(res.getString("name"));
				s.setstudent_id(res.getLong("student_id"));
				s.setstandard(res.getString("standard"));
				s.setsection(res.getString("section"));
				s.setacad_year(res.getString("acad_year"));
				s.setschool_id(res.getLong("school_id"));
				students.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Student>>(students, HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("Getting students for class failed:"	+ e.getMessage());
		}
	}
	
	
	@CrossOrigin()
	@RequestMapping(value = "/class/{class_id}/students", method = RequestMethod.GET)
	public ResponseEntity<List<Attendance>> getStudentsForClass(@PathVariable Long class_id, Authentication auth) {
			Connection c = null;
			ResultSet res;
			ArrayList<Attendance> students = new ArrayList<Attendance>();
			
			System.out.println("Coming to Attendance code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.ATTENDANCE, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Attendance for class");
			}
			
		try {

			c = DBConnection.getDBConnection();

			String sql = "select id, name, student_roll_no, student_id, class_id, s.standard, s.section, s.school_id, "
					+ "s.acad_year from student_class as c inner join school_class_year as s on class_id=s.id "
					+ "left outer join user_record on student_id = tg_id where class_id=? order by name asc";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);

			res = pstmt.executeQuery();
		
			while (res.next()) {
				Attendance s = new Attendance();
				s.setid(res.getLong("id"));
				s.setclass_id(res.getLong("class_id"));
				s.setstudent_roll_no(res.getString("student_roll_no"));
				s.setname(res.getString("name"));
				s.setstudent_id(res.getLong("student_id"));
				s.setstandard(res.getString("standard"));
				s.setsection(res.getString("section"));
				s.setacad_year(res.getString("acad_year"));
				s.setschool_id(res.getLong("school_id"));
				students.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Attendance>>(students, HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("Getting students for class failed:"	+ e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/attendance/{tt_id}/{date}/{class_id}/list", method = RequestMethod.GET)
	public ResponseEntity<List<Attendance>> getStudentsForClass(@PathVariable Long tt_id, @PathVariable String date,
																@PathVariable Long class_id, Authentication auth) {
			Connection c = null;
			ResultSet res;
			ArrayList<Attendance> students = new ArrayList<Attendance>();
			
			System.out.println("Coming to Attendance code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.ATTENDANCE, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Attendance for class");
			}
		try {

			c = DBConnection.getDBConnection();

				/*String sql = "select tt_id, date, s.student_id, class_id, student_roll_no, name, "
						+ "CASE WHEN(tt_id is null) then true ELSE attendance END as attendance "
						+ "from  student_class as s inner join user_record on s.student_id = tg_id "
						+ "left outer join student_attendance as a on s.student_id = a.student_id and tt_id = ? where s.class_id = ? ";*/
			String sql = "select tt_id, date, s.student_id, class_id, student_roll_no, name, CASE WHEN(tt_id is null) "
						+ "then true ELSE attendance END as attendance from  student_class as s "
						+ "inner join user_record on s.student_id = tg_id "
						+ "left outer join student_attendance as a on s.student_id = a.student_id and tt_id = ? "
						+ "and date = ? where s.class_id = ? ";
	
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, tt_id);
			pstmt.setString(2, date);
			pstmt.setLong(3, class_id);

			res = pstmt.executeQuery();
		
			while (res.next()) {
				Attendance s = new Attendance();
				s.setname(res.getString("name"));
				s.settt_id(res.getLong("tt_id"));
				s.setclass_id(res.getLong("class_id"));
				s.setstudent_roll_no(res.getString("student_roll_no"));
				s.setattendance(res.getBoolean("attendance"));
				s.setstudent_id(res.getLong("student_id"));
				students.add(s);
			}

			pstmt.close();
			c.close();
			return new ResponseEntity<List<Attendance>>(students, HttpStatus.OK);

		} catch (Exception e) {
			throw new GenericException("Getting students for class failed:"	+ e.getMessage());
		}
	}

	
	@CrossOrigin()
	@RequestMapping(value = "/classes/{class_id}/{student_id}/absent_list", method = RequestMethod.GET)
	public ResponseEntity<List<Attendance>> getstudent(@PathVariable Long class_id, @PathVariable Long student_id,
														Authentication auth) {
			
		Connection c = null;
			ResultSet res;
			ArrayList<Attendance> atts = new ArrayList<Attendance>();

			System.out.println("Coming to Attendance code"   );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.ATTENDANCE, TargetEntity.STUDENT, student_id, TargetEntity.CLASS, class_id))
			{
			throw new GenericException("Authorization problem getting Attendance for class");
			}
		try {

			c = DBConnection.getDBConnection();

			String sql = "select tt_id, attendance, date, s.student_id, name,c.class_id,c.student_roll_no,"
					+ " standard, section, school_id, acad_year from student_attendance as s "
					+ "left outer join user_record as u on u.tg_id=s.student_id "
					+ "left outer join student_class as c on c.student_id= s.student_id "
					+ "left outer join school_class_year on c.class_id=id where c.class_id=? and s.student_id=? order by date";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, class_id);
			pstmt.setLong(2, student_id);

			res = pstmt.executeQuery();
		while (res.next()) {
			Attendance a = new Attendance();
				a.settt_id(res.getLong("tt_id"));
				a.setattendance(res.getBoolean("attendance"));
				a.setdate(res.getString("date"));
				a.setstudent_id(res.getLong("student_id"));
				a.setname(res.getString("name"));
				a.setclass_id(res.getLong("class_id"));
				a.setstudent_roll_no(res.getString("student_roll_no"));
				a.setstandard(res.getString("standard"));
				a.setsection(res.getString("section"));
				a.setacad_year(res.getString("acad_year"));
				a.setschool_id(res.getLong("school_id"));

				atts.add(a);
			}

			pstmt.close();
			c.close();
		return new ResponseEntity<List<Attendance>>(atts, HttpStatus.OK);

		}
		catch (Exception e) {
		throw new GenericException("Get absent for period failed:" + e.getMessage());
		}
	}
	
	
	@CrossOrigin()
	@RequestMapping(value = "/classes/{tt_id}/class/{class_id}/attendance", method = RequestMethod.PUT)
	public String storeAttendanceForPeriod(@PathVariable Long tt_id, @PathVariable Long class_id, 
											@RequestBody Attendance[] p_attendances, Authentication auth) {
			
		String op = "{ }";
		Connection c = null;
		
		System.out.println("Coming to Attendance code" );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.ATTENDANCE, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem getting Attendance for class");
		}
		try {

			Long[] student_id = new Long[p_attendances.length];
			Boolean[] atts = new Boolean[p_attendances.length];
			
		for (int i = 0; i < p_attendances.length; i++) {
				student_id[i] = p_attendances[i].getstudent_id();
				atts[i] = p_attendances[i].getattendance();
				}

			c = DBConnection.getDBConnection();

			String sql = "select upsert_attendance(?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, tt_id);
			Array pst = c.createArrayOf("BIGINT", student_id);
			pstmt.setArray(2, pst);
			Array patt = c.createArrayOf("BOOLEAN", atts);
			pstmt.setArray(3, patt);
			pstmt.setString(4,p_attendances[0].getdate());
			pstmt.setLong(5, p_attendances[0].getcreated_by());
			pstmt.setLong(6, p_attendances[0].getmodified_by());
			pstmt.setString(7,p_attendances[0].getcreated_by_date());
			pstmt.setString(8,p_attendances[0].getmodified_by_date());

			
			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing attendance for period failed:" + e.getMessage());
		}
	}
	
	
	@CrossOrigin()
	@RequestMapping(value = "/work_report/{id}/{class_id}", method = RequestMethod.GET)
	public ResponseEntity<List<WorkUpdates>> getstatusforhomework(@PathVariable Long id, @PathVariable Long class_id, Authentication auth) {
			Connection c = null;
			ResultSet res;
			ArrayList<WorkUpdates> wu = new ArrayList<WorkUpdates>();
			
			System.out.println("Coming to Work Updates code" );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.CLASS_DIARY_UPDATES, TargetEntity.CLASS, class_id, null, null))
			{
			throw new GenericException("Authorization problem getting Work Updates for class");
			}

		try {
			c = DBConnection.getDBConnection();
			
			String sql = "select c.id, s.student_id, s.student_roll_no, w.status, w.class_diary_id, u.name, w.teacher_status from class_diary as c  "
					+ "inner join student_class as s on c.class_id = s.class_id and c.id = ? "					
					+ "inner join user_record as u on s.student_id = u.tg_id "
					+ "left outer join class_diary_updates as w on w.class_diary_id = c.id and s.student_id = w.student_id ";
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			
			pstmt.setLong(1, id);

			res = pstmt.executeQuery();
			while (res.next()) {
		
			WorkUpdates n = new WorkUpdates();
			n.setid(res.getLong("id"));
			n.setstatus(res.getString("status"));
			n.setteacher_status(res.getBoolean("teacher_status"));
			n.setclass_diary_id(res.getLong("class_diary_id"));
			n.setstudent_id(res.getLong("student_id"));
			n.setname(res.getString("name"));
			n.setstudent_roll_no(res.getString("student_roll_no"));


			wu.add(n);
			}
			pstmt.close();
			res.close();
			c.close();
		return new ResponseEntity<List<WorkUpdates>>(wu, HttpStatus.OK);
		}
		
		catch (Exception e) {
		throw new GenericException(	"Getting work status for school failed:" + e.getMessage());
		}
	}
	

	@CrossOrigin()
	@RequestMapping(value = "/workupdate/{student_id}/{class_diary_id}", method = RequestMethod.PUT)
	public String update(@PathVariable Long student_id,@PathVariable Long class_diary_id,
										@RequestBody WorkUpdates p_work, Authentication auth) {

			String op = "{ }";
			Connection c = null;
			System.out.println("Coming to Work Updates code" );
			
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.CLASS_DIARY_UPDATES, TargetEntity.STUDENT, student_id, null, null))
			{
			throw new GenericException("Authorization problem getting Work Updates for class");
			}

		try {
			c = DBConnection.getDBConnection();
			
			String sql = "select upsert_work_updates(?,?,?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, p_work.getstatus());
			pstmt.setLong(2, student_id);
			pstmt.setLong(3, class_diary_id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Adding work status failed:"	+ e.getMessage());
		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/quiz/{class_id}/{subject_id}", method = RequestMethod.GET)
	public ResponseEntity<List<Quiz>> getquizquestions(	@PathVariable Long class_id, 
												@PathVariable Long subject_id, Authentication auth) {
			
		Connection c = null;
		ResultSet rst;
		ArrayList<Quiz> q = new ArrayList<Quiz>();
		
		System.out.println("Coming to Quiz code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.QUIZ, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem Editing Notification for class");
		}

		try {
			
			c = DBConnection.getDBConnection();
			
			String sql = "select questions,option1,option2,option3,option4, answer from quiz where class_id =? and subject_id=? order by random() limit 25";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, class_id);
			pstm.setLong(2, subject_id);
			rst = pstm.executeQuery();
			
		while (rst.next()) {
			Quiz q1 = new Quiz();
			q1.setquestions(rst.getString("questions"));
			q1.setoption1(rst.getString("option1"));
			q1.setoption2(rst.getString("option2"));
			q1.setoption3(rst.getString("option3"));
			q1.setoption4(rst.getString("option4"));
			q1.setanswer(rst.getString("answer"));
			q.add(q1);
			}
			pstm.close();
			c.close();
		return new ResponseEntity<List<Quiz>>(q, HttpStatus.OK);
		}
		catch (Exception e) {
		throw new GenericException("Getting questions failed:" + e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "quiz/{class_id}/{subject_id}", method = RequestMethod.POST)
	public String addQuiz(@PathVariable Long class_id, @PathVariable Long subject_id, 
							@RequestBody Quiz q, Authentication auth) {
		
		System.out.println("Coming to Notification PUT code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.QUIZ, TargetEntity.CLASS, class_id, null, null))
		{
		throw new GenericException("Authorization problem Editing Notification for class");
		}

		try {
		if (q == null) {
		throw new Exception("req. quiz missing");
		
		}
			String op = "{ }";
			String questions = q.getquestions();
			String option1 = q.getoption1();
			String option2 = q.getoption2();
			String option3 = q.getoption3();
			String option4 = q.getoption4();
			String answer = q.getanswer();
			
			Connection c = null;
			c = DBConnection.getDBConnection();
			
			String sql = "insert into quiz(class_id, subject_id, questions, option1, option2, option3, option4, answer) values (?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setLong(1, class_id);
			pstm.setLong(2, subject_id);
			pstm.setString(3, questions);
			pstm.setString(4, option1);
			pstm.setString(5, option2);
			pstm.setString(6, option3);
			pstm.setString(7, option4);
			pstm.setString(8, answer);
			pstm.execute();
			pstm.close();
			c.close();
		return op;
		}
		catch (Exception e) {
		throw new GenericException(" Insert quiz failed:" + e.getMessage());
		}
	}

	@CrossOrigin()
	@RequestMapping(value = "/login/{login_key}", method = RequestMethod.GET)
	public ResponseEntity<List<Master>> getNew(@PathVariable String login_key) {
		
			Connection c = null;
			ResultSet rst;
			ArrayList<Master> pt = new ArrayList<Master>();
		
		try {
			c = DBConnection.getDBConnection();
			
			/*String sql = "select u.tg_id, u.name, u.login_key, u.access_role, tc.teacher_number,"
					+ "su.name as student_name, scy.id as student_class_id, scy.standard as student_standard,"
					+ "scy.section as student_section, scy.school_id, scy.acad_year, "
					+ "tc.class_id as teacher_class_id, scyt.standard as teacher_standard, scyt.section as teacher_section from user_record AS u"
					+ " left join teacher_class tc on u.tg_id = tc.teacher_id and u.access_role = 'T' "
					+ "left join school_class_year scyt on tc.class_id = scyt.id "
					+ "left join parent_child pc on u.tg_id = pc.parent_id and u.access_role = 'P'"
					+ "left join student_class sc on sc.student_id = pc.student_id and u.access_role= 'S'"
					+ "left join user_record su on sc.student_id = su.tg_id "
					+ "left join school_class_year scy on sc.class_id = scy.id where u.login_key = ?";*/
			
			String sql = " select u.tg_id, u.login_key, u.name, u.access_role, tc.teacher_number, sc.student_id, sc.student_roll_no,"
					+ " scys.standard as standard, scys.section as section, su.name as student_name, scy.id as student_class_id,"
					+ " scy.standard as student_standard, scy.section as student_section, scy.school_id, scy.acad_year, tc.class_id as teacher_class_id, "
					+ "scyt.standard as teacher_standard, scyt.section as teacher_section from user_record as u "
					+ "left join teacher_class tc on u.tg_id = tc.teacher_id and u.access_role = 'T' "
					+ "left join school_class_year scyt on tc.class_id = scyt.id "
					+ "left join parent_child pc on u.tg_id = pc.parent_id and u.access_role = 'P'	"
					+ "left join student_class sc on sc.student_id = pc.student_id "
					+ "left join student_class scs on scs.student_id = u.tg_id and u.access_role= 'S' "
					+ "left join user_record su on sc.student_id = su.tg_id "
					+ "left join school_class_year scy on sc.class_id = scy.id "
					+ "left join school_class_year scys on scs.class_id = scys.id where u.login_key = ?";
			
			PreparedStatement pstm = c.prepareStatement(sql);
			pstm.setString(1, login_key);
			rst = pstm.executeQuery();
			
		while (rst.next()) {
		
			Master log = new Master();
			log.settg_id(rst.getLong("tg_id"));
			log.setname(rst.getString("name"));
			log.setlogin_key(rst.getString("login_key"));
			log.setaccess_role(rst.getString("access_role"));
			log.setteacher_number(rst.getString("teacher_number"));
			log.setstudent_name(rst.getString("student_name"));
			log.setstudent_class_id(rst.getLong("student_class_id"));
			log.setstudent_standard(rst.getString("student_standard"));
			log.setstudent_section(rst.getString("student_section"));
			log.setschool_id(rst.getLong("school_id"));
			log.setacad_year(rst.getString("acad_year"));
			log.setteacher_class_id(rst.getLong("teacher_class_id"));
			log.setteacher_standard(rst.getString("teacher_standard"));
			log.setteacher_section(rst.getString("teacher_section"));
			log.setstandard(rst.getString("standard"));
			log.setsection(rst.getString("section"));
			log.setstudent_id(rst.getLong("student_id"));
			log.setstudent_roll_no(rst.getString("student_roll_no"));
			System.out.println(student_class_id + "student_standard");
			pt.add(log);

		}
			pstm.close();
			c.close();
			return new ResponseEntity<List<Master>>(pt, HttpStatus.OK);

		}
		catch (Exception e) {
		throw new GenericException("gettiong meeting details failed:"+ e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/parent/{parent_id}", method = RequestMethod.PUT)
	public String storeteacher_details(@PathVariable Long parent_id, @RequestBody parent_child[] p_parent) {
			
		String op = "{ }";
		Connection c = null;
		
		try {
			Long[] student_id = new Long[p_parent.length];

		for (int i = 0; i < p_parent.length; i++) {
			student_id[i] = p_parent[i].getstudent_id();
			 }
		
		c = DBConnection.getDBConnection();

		String sql = "select upsert_parent_child(?, ?)";
		PreparedStatement pstmt = c.prepareStatement(sql);
				
				pstmt.setLong(1, parent_id);
				Array rn = c.createArrayOf("BIGINT", student_id);
				pstmt.setArray(2, rn);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		
		catch (Exception e) {
		throw new GenericException("Storing parent_child data failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/{acad_year}/student", method = RequestMethod.GET)
	public ResponseEntity<List<Master>> getstudent(@PathVariable Long school_id, @PathVariable String acad_year, Authentication auth) {
		
		ArrayList<Master> teacher = new ArrayList<Master>();
		Connection c = null;
		ResultSet res;

		System.out.println("Coming to student code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting student for class");
		}
		
		try {
			c = DBConnection.getDBConnection();

			String sql = "select s.student_id, student_roll_no, s.class_id, parent_id, u.name as student_name, ur.name,"
					+ " u.access_role as student_access_role, ur.access_role, u.login_key as student_key, ur.login_key, "
					+ " standard, section from school_class_year as scy inner join student_class as s on s.class_id=scy.id "
					+ "inner join user_record as u on u.tg_id = s.student_id "
					+ "left outer join parent_child as p on s.student_id=p.student_id "
					+ "left outer join user_record as ur on ur.tg_id=p.parent_id where school_id=? and acad_year=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, acad_year);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			Master s = new Master();
			s.setstudent_id(res.getLong("student_id"));
			s.setstudent_roll_no(res.getString("student_roll_no"));
			s.setclass_id(res.getLong("class_id"));
			s.setparent_id(res.getLong("parent_id"));
			s.setstudent_name(res.getString("student_name"));
			s.setname(res.getString("name"));
			s.setaccess_role(res.getString("access_role"));
			s.setstudent_access_role(res.getString("student_access_role"));
			s.setlogin_key(res.getString("login_key"));
			s.setstudent_key(res.getString("student_key"));
			s.setstandard(res.getString("standard"));
			s.setsection(res.getString("section"));

			teacher.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<Master>>(teacher, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}
	
	@CrossOrigin()
	@RequestMapping(value = "/schools/{school_id}/{acad_year}/teacher", method = RequestMethod.GET)
	public ResponseEntity<List<Master>> getteacher(@PathVariable Long school_id, @PathVariable String acad_year, Authentication auth) {
		
		ArrayList<Master> teacher = new ArrayList<Master>();
		Connection c = null;
		ResultSet res;		
		
		System.out.println("Coming to Teacher code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting teacher for class");
		}
		try {
			c = DBConnection.getDBConnection();

			String sql = "select distinct on (teacher_id) teacher_id, class_id, teacher_number, name, access_role, login_key from "
					+ "school_class_year as scy inner join teacher_class on scy.id=class_id "
					+ "inner join user_record on teacher_id=tg_id where school_id=? and acad_year=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, school_id);
			pstmt.setString(2, acad_year);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			Master s = new Master();
			s.setteacher_id(res.getLong("teacher_id"));
			s.setclass_id(res.getLong("class_id"));
			s.setteacher_number(res.getString("teacher_number"));
			s.setname(res.getString("name"));
			s.setaccess_role(res.getString("access_role"));
			s.setlogin_key(res.getString("login_key"));

			teacher.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<Master>>(teacher, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}
	
	/*@CrossOrigin()
	@RequestMapping(value = "/class/{teacher_id}/teacher", method = RequestMethod.PUT)
	public String storeteacher_details(@PathVariable Long teacher_id, @RequestBody Teacher_class[] p_teacher) {
			
		String op = "{ }";
		Connection c = null;
		
		try {

			Long[] class_id = new Long[p_teacher.length];

		for (int i = 0; i < p_teacher.length; i++) {
				class_id[i] = p_teacher[i].getclass_id();
		}
		c = DBConnection.getDBConnection();

			String sql = "select upsert_teacher(?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
				
				pstmt.setLong(1, teacher_id);
				Array cls = c.createArrayOf("BIGINT", class_id);
				pstmt.setArray(2, cls);
				pstmt.setString(3, p_teacher[0].getteacher_number());

            pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		} 
		catch (Exception e) {
		throw new GenericException("Storing teacher_class for period failed:" + e.getMessage());
		}
	}*/

	/*@CrossOrigin()
	@RequestMapping(value = "/school/{login_key}", method = RequestMethod.POST)
	public String addstandard(@PathVariable String login_key, @RequestBody User_record ur) {

		try {
			if (ur == null) {
				throw new Exception("Request body missing");
			}

			String op = "{ }";
			String name = ur.getname();
			String access_role = ur.getaccess_role();

			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into user_record(login_key, name, access_role) values (?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, login_key);
			pstmt.setString(2, name);
			pstmt.setString(3, access_role);

			pstmt.execute();
			pstmt.close();
			c.close();
			return op;

		} 
		catch (Exception e) {
		throw new GenericException("Add members failed:" + e.getMessage());
		
		}
	}*/

	/*@CrossOrigin()
	@RequestMapping(value = "/student/{student_roll_no}", method = RequestMethod.POST)
	public String addstandard(@PathVariable String student_roll_no, @RequestBody Student ur) {

		try {
			if (ur == null) {
				throw new Exception("Request body missing");
			}

			String op = "{ }";
			Long student_id = ur.getstudent_id();
			Long class_id = ur.getclass_id();


			Connection c = null;
			c = DBConnection.getDBConnection();

			String sql = "insert into student_class(student_id, class_id, student_roll_no) values (?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, student_id);
			pstmt.setLong(2, class_id);
			pstmt.setString(3, student_roll_no);

			pstmt.execute();
			pstmt.close();
			c.close();
			return op;

		} 
		catch (Exception e) {
		throw new GenericException("Add members failed:" + e.getMessage());
		
		}
	}*/

		/*@CrossOrigin()
	@RequestMapping(value = "/user_record/{login_key}/{access_role}", method = RequestMethod.PUT)
	public String updateuser(@PathVariable String login_key, @PathVariable String access_role, @RequestBody User_record record) {

		String op = "{ }";
		Connection c = null;

		try {

			c = DBConnection.getDBConnection();

			String sql = "update user_record set login_key=? where login_key=? and access_role=?";
 
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, record.getlogin_key());

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update user record for class failed:"+ e.getMessage());
		}
	}*/
	
/*	@CrossOrigin()
	@RequestMapping(value = "/user_record/{student_roll_no}", method = RequestMethod.PUT)
	public String updateuser(@PathVariable String student_roll_no, @RequestBody User_record record) {

		String op = "{ }";
		Connection c = null;

		try {

			c = DBConnection.getDBConnection();

			String sql = "update user_record set student_roll_no=? and class_id=? where student_roll_no=?";
 
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, record.getstudent_roll_no());
			pstmt.setLong(2, record.getclass_id());

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update user record for class failed:"+ e.getMessage());
		}
				
	}*/


	
	/*@CrossOrigin()
	@RequestMapping(value = "/user_record/{student_id}", method = RequestMethod.PUT)
	public String updateuser(@PathVariable Long student_id, @RequestBody User_record record) {

		String op = "{ }";
		Connection c = null;

		try {

			c = DBConnection.getDBConnection();

			String sql = "update user_record set login_key=? "
					+ "from( select parent_id from parent_child where student_id=?) i where tg_id= i.parent_id";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, record.getlogin_key());
			pstmt.setLong(2, student_id);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Update user record for class failed:"+ e.getMessage());
		}
	}*/

	/*@CrossOrigin()
	@RequestMapping(value = "/user_record/{login_key}/delete", method = RequestMethod.DELETE)
	public String deleterecord(@PathVariable String login_key) {
		
		String op = "{ }";
		Connection c = null;

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from user_record where login_key=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, login_key);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Remove standard failed:" + e.getMessage());
		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/schools/{student_roll_number}/student", method = RequestMethod.DELETE)
	public String deletestudent(@PathVariable Long student_roll_number) {
		
		String op = "{ }";
		Connection c = null;

		try {
			c = DBConnection.getDBConnection();

			String sql = "delete from student_class  where student_roll_number=?";

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, student_roll_number);

			pstmt.execute();
			pstmt.close();
			c.close();
		return op;

		}
		catch (Exception e) {
		throw new GenericException("Remove standard failed:" + e.getMessage());
		}
	}*/
	
	@CrossOrigin()
	@RequestMapping(value = "/school/{school_id}/teachers/{teacher_id}", method = RequestMethod.GET)
	public ResponseEntity<List<Teacher>> getteacherdatas( @PathVariable Long teacher_id, @PathVariable Long school_id,
															Authentication auth) {
		
		ArrayList<Teacher> teacher = new ArrayList<Teacher>();
		Connection c = null;
		ResultSet res;
		
		System.out.println("Coming to Teacher code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting teacher for class");
		}

		try {
			c = DBConnection.getDBConnection();

			String sql = "select distinct on (standard) standard as teacher_standard, section as teacher_section, "
					+ "name as teacher_name, teacher_id, teacher_number, class_id as teacher_class_id from teacher_class as t "
					+ "left outer join user_record on tg_id = teacher_id "
					+ "left outer join school_class_year on class_id=id where teacher_id=?";	
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setLong(1, teacher_id);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			Teacher s = new Teacher();
			s.setteacher_name(res.getString("teacher_name"));
			s.setteacher_number(res.getString("teacher_number"));
			s.setteacher_id(res.getLong("teacher_id"));
			s.setteacher_class_id(res.getLong("teacher_class_id"));
			s.setteacher_standard(res.getString("teacher_standard"));
			s.setteacher_section(res.getString("teacher_section"));

			teacher.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<Teacher>>(teacher, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}	
	

	@CrossOrigin()
	@RequestMapping(value = "/school/{school_id}/studentdetails/{student_roll_no}", method = RequestMethod.GET)
	public ResponseEntity<List<Student>> getstudentdatas( @PathVariable String student_roll_no, @PathVariable Long school_id, Authentication auth) {
		
		ArrayList<Student> student = new ArrayList<Student>();
		Connection c = null;
		ResultSet res;

		System.out.println("Coming to Student code"   );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Student for class");
		}

		try {
			c = DBConnection.getDBConnection();

			String sql = "select tg_id, class_id, student_roll_no, name, login_key, access_role, standard, "
					+ "section from student_class left outer join user_record on student_id=tg_id "
					+ "left outer join school_class_year on class_id=id where student_roll_no=?";	
			
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, student_roll_no);

			res = pstmt.executeQuery();

		while (res.next()) {
			
			Student s = new Student();
			s.settg_id(res.getLong("tg_id"));
			s.setclass_id(res.getLong("class_id"));
			s.setstudent_roll_no(res.getString("student_roll_no"));
			s.setname(res.getString("name"));
			s.setlogin_key(res.getString("login_key"));
			s.setaccess_role(res.getString("access_role"));
			s.setstandard(res.getString("standard"));
			s.setsection(res.getString("section"));

			student.add(s);
			}

			pstmt.close();
			c.close();
		
			return new ResponseEntity<List<Student>>(student, HttpStatus.OK);
		}

		catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}
	

	@CrossOrigin()
	@RequestMapping(value = "/user_record/{class_id}/{student_roll_no}/{update_ind}/{school_id}", method = RequestMethod.POST)
	public String addstudentrecord(@PathVariable Long class_id, @PathVariable String student_roll_no, @PathVariable String update_ind,
									@PathVariable Long school_id,@RequestBody Master p_user, Authentication auth) {

		String op = "{ }";
		//Long login_key = p_user.getlogin_key();
		Connection c = null;
		
		/*System.out.println("Coming to Student code"   );
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Student for class");
		}*/

	try {
		c = DBConnection.getDBConnection();
		
			String sql = "select upsert_student(?, ?, ?, ?, ?, ?, ?, ?)";
			
		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, p_user.getlogin_key());
		pstmt.setString(2, p_user.getname());
		pstmt.setString(3, p_user.getstudent_key());
		pstmt.setString(4, p_user.getstudent_name());
		pstmt.setLong(5, p_user.getstudent_id());
		pstmt.setLong(6, class_id);
		pstmt.setString(7, student_roll_no);
		pstmt.setString(8, update_ind);
		System.out.println("update ;ind" + update_ind);
		pstmt.execute();
		pstmt.close();
		c.close();
		
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Adding work status failed:"	+ e.getMessage());
	}
}

	@CrossOrigin()
	@RequestMapping(value = "/user_record/{teacher_number}/{school_id}/teacher", method = RequestMethod.POST)
	public String addteacherdatas(@PathVariable String teacher_number,	@PathVariable Long school_id,
									@RequestBody Teacher[] p_user, Authentication auth) {

		String op = "{ }";
		Connection c = null;
		
		/*System.out.println("Coming to Teacher code"   );
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Teacher for class");
		}*/


		try {
			Long[] teacher_class_id = new Long[ p_user.length];
			
			for (int i = 0; i <  p_user.length; i++) {
				teacher_class_id[i] =  p_user[i].getteacher_class_id();

			}
			
		c = DBConnection.getDBConnection();
		
		String sql = "select insert_teacher(?, ?, ?, ?)";
		
		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, p_user[0].getteacher_key());
		pstmt.setString(2, p_user[0].getteacher_name());
		Array cls = c.createArrayOf("BIGINT", teacher_class_id);
		pstmt.setArray(3, cls);
		pstmt.setString(4, teacher_number);
		System.out.println("class_id" + cls);
		System.out.println("class id" + teacher_class_id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Adding work status failed:"	+ e.getMessage());
	}
	}


	@CrossOrigin()
	@RequestMapping(value = "/teacher/{teacher_id}/{teacher_number}/{school_id}/delete", method = RequestMethod.PUT)
	public String deleteteacher(@PathVariable Long teacher_id, @PathVariable String teacher_number,
								@PathVariable Long school_id, Authentication auth) {
	
			String op = "{ }";
			Connection c = null;
			
		/*	System.out.println("Coming to Teacher code"   );
			if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
			{
			throw new GenericException("Authorization problem getting Teacher for class");
			}*/

		try {
						
			c = DBConnection.getDBConnection();

			String sql = "select delete_teacher(?, ?)";

			PreparedStatement pstmt = c.prepareStatement(sql);
				
			pstmt.setLong(1, teacher_id);			
			pstmt.setString(2, teacher_number);			

			pstmt.execute();
			pstmt.close();
			c.close();
		
		} 
		catch (Exception e) {
		throw new GenericException("Delete teacher failed:" + e.getMessage());
		}
		return op;

	}

	@CrossOrigin()
	@RequestMapping(value = "/teacher/{teacher_number}/{update_ind}/{school_id}", method = RequestMethod.POST)
	public String addteacherrecord(@PathVariable String teacher_number, @PathVariable String update_ind,
									@PathVariable Long school_id, @RequestBody Teacher p_user[], Authentication auth) {

		String op = "{ }";
		Connection c = null;

		/*System.out.println("Coming to Teacher code"   );
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
		{
		throw new GenericException("Authorization problem getting Teacher for class");
		}*/
		
		try {
			Long[] class_id = new Long[p_user.length];

			for (int i = 0; i < p_user.length; i++) {
				class_id[i] = p_user[i].getteacher_class_id();
				System.out.println("class id" + class_id[i]);
			}
			
			c = DBConnection.getDBConnection();
		
			String sql = "select upsert_teacher(?, ?, ?, ?, ?)";
			
		PreparedStatement pstmt = c.prepareStatement(sql);
		
		Array cls = c.createArrayOf("BIGINT", class_id);
		pstmt.setArray(1, cls);
		pstmt.setString(2, p_user[0].getteacher_key());
		pstmt.setString(3, p_user[0].getteacher_name());
		pstmt.setString(4, teacher_number);
		pstmt.setString(5, update_ind);
		
		System.out.println("update ;ind" + update_ind);
		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Adding teacher details failed:"	+ e.getMessage());
	}
}


@CrossOrigin()
	@RequestMapping(value = "/schools/{tg_id}/images", method = RequestMethod.POST)
	public String addImageToSchool(@PathVariable("tg_id") Long id, @RequestBody SchoolImage scimg, Authentication auth) {
	
	try {

	if (scimg == null) {

	throw new Exception("req. body missing");

	}

	String op = "{ }";

	//Cloudinary cloudinary = new Cloudinary();
	//cloudinary(ObjectUtils.asMap(
	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
	  "cloud_name", "dxp9zjbwe",
	  "api_key", "358771889364622",
	  "api_secret", "GaaQxGHbUgyByhQT87ZVeC62Rys"));

	Map uploadResult = cloudinary.uploader().upload(scimg.getbase64img(), ObjectUtils.emptyMap());

	String url = uploadResult.get("secure_url").toString();

	if (url == null || url.trim().length() <= 0)

	throw new Exception("cloudinary upload failed");

	Connection c = null;

	c = DBConnection.getDBConnection();

	String sql = "select insert_image (?,?)";

	PreparedStatement pstmt = c.prepareStatement(sql);

	pstmt.setString(1, url);
	pstmt.setLong(2, id);

	pstmt.execute();
	pstmt.close();
	c.close();

	return op;
	} catch (Exception e) {

	throw new GenericException("Adding school image failed:" + e.getMessage());

	}
	}


@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/gallery_images", method = RequestMethod.POST)
public String addImage(@PathVariable Long school_id , @RequestBody SchoolImage scimg, Authentication auth) {

		try {
		
		if (scimg == null) {
		
		throw new Exception("req. body missing");
		
		}
		
		String op = "{ }";
		//Long school_id = scimg.getschool_id();
		//String update_ind = scimg.getupdate_ind();
		
		//Cloudinary cloudinary = new Cloudinary();
		//cloudinary(ObjectUtils.asMap(
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
		  "cloud_name", "dxp9zjbwe",
		  "api_key", "358771889364622",
		  "api_secret", "GaaQxGHbUgyByhQT87ZVeC62Rys"));
		
		Map uploadResult = cloudinary.uploader().upload(scimg.getbase64img(), ObjectUtils.emptyMap());
		
		String url = uploadResult.get("secure_url").toString();
		
		if (url == null || url.trim().length() <= 0)
		
		throw new Exception("cloudinary upload failed");
		
		Connection c = null;
		
		c = DBConnection.getDBConnection();
		
		String sql = "insert into gallery (school_id, url, update_ind) values (?, ?, 'I')";
		
		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);
		pstmt.setString(2, url);
		//pstmt.setString(3, update_ind);
		System.out.println("My url" + school_id + url);
		pstmt.execute();
		pstmt.close();
		c.close();
		
		return op;
		} catch (Exception e) {
		
		throw new GenericException("Adding school image failed:" + e.getMessage());
		
		}
	}
		



@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/gallery_video", method = RequestMethod.POST)
public String addvideol(@PathVariable Long school_id, @RequestBody SchoolImage scimg, Authentication auth) {

try {

if (scimg == null) {

throw new Exception("req. body missing");

}

String op = "{ }";
//Long school_id = scimg.getschool_id();
//String update_ind = scimg.getupdate_ind();

//Cloudinary cloudinary = new Cloudinary();
//cloudinary(ObjectUtils.asMap(
Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
  "cloud_name", "dxp9zjbwe",
  "api_key", "358771889364622",
  "api_secret", "GaaQxGHbUgyByhQT87ZVeC62Rys"));


Map uploadResult = cloudinary.uploader().upload(scimg.getbase64img(), ObjectUtils.asMap("resource_type", "video"));

String url = uploadResult.get("secure_url").toString();

if (url == null || url.trim().length() <= 0)

throw new Exception("cloudinary upload failed");

Connection c = null;

c = DBConnection.getDBConnection();

String sql = "insert into gallery (school_id, url, update_ind) values (?, ?, 'V')";

PreparedStatement pstmt = c.prepareStatement(sql);

pstmt.setLong(1, school_id);
pstmt.setString(2, url);

pstmt.execute();
pstmt.close();
c.close();

return op;
} catch (Exception e) {

throw new GenericException("Adding school image failed:" + e.getMessage());

}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/{update_ind}/gallery", method = RequestMethod.GET)
public ResponseEntity<List<SchoolImage>> getimages(@PathVariable Long school_id,@PathVariable String update_ind, Authentication auth) {
	
	ArrayList<SchoolImage> subjects = new ArrayList<SchoolImage>();
	Connection c = null;
	ResultSet res;


	try {
		c = DBConnection.getDBConnection();

		String sql = "select url, id from gallery where school_id=? and update_ind=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		
		pstmt.setLong(1, school_id);
		pstmt.setString(2, update_ind);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		SchoolImage s = new SchoolImage();
		
		s.seturl(res.getString("url"));
		s.setid(res.getLong("id"));

		subjects.add(s);
		//System.out.println("my url" + url);
	}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<SchoolImage>>(subjects, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/school/{id}/gallery", method = RequestMethod.DELETE)
public String deleteimage(@PathVariable Long id, Authentication auth) {
	
	String op = "{ }";
	Connection c = null;
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "delete from gallery where id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Remove gallery failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{tg_id}/classes", method = RequestMethod.GET)
public ResponseEntity<List<SchoolImage>> getschool_class_year(@PathVariable Long tg_id, Authentication auth) {
	
	ArrayList<SchoolImage> subjects = new ArrayList<SchoolImage>();
	Connection c = null;
	ResultSet res;


	try {
		c = DBConnection.getDBConnection();

		String sql = "select url, tg_id from school_images where tg_id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		
		pstmt.setLong(1, tg_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		SchoolImage s = new SchoolImage();
		
		s.seturl(res.getString("url"));
		s.settg_id(res.getLong("tg_id"));
		subjects.add(s);
		//System.out.println("my url" + url);
	}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<SchoolImage>>(subjects, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/school/{field_id}/school_constants", method = RequestMethod.GET)
public ResponseEntity<List<School_constant>> getschool_constants(@PathVariable Long field_id, Authentication auth) {
	
	ArrayList<School_constant> scl = new ArrayList<School_constant>();
	Connection c = null; 
	ResultSet res;

	try {
		c = DBConnection.getDBConnection();

		String sql = "select field_name, field_value, active, school_id, start_time, end_time from school_constants where field_id=? ";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, field_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		School_constant s = new School_constant();
		s.setfield_name(res.getString("field_name"));
		s.setfield_value(res.getString("field_value"));
		s.setactive(res.getBoolean("active"));
		s.setschool_id(res.getLong("school_id"));
		s.setstart_time(res.getString("start_time"));
		s.setend_time(res.getString("end_time"));

		scl.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<School_constant>>(scl, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/template", method = RequestMethod.GET)
public ResponseEntity<List<Template>> getschooltemplate(@PathVariable Long school_id, Authentication auth) {
	
	ArrayList<Template> temp = new ArrayList<Template>();
	Connection c = null;
	ResultSet res;

	try {
		c = DBConnection.getDBConnection();

		String sql = "select id, template_message, template_title from templates where school_id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Template s = new Template();
		s.setid(res.getLong("id"));
		s.settemplate_message(res.getString("template_message"));
		s.settemplate_title(res.getString("template_title"));

		temp.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Template>>(temp, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting templates failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/template", method = RequestMethod.POST)
public String addtemplate(@PathVariable Long school_id, @RequestBody Template temp, Authentication auth) {

	
	try {
		if (temp == null) {
			throw new Exception("Request body missing");
		}

		String op = "{ }";
		String template_message = temp.gettemplate_message();
		String template_title = temp.gettemplate_title();
		
		Connection c = null;
		c = DBConnection.getDBConnection();

		String sql = "insert into templates(school_id, template_message, template_title) values (?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);
		pstmt.setString(2, template_message);
		pstmt.setString(3, template_title);
		
		pstmt.execute();
		pstmt.close();
		c.close();
		return op;

	} 
	catch (Exception e) {
	throw new GenericException("Add templates failed:" + e.getMessage());
	
	}
}

@CrossOrigin()
@RequestMapping(value = "/schools/{id}/template", method = RequestMethod.DELETE)
public String deletetemplate(@PathVariable Long id, Authentication auth) {
	
	String op = "{ }";
	Connection c = null;
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "delete from templates where id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Remove template failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/{update_ind}/compliant", method = RequestMethod.GET)
public ResponseEntity<List<Template>> getcompliant(@PathVariable Long school_id, @PathVariable String update_ind, Authentication auth) {
	
	ArrayList<Template> temp = new ArrayList<Template>();
	Connection c = null;
	ResultSet res;

	try {
		c = DBConnection.getDBConnection();

		String sql = "select id, message, update_ind from compliant where school_id=? and update_ind=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);
		pstmt.setString(2, update_ind);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Template s = new Template();
		s.setid(res.getLong("id"));
		s.setmessage(res.getString("message"));
		s.setupdate_ind(res.getString("update_ind"));

		
		temp.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Template>>(temp, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting templates failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{student_id}/{school_id}/compliant", method = RequestMethod.PUT)
public String addcompliant(@PathVariable Long student_id, @PathVariable Long school_id, @RequestBody Template temp, Authentication auth) {

System.out.println("Coming to Work Updates code" );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.TEMPLATE, TargetEntity.STUDENT, student_id, null, null))
	{
	throw new GenericException("Authorization problem getting Work Updates for class");
	}

	
	try {
		if (temp == null) {
			throw new Exception("Request body missing");
		}

		String op = "{ }";
		String message = temp.getmessage();
		String update_ind = temp.getupdate_ind();
		
		Connection c = null;
		c = DBConnection.getDBConnection();

		String sql = "insert into compliant(school_id, message, update_ind) values (?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);
		pstmt.setString(2, message);
		pstmt.setString(3, update_ind);
		
		pstmt.execute();
		pstmt.close();
		c.close();
		return op;

	} 
	catch (Exception e) {
	throw new GenericException("Add compliant failed:" + e.getMessage());
	
	}
}

@CrossOrigin()
@RequestMapping(value = "/schools/{id}/compliant", method = RequestMethod.DELETE)
public String deleteCompliant(@PathVariable Long id, Authentication auth) {
	
	String op = "{ }";
	Connection c = null;
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "delete from compliant where id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Remove Compliant failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/classes/{class_diary_id}/status", method = RequestMethod.PUT)
public String teacherstats(@PathVariable Long class_diary_id, @RequestBody WorkUpdates[] p_work, Authentication auth) {
		
	String op = "{ }";
	Connection c = null;
	
	try {

		Long[] student_id = new Long[p_work.length];
		Boolean[] atts = new Boolean[p_work.length];
		
	for (int i = 0; i < p_work.length; i++) {
			student_id[i] = p_work[i].getstudent_id();
			atts[i] = p_work[i].getteacher_status();
			}

		c = DBConnection.getDBConnection();

		String sql = "select teacher_diary_update(?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		Array patt = c.createArrayOf("BOOLEAN", atts);
		pstmt.setArray(1, patt);
		Array pst = c.createArrayOf("BIGINT", student_id);
		pstmt.setArray(2, pst);
		pstmt.setLong(3, class_diary_id);
				
		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	} 
	catch (Exception e) {
	throw new GenericException("Storing attendance for period failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/dailydiary/{id}/co-ordinator", method = RequestMethod.PUT)
public String updatedailydiary(@PathVariable Long id, @RequestBody Dailydiary diary, Authentication auth) {

	String op = "{ }";
	Connection c = null;

	try {

		c = DBConnection.getDBConnection();

		String sql = "update class_diary set coordinator_update=? where id = ?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setBoolean(1, diary.getcoordinator_update());
		pstmt.setLong(2, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Update dailydiary for class failed:"+ e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{class_id}/{id}/staff_list", method = RequestMethod.GET)
public ResponseEntity<List<TimeTable>> getstaff_listfortimetable( @PathVariable Long class_id, @PathVariable Long id, Authentication auth) {
	
	ArrayList<TimeTable> teacher = new ArrayList<TimeTable>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql = "SELECT  distinct t.teacher_id, t.teacher_number, name, day, period FROM teacher_class as t "
				+ "left outer join class_time_table as c on t.teacher_number=c.teacher and active =true "
				+ "left outer join user_record as u on t.teacher_id=u.tg_id "
				+ "left outer join period_reference as p on c.period=p.period_type	"
				+ "where not exists(select distinct c.teacher_id from class_time_table as c "
				+ "where t.teacher_number=c.teacher and p.id=? and c.class_id=? and active=true) order by t.teacher_number";


		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, class_id);
		pstmt.setLong(2, id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		TimeTable s = new TimeTable();
		s.setteacher_id(res.getLong("teacher_id"));
		s.setteacher_number(res.getString("teacher_number"));
		s.setname(res.getString("name"));
		s.setday(res.getString("day"));
		s.setperiod(res.getString("period"));

		teacher.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<TimeTable>>(teacher, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/teacher/{date}/teacher_attendance", method = RequestMethod.GET)
public ResponseEntity<List<Teacher_class>> getteacher_list( @PathVariable String date, Authentication auth) {
	
	ArrayList<Teacher_class> teacher = new ArrayList<Teacher_class>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql ="select distinct tc.teacher_id, teacher_number, name, CASE WHEN(ta.teacher_id is null) "
				+ "then true ELSE attendance END as attendance from teacher_class as tc "
				+ "left outer join user_record on tc.teacher_id=tg_id "
				+ "left outer join teacher_attendance as ta on tc.teacher_id=ta.teacher_id and date=? "
				+ "where access_role='T' order by teacher_number";
				
				
				/* "select distinct (tg_id)tg_id, name, teacher_number, attendance, date from user_record "
				+ "left outer join teacher_class as tc on tg_id= teacher_id "
				+ "left outer join teacher_attendance as t on teacher_number=teacher_number "
				+ "where access_role=? order by teacher_number";*/


		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, date);


		res = pstmt.executeQuery();

	while (res.next()) {
		
		Teacher_class s = new Teacher_class();
		s.settg_id(res.getLong("teacher_id"));
		s.setteacher_number(res.getString("teacher_number"));
		s.setname(res.getString("name"));
		s.setattendance(res.getBoolean("attendance"));

		teacher.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Teacher_class>>(teacher, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/teacher/{date}/attendance", method = RequestMethod.PUT)
public String storeAttendanceForPeriod(@PathVariable String date, 
										@RequestBody Teacher_class[] p_teacher, Authentication auth) {
		
	String op = "{ }";
	Connection c = null;
	
	try {

		Long[] teacher_id = new Long[p_teacher.length];
		Boolean[] atts = new Boolean[p_teacher.length];
		
	for (int i = 0; i < p_teacher.length; i++) {
			teacher_id[i] = p_teacher[i].getteacher_id();
			atts[i] = p_teacher[i].getattendance();
			}

		c = DBConnection.getDBConnection();

		String sql = "select teacher_attendance(?, ?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		Array pst = c.createArrayOf("BIGINT", teacher_id);
		pstmt.setArray(1, pst);
		Array patt = c.createArrayOf("BOOLEAN", atts);
		pstmt.setArray(2, patt);
		pstmt.setString(3,date);
		pstmt.setLong(4, p_teacher[0].getcreated_by());
		
		
		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	} 
	catch (Exception e) {
	throw new GenericException("Storing attendance for period failed:" + e.getMessage());
	}
}



@CrossOrigin()
@RequestMapping(value = "/school/{school_id}/bus", method = RequestMethod.GET)
public ResponseEntity<List<Bus>> getbus_list( @PathVariable Long school_id, Authentication auth) {
	
	ArrayList<Bus> bus = new ArrayList<Bus>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql ="select route, route_no from bus where school_id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Bus s = new Bus();
		s.setroute_no(res.getLong("route_no"));
		s.setroute(res.getString("route"));
		
		bus.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Bus>>(bus, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/bus/{route_no}/{date}/student_list", method = RequestMethod.GET)
public ResponseEntity<List<Bus>> getbusstudent_list( @PathVariable Long route_no, @PathVariable String date,  Authentication auth) {
	
	ArrayList<Bus> bus = new ArrayList<Bus>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql ="select tg_id, student_roll_no, name, sc.class_id, u.route_no, attendance from user_record as u "
				+ "left outer join bus as b on b.route_no = u.route_no "
				+ "left outer join student_class  as sc on tg_id = student_id "
				+ "left outer join bus_attendance as ba on u.tg_id = ba.student_id and date=? where b.route_no=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, date);
		pstmt.setLong(2, route_no);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Bus s = new Bus();
		s.settg_id(res.getLong("tg_id"));
		s.setstudent_roll_no(res.getString("student_roll_no"));
		s.setname(res.getString("name"));
		s.setclass_id(res.getLong("class_id"));
		s.setroute_no(res.getLong("route_no"));
		s.setattendance(res.getBoolean("attendance"));

		bus.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Bus>>(bus, HttpStatus.OK);
	}

	catch (Exception e) {
		throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}



@CrossOrigin()
@RequestMapping(value = "/bus/{date}/attendance", method = RequestMethod.PUT)
public String storeAttendanceForPeriod(@PathVariable String date, 
										@RequestBody Bus[] p_bus, Authentication auth) {
		
	String op = "{ }";
	Connection c = null;
	
	try {

		Long[] student_id = new Long[p_bus.length];
		Boolean[] atts = new Boolean[p_bus.length];
		Long[] class_id = new Long[p_bus.length];

		
	for (int i = 0; i < p_bus.length; i++) {
			student_id[i] = p_bus[i].getstudent_id();
			atts[i] = p_bus[i].getattendance();
			class_id[i] = p_bus[i].getclass_id();

			}

		c = DBConnection.getDBConnection();

		String sql = "select bus_attendance(?, ?, ?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		Array pst = c.createArrayOf("BIGINT", student_id);
		pstmt.setArray(1, pst);
		Array patt = c.createArrayOf("BOOLEAN", atts);
		pstmt.setArray(2, patt);
		Array psttt = c.createArrayOf("BIGINT", class_id);
		pstmt.setArray(3, psttt);
		pstmt.setString(4,date);
		pstmt.setLong(5, p_bus[0].getcreated_by());
		
		
		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	} 
	catch (Exception e) {
	throw new GenericException("Storing attendance for period failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/student/{student_id}/bus_attendance", method = RequestMethod.GET)
public ResponseEntity<List<Attendance>> getstaff_list( @PathVariable Long student_id, Authentication auth) {
	
	ArrayList<Attendance> teacher = new ArrayList<Attendance>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql = "select s.student_id, s.attendance as class_attendance, b.attendance as bus_attendance, period, s.date "
				+ "from student_attendance as s full join bus_attendance as b on b.student_id = s.student_id and s.date=b.date "
				+ "left outer join class_time_table as c on s.tt_id=c.id and period='Period 1' and period is not null "
				+ "where (s.student_id=? or b.student_id=?) and "
				+ "((s.attendance=true and b.attendance=false) or (s.attendance=false and b.attendance=true))";


		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, student_id);
		pstmt.setLong(2, student_id);


		res = pstmt.executeQuery();

	while (res.next()) {
		
		Attendance s = new Attendance();
		s.setstudent_id(res.getLong("student_id"));
		s.setclass_attendance(res.getBoolean("class_attendance"));
		s.setbus_attendance(res.getBoolean("bus_attendance"));
		s.setdate(res.getString("date"));
		s.setperiod(res.getString("period"));

		teacher.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Attendance>>(teacher, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/student/{class_id}/attendance", method = RequestMethod.GET)
public ResponseEntity<List<Student>> getcomparestudent_list( @PathVariable Long class_id, Authentication auth) {
	
	ArrayList<Student> teacher = new ArrayList<Student>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql = "select tg_id, name from user_record left outer join student_class on tg_id=student_id"
				+ "	where access_role='S' and route_no is not null and class_id=?";


		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, class_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Student s = new Student();
		s.settg_id(res.getLong("tg_id"));
		s.setname(res.getString("name"));
		
		teacher.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Student>>(teacher, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{teacher_id}/payroll", method = RequestMethod.GET)
public ResponseEntity<List<Payroll>> getstaff_listfortimetable( @PathVariable Long teacher_id, Authentication auth) {
	
	ArrayList<Payroll> teacher = new ArrayList<Payroll>();
	Connection c = null;
	ResultSet res;		
	/*
	System.out.println("Coming to Teacher code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting teacher for class");
	}*/
	try {
		c = DBConnection.getDBConnection();

		String sql = "select date, teacher_id, credited, name from payroll "
				+ "left outer join user_record on tg_id=teacher_id where teacher_id=?";


		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, teacher_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		
		Payroll s = new Payroll();
		s.setteacher_id(res.getLong("teacher_id"));
		s.setdate(res.getString("date"));
		s.setcredited(res.getBoolean("credited"));
		s.setname(res.getString("name"));


		teacher.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Payroll>>(teacher, HttpStatus.OK);
	}
		catch (Exception e) {
			throw new GenericException("Getting standard failed:" + e.getMessage());
			}
		}

@CrossOrigin()
@RequestMapping(value = "/schools/{activity}/events", method = RequestMethod.GET)
public ResponseEntity<List<Events>> getevents(@PathVariable String activity, Authentication auth) {
	
	ArrayList<Events> events = new ArrayList<Events>();
	Connection c = null;
	ResultSet res;

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting Class Details for class");
	}*/
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "select id, activity, name from events where activity=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, activity);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Events s = new Events();
		s.setid(res.getLong("id"));
		s.setactivity(res.getString("activity"));
		s.setname(res.getString("name"));
		
		events.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Events>>(events, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/schools/{activity}/events", method = RequestMethod.POST)
public String addevents( @RequestBody Events et, Authentication auth) {

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem Posting Class Details for class");
	}*/

	try {
		if (et == null) {
			throw new Exception("Request body missing");
		}

		String op = "{ }";
		
		String name = et.getname();
		String activity = et.getactivity();


		Connection c = null;
		c = DBConnection.getDBConnection();

		String sql = "insert into events(activity, name) values (?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, activity);
		pstmt.setString(2, name);

		pstmt.execute();
		pstmt.close();
		c.close();
		return op;

	} 
	catch (Exception e) {
	throw new GenericException("Add standard failed:" + e.getMessage());
	
	}
}

@CrossOrigin()
@RequestMapping(value = "/schools/{id}/events", method = RequestMethod.DELETE)
public String deleteevents( @PathVariable Long id, Authentication auth) {
	
	String op = "{ }";
	Connection c = null;
	
	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE, TargetDomain.CLASS, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem deleting Class Details for class");
	}*/


	try {
		c = DBConnection.getDBConnection();

		String sql = "delete from events where id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Remove standard failed:" + e.getMessage());
	}
}

@CrossOrigin()
@RequestMapping(value = "/schools/{student_id}/student_event", method = RequestMethod.GET)
public ResponseEntity<List<Events>> getstudentevent(@PathVariable Long student_id, Authentication auth) {
	
	ArrayList<Events> events = new ArrayList<Events>();
	Connection c = null;
	ResultSet res;

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting Class Details for class");
	}*/
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "select id, student_id, house_name, activity, name, prize from student_event where student_id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, student_id);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Events s = new Events();
		s.setid(res.getLong("id"));
		s.setstudent_id(res.getLong("student_id"));
		s.setactivity(res.getString("activity"));
		s.setname(res.getString("name"));
		s.sethouse_name(res.getString("house_name"));
		s.setprize(res.getString("prize"));

		events.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Events>>(events, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{student_id}/student_event", method = RequestMethod.PUT)
public String addstudentevent( @PathVariable Long student_id, @RequestBody Events et, Authentication auth) {

		String op = "{ }";
		Connection c = null;
		System.out.println("Coming to Work Updates code" );
		
		if (!SecurityUtilities.verifyAccess(auth, RequestMethod.PUT,  TargetDomain.CLASS_DIARY_UPDATES, TargetEntity.STUDENT, student_id, null, null))
		{
		throw new GenericException("Authorization problem getting Work Updates for class");
		}

	try {
		c = DBConnection.getDBConnection();
		
	String sql = "select events(?, ?, ?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		
		pstmt.setLong(1, student_id);
		pstmt.setString(2, et.gethouse_name());
		pstmt.setString(3, et.getactivity());
		pstmt.setString(4, et.getname());
		pstmt.setString(5, et.getprize());

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Adding work status failed:"	+ e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{id}/student_event", method = RequestMethod.DELETE)
public String deletestudentevents( @PathVariable Long id, Authentication auth) {
	
	String op = "{ }";
	Connection c = null;
	
	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.DELETE, TargetDomain.CLASS, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem deleting Class Details for class");
	}*/


	try {
		c = DBConnection.getDBConnection();

		String sql = "delete from student_event where id=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, id);

		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	}
	catch (Exception e) {
	throw new GenericException("Remove standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{name}/prize_list", method = RequestMethod.GET)
public ResponseEntity<List<Events>> getstudentprize(@PathVariable String name, Authentication auth) {
	
	ArrayList<Events> events = new ArrayList<Events>();
	Connection c = null;
	ResultSet res;

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting Class Details for class");
	}*/
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "select s.student_id, u.name as student_name, class_id, house_name, s.name, prize from student_event  as s "
				+ "left outer join user_record as u on student_id= tg_id "
				+ "left outer join student_class as sc on s.student_id=sc.student_id where s.name=? and prize is not null";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, name);

		res = pstmt.executeQuery();

	while (res.next()) {
		
		Events s = new Events();
		s.setstudent_id(res.getLong("student_id"));
		s.setstudent_name(res.getString("student_name"));
		s.setclass_id(res.getLong("class_id"));
		s.setname(res.getString("name"));
		s.sethouse_name(res.getString("house_name"));
		s.setprize(res.getString("prize"));

		
		events.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Events>>(events, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/schools/{student_id}/{class_id}/{to_date}/event_notification", method = RequestMethod.GET)
		public ResponseEntity<List<Notification>> getstudentevent(@PathVariable Long student_id, @PathVariable String to_date,
																	@PathVariable Long class_id, Authentication auth) {
	
	ArrayList<Notification> events = new ArrayList<Notification>();
	Connection c = null;
	ResultSet res;

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting Class Details for class");
	}*/
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "select school_id, from_date, to_date, title, message from event_notification "
					+ "left outer join student_event on name=title where student_id=? and to_date>=?"
					+ "union "
					+ "select school_id, from_date, to_date, title, message from event_notification as e "
					+ "left outer join student_class as s on s.class_id=e.class_id where student_id=? and e.class_id=? and to_date>=?";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, student_id);
		pstmt.setString(2, to_date);
		pstmt.setLong(3, student_id);
		pstmt.setLong(4, class_id);
		pstmt.setString(5, to_date);
		
		res = pstmt.executeQuery();

	while (res.next()) {
		
		Notification s = new Notification();
		s.setschool_id(res.getLong("school_id"));
		s.setfrom_date(res.getString("from_date"));
		s.setto_date(res.getString("to_date"));
		s.settitle(res.getString("title"));
		s.setmessage(res.getString("message"));
		
		events.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Notification>>(events, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}



@CrossOrigin()
@RequestMapping(value = "/schools/{school_id}/event_notification", method = RequestMethod.POST)
public String addeventnotification( @RequestBody Notification et, Authentication auth) {

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.POST,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem Posting Class Details for class");
	}*/

	try {
		if (et == null) {
			throw new Exception("Request body missing");
		}

		String op = "{ }";
		Long school_id = et.getschool_id();
		String from_date = et.getfrom_date();
		String to_date = et.getto_date();
		String title = et.gettitle();
		String message = et.getmessage();
		Long class_id = et.getclass_id();


		Connection c = null;
		c = DBConnection.getDBConnection();

		String sql = "insert into event_notification(school_id, from_date, to_date, title, message, class_id) values (?, ?, ?, ?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setLong(1, school_id);
		pstmt.setString(2, from_date);
		pstmt.setString(3, to_date);
		pstmt.setString(4, title);
		pstmt.setString(5, message);
		pstmt.setLong(6, class_id);

		pstmt.execute();
		pstmt.close();
		c.close();
		return op;

	} 
	catch (Exception e) {
	throw new GenericException("Add standard failed:" + e.getMessage());
	
	}
}



@CrossOrigin()
@RequestMapping(value = "/schools/{quiz_type}/quiz_structure", method = RequestMethod.GET)
		public ResponseEntity<List<Quiz_structure>> getquizstructure( @PathVariable String quiz_type, Authentication auth) {
	
	ArrayList<Quiz_structure> events = new ArrayList<Quiz_structure>();
	Connection c = null;
	ResultSet res;

	/*System.out.println("Coming to Class Details Code"   );
	
	if (!SecurityUtilities.verifyAccess(auth, RequestMethod.GET,  TargetDomain.SCHOOL, TargetEntity.SCHOOL, school_id, null, null))
	{
	throw new GenericException("Authorization problem getting Class Details for class");
	}*/
	
	try {
		c = DBConnection.getDBConnection();

		String sql = "select title, quiz_type, duration_mins, attempts_allowed, randomized, qq.qid, qq.question_number, question,"
				+ " explanation, answer from quiz_metadata qm left outer join quiz_questions qq on qm.id=qq.qid "
				+ "left outer join quiz_answer_choices qs on qq.question_number = qs.question_number where quiz_type=?";
						
		PreparedStatement pstmt = c.prepareStatement(sql);
		pstmt.setString(1, quiz_type);
		
		res = pstmt.executeQuery();

	while (res.next()) {
		
		Quiz_structure s = new Quiz_structure();
		s.settitle(res.getString("title"));
		s.setquiz_type(res.getString("quiz_type"));
		s.setduration_mins(res.getLong("duration_mins"));
		s.setattempts_allowed(res.getLong("attempts_allowed"));
		s.setrandomized(res.getBoolean("randomized"));
		s.setqid(res.getLong("qid"));
		s.setquestion_number(res.getLong("question_number"));
		s.setquestion(res.getString("question"));
		s.setexplanation(res.getString("explanation"));
		s.setanswer(res.getString("answer"));
		System.out.println("Quiz" + quiz_type );
		events.add(s);
		}

		pstmt.close();
		c.close();
	
		return new ResponseEntity<List<Quiz_structure>>(events, HttpStatus.OK);
	}

	catch (Exception e) {
	throw new GenericException("Getting standard failed:" + e.getMessage());
	}
}


@CrossOrigin()
@RequestMapping(value = "/school/{quiz_type}/quiz_structure", method = RequestMethod.PUT)
public String storequizstructure( @PathVariable String quiz_type, @RequestBody Quiz_structure[] p_quiz, Authentication auth) {
	
		String op = "{ }";
		Connection c = null;
	
		
		
	try {
		String[] answer = new String[p_quiz.length];
		Boolean[] is_right_answer = new Boolean[p_quiz.length];
		
		for (int i = 0; i < p_quiz.length; i++) {
			answer[i] = p_quiz[i].getanswer();
			is_right_answer[i] = p_quiz[i].getis_right_answer();

		}
			c = DBConnection.getDBConnection();

		String sql = "select from quiz_struc(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pstmt = c.prepareStatement(sql);
		
		pstmt.setString(1, p_quiz[0].gettitle());
		pstmt.setString(2, p_quiz[0].getdescription());
		pstmt.setString(3, quiz_type);
		pstmt.setString(4, p_quiz[0].getstart_date());
		pstmt.setString(5, p_quiz[0].getend_date());
		pstmt.setLong(6, p_quiz[0].getduration_mins());
		pstmt.setLong(7, p_quiz[0].getattempts_allowed());
		pstmt.setBoolean(8, p_quiz[0].getrandomized());
		pstmt.setLong(9, p_quiz[0].getquestion_number());
		pstmt.setString(10, p_quiz[0].getquestion());
		pstmt.setLong(11, p_quiz[0].getsequence_number());
		pstmt.setString(12, p_quiz[0].getanswer_type());
		pstmt.setLong(13, p_quiz[0].getcorrect_answer_count());
		pstmt.setString(14, p_quiz[0].getexplanation());
		Array ans = c.createArrayOf("TEXT", answer);
		pstmt.setArray(15, ans);
		Array is = c.createArrayOf("BOOLEAN", is_right_answer);
		pstmt.setArray(16, is);
		
		System.out.println("Quiz" + ans + is );
		pstmt.execute();
		pstmt.close();
		c.close();
	return op;

	} 
	catch (Exception e) {
	throw new GenericException("Storing Quiz failed:" + e.getMessage());
	}
}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ExceptionDetail> myError(HttpServletRequest request,Exception exception) {
		System.out.println("ClassController Exception:"	+ exception.getLocalizedMessage());
		ExceptionDetail error = new ExceptionDetail();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		error.setUrl(request.getRequestURL().toString());
		return new ResponseEntity<ExceptionDetail>(error,
				HttpStatus.BAD_REQUEST);
	}
}
