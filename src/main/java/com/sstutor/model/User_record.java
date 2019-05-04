package com.sstutor.model;

public class User_record {
	String name;
	String login_key;
	String access_role;
	String teacher_number;
	Long teacher_id;
	Long class_id;
	String student_roll_no;

	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public String getlogin_key() {
		return login_key;
	}
	public void setlogin_key(String login_key) {
		this.login_key = login_key;
	}
	public String getaccess_role() {
		return access_role;
	}
	public void setaccess_role(String access_role) {
		this.access_role = access_role;
	}
	public String getteacher_number() {
		return teacher_number;
	}
	public void setteacher_number(String teacher_number) {
		this.teacher_number = teacher_number;
	}
	public Long getteacher_id() {
		return teacher_id;
	}
	public void setteacher_id(Long teacher_id) {
		this.teacher_id = teacher_id;
	}
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public String getstudent_roll_no() {
		return student_roll_no;
	}
	public void setstudent_roll_no(String student_roll_no) {
		this.student_roll_no = student_roll_no;
	}

}
