package com.sstutor.model;
import java.sql.Timestamp;

public class ParentTeacherMeeting {
	
	Long class_id;
	Long student_id;
	String teacher_message;
	String student_message;
	String date;
	Long teacher_id;
	String standard;
	String section;
	String acad_year;
	Long school_id;
	Long created_by;
	Long modified_by;
	//String created_by_date;
	//String modified_by_date;
	Timestamp created_by_date;
	Timestamp modified_by_date;
	
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public Long getstudent_id(){
		return student_id;
	}
	public void setstudent_id(Long student_id){
		this.student_id = student_id;
	}
	public String getstudent_message(){
		return student_message;
	}
	public void setstudent_message(String student_message){
		this.student_message = student_message;
	}
	public String getteacher_message(){
		return teacher_message;
	}
	public void setteacher_message(String teacher_message){
		this.teacher_message = teacher_message;
	}
	public String getdate(){
		return date;
	}
	public void setdate(String date){
		this.date = date;
	}
	public Long getteacher_id(){
		return teacher_id;
	}
	public void setteacher_id(Long teacher_id){
		this.teacher_id = teacher_id;
	}
	public Long getschool_id() {
		return school_id;
	}
	public void setschool_id(Long school_id) {
		this.school_id = school_id;
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
	public Long getcreated_by(){
		return created_by;
	}
	public void setcreated_by(Long created_by){
		this.created_by=created_by;
	}
	public Long getmodified_by() {
		return modified_by;
	}
	public void setmodified_by(Long modified_by) {
		this.modified_by = modified_by;
	}
	public Timestamp getcreated_by_date() {
		return created_by_date;
	}
	public void setcreated_by_date(Timestamp created_by_date) {
		this.created_by_date = created_by_date;
	}
	public Timestamp getmodified_by_date() {
		return modified_by_date;
	}
	public void setmodified_by_date(Timestamp modified_by_date) {
		this.modified_by_date = modified_by_date;
	}
}
