package com.sstutor.model;

import java.util.ArrayList;
import java.util.HashSet;

public class AppRole {
	
	//Depending on the role - teacher/parent the below set of IDs are required
	//A teacher - has access to classes (so list of school_class_year should be present)
	//A parent - has access to students (so list of User objects should be there); and each user object will map to 1 student and within that the approle will contain the list of classIDs!
	//A student - has access to classes (so list of school_class_year should be present)
	
	HashSet<User> students;
	HashSet<School_class_year> classes;
	HashSet<School_constant> schoolconstant;
	
	Boolean parent;
	Boolean student;
	Boolean teacher;
	Boolean admin;
	
	public AppRole()
	{	
		students = new HashSet<User>();
		classes = new HashSet<School_class_year>();
		schoolconstant = new HashSet<School_constant>();
	}
	
	
	
	public Boolean getParent() {
		return parent;
	}
	public void setParent(Boolean parent) {
		this.parent = parent;
	}
	public Boolean getStudent() {
		return student;
	}
	public void setStudent(Boolean student) {
		this.student = student;
	}
	public Boolean getTeacher() {
		return teacher;
	}
	public void setTeacher(Boolean teacher) {
		this.teacher = teacher;
	}
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public HashSet<User> getStudents() {
		return students;
	}

	public void setStudents(HashSet<User> students) {
		this.students = students;
	}

	public HashSet<School_class_year> getClasses() {
		return classes;
	}

	public void setClasses(HashSet<School_class_year> classes) {
		this.classes = classes;
	}
	
	public HashSet<School_constant> getschoolconstant(){
		return schoolconstant;
	}
	
	public void setschoolconstant(HashSet<School_constant> schoolconstant) {
		this.schoolconstant = schoolconstant;
	}
}
