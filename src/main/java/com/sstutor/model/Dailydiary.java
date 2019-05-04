package com.sstutor.model;

public class Dailydiary {
	
	Long id;
	Long class_id;
	Long subject_id;
	String end_date;
	String title;
	String message;
	String activity;
	String standard;
	String section;
	String acad_year;
	String subject;
	Long school_id;
	Long created_by;
	Long modified_by;
	String created_by_date;
	String modified_by_date;
	String status;
	Long student_id;
	Boolean coordinator_update;
	
	public Long getid() {
		return id;
	}
	public void setid(Long id) {
		this.id = id;
	}
	
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public Long getsubject_id() {
		return subject_id;
	}
	public void setsubject_id(Long subject_id) {
		this.subject_id = subject_id;
	}
	public String getend_date() {
		return end_date;
	}
	public void setend_date(String end_date) {
		this.end_date = end_date;
	}
	public String gettitle(){
		return title;
	}
	public void settitle(String title){
		this.title = title;
	}
	
	public String getmessage(){
		return message;
	}
	public void setmessage(String message){
		this.message = message;
	}
	public String getactivity() {
		return activity;
	}
	public void setactivity(String activity) {
		this.activity = activity;
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
	public String getacad_year() {
		return acad_year;
	}
	public void setacad_year(String acad_year) {
		this.acad_year = acad_year;
	}
	public String getsubject() {
		return subject;
	}
	public void setsubject(String subject) {
		this.subject = subject;
	}
	public Long getschool_id() {
		return school_id;
	}
	public void setschool_id(Long school_id) {
		this.school_id = school_id;
	}
	public Long getcreated_by() {
		return created_by;
	}
	public void setcreated_by(Long created_by) {
		this.created_by = created_by;
	}
	public Long getmodified_by() {
		return modified_by;
	}
	public void setmodified_by(Long modified_by) {
		this.modified_by = modified_by;
	}
	public String getcreated_by_date() {
		return created_by_date;
	}
	public void setcreated_by_date(String created_by_date) {
		this.created_by_date = created_by_date;
	}
	public String getmodified_by_date() {
		return modified_by_date;
	}
	public void setmodified_by_date(String modified_by_date) {
		this.modified_by_date = modified_by_date;
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
	public Boolean getcoordinator_update() {
		return coordinator_update;
	}
	public void setcoordinator_update(Boolean coordinator_update) {
		this.coordinator_update = coordinator_update;
	}

}
