package com.sstutor.model;

public class WorkUpdates {

	String status;
	Long class_diary_id;
	String activity;
	Long student_id;
	String standard;
	String section;
	Long school_id;
	String acad_year;
	String name;
	String student_roll_no;
	Long id;
	Boolean teacher_status;

	
	public String getstudent_roll_no() {
		return student_roll_no;
	}
	public void setstudent_roll_no(String student_roll_no) {
		this.student_roll_no = student_roll_no;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public Long getstudent_id(){
		return student_id;
	}
	public void setstudent_id(Long student_id){
		this.student_id=student_id;
	}
	public String getstatus() {
		return status;
	}
	public void setstatus(String status) {
		this.status = status;
	}
	public String getactivity() {
		return activity;
	}
	public void setactivity(String activity) {
		this.activity = activity;
	}
	public Long getclass_diary_id() {
		return class_diary_id;
	}
	public void setclass_diary_id(Long class_diary_id) {
		this.class_diary_id = class_diary_id;
	}
	public String getstandard() {
		return standard;
	}
	public void setstandard(String standard) {
		this.standard = standard;
	}
	public String getsection() {
		return section;
	}
	public void setsection(String section) {
		this.section = section;
	}
	public Long getschool_id() {
		return school_id;
	}
	public void setschool_id(Long school_id) {
		this.school_id = school_id;
	}
	public String getacad_year() {
		return acad_year;
	}
	public void setacad_year(String acad_year) {
		this.acad_year = acad_year;
	}
	public Long getid() {
		return id;
	}
	public void setid(Long id) {
		this.id = id;
	}
	public Boolean getteacher_status() {
		return teacher_status;
	}
	public void setteacher_status(Boolean teacher_status) {
		this.teacher_status = teacher_status;
	}
}
