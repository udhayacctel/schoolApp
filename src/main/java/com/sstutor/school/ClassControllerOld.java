package com.sstutor.school;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassControllerOld {

	@CrossOrigin()
	@RequestMapping("/getAllClassesForSchool")
	public String getAllClassesForSchool(@RequestParam(value = "schoolid", defaultValue = "0") Long id) {
		System.out.println("Hello there starting:" + id.toString());
		Connection c = null;
		Statement stmt = null;
		ResultSet res;
		String op = "0";
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			stmt = c.createStatement();

			String sql = "select array_to_json(array_agg(row_to_json(t))) as classes from (select id, standard, section, academic_year from school_class_year "
					+ "where school_branch_id = " + id.toString() + ") t;";
			res = stmt.executeQuery(sql);

			while (res.next()) {
				if (res.getString("classes") != null)
					op = res.getString("classes");
				System.out.println("Hello class output:" + op);
			}
			res.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
		}
		return op;
	}

	@CrossOrigin()
	@RequestMapping("/createClassForSchool")
	public String createClassForSchool(@RequestParam Map<String, String> requestParams) {
		int schoolBranchID = 1;
		String standard = requestParams.get("standard");
		String section = requestParams.get("section");
		String academic_year = requestParams.get("academic_year");
		int acad_year = Integer.parseInt(academic_year);
		String op = "{ }";

		System.out.println("Hello there starting create class:" + standard + section + academic_year);

		Connection c = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");

			String sql = "insert into school_class_year (school_branch_id, standard, section, academic_year) "
					+ " values (?,?,?,?)";

			System.out.println("Insert SQL:" + sql);

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setInt(1, schoolBranchID);
			pstmt.setString(2, standard);
			pstmt.setString(3, section);
			pstmt.setInt(4, acad_year);

			pstmt.execute();

			pstmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
			return op;
		}
		return op;
	}

	@CrossOrigin()
	@RequestMapping("/listSubjectsForClass")
	public String listSubjectsForClass(@RequestParam(value = "classid", defaultValue = "0") int classid) {

		String op = "{ }";
		Connection c = null;
		ResultSet res;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			
			String sql = "select array_to_json(array_agg(row_to_json(t))) as subjects from (select cs.id, school_subject_id, subject from class_subject cs, school_subject ss "
					+ "where cs.school_subject_id = ss.id and class_id = ?) t;";
			
			System.out.println("Insert SQL:" + sql);

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setInt(1, classid);

			res = pstmt.executeQuery();
			while (res.next()) {
				if (res.getString("subjects") != null)
					op = res.getString("subjects");
				System.out.println("Hello class subjects output:" + op);
			}
			
			pstmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
			return op;
		}
		return op;
	}
	
	@CrossOrigin()
	@RequestMapping("/delSubjectForClass")
	public String deleteSubjectForClass(@RequestParam(value = "id", defaultValue = "0") int id) {

		String op = "{ }";

		Connection c = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			
			String sql = "delete from class_subject where id = ?";

			System.out.println("Insert SQL:" + sql);

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setInt(1, id);

			pstmt.execute();
			pstmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
			return op;
		}
		return op;
	}
	
	@CrossOrigin()
	@RequestMapping("/addSubjectForClass")
	public String addSubjectForClassl(@RequestParam Map<String, String> requestParams) {
		int classID = Integer.parseInt(requestParams.get("classid"));
		int schoolSubjectID = Integer.parseInt(requestParams.get("school_subject_id"));
		String op = "{ }";

		Connection c = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			

			String sql = "insert into class_subject (class_id, school_subject_id) "
					+ " values (?,?)";

			System.out.println("Insert SQL:" + sql);

			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setInt(1, classID);
			pstmt.setInt(2, schoolSubjectID);

			pstmt.execute();
			pstmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
			return op;
		}
		return op;
	}	
	
	
	
	
}
