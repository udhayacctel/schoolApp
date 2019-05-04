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
public class SchoolControllerOld {

	@CrossOrigin()
	@RequestMapping("/getAllSubjectsForSchool")
	public String getAllSubjectsForSchool(@RequestParam(value = "schoolid", defaultValue = "1") Long id) {
		System.out.println("Hello there getAllSubjectsForSchool:" + id.toString());
		Connection c = null;
		Statement stmt = null;
		ResultSet res;
		String op = " ";
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			stmt = c.createStatement();

			String sql = "select array_to_json(array_agg(row_to_json(t))) as subjects from (select id, subject from school_subject "
					+ "where school_branch_id = " + id.toString() + ") t;";
			System.out.println(sql);
			res = stmt.executeQuery(sql);

			while (res.next()) {
				if (res.getString("subjects") != null)
					op = res.getString("subjects");
				System.out.println("Hello school subjects output:" + op);
			}
			res.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.out.println("Hello there:" + e.getClass().getName() + ": " + e.getMessage());
		}
		return op;
	}

	/*
	@CrossOrigin()
	@RequestMapping("/createClassForSchool")
	public String createClassForSchool(@RequestParam Map<String, String> requestParams) {
		int schoolBranchID = 1;
		String standard = requestParams.get("standard");
		String section = requestParams.get("section");
		String academic_year = requestParams.get("academic_year");
		int acad_year = Integer.parseInt(academic_year);
		String op = "0";

		System.out.println("Hello there starting create class:" + standard + section + academic_year);

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "odin");
			stmt = c.createStatement();

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
		return "1";
	}
	*/

}
