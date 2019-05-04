package com.sstutor.model;

public class Teacher_class {

	Long teacher_id;
	Long class_id;
	String teacher_number;
	String name;
	Long tg_id;
	Boolean attendance;
	String date;
	Long created_by;

	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
	public Boolean getattendance() {
		return attendance;
	}
	public void setattendance(Boolean attendance) {
		this.attendance = attendance;
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
	public String getteacher_number() {
		return teacher_number;
	}
	public void setteacher_number(String teacher_number) {
		this.teacher_number = teacher_number;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public Long gettg_id() {
		return tg_id;
	}
	public void settg_id(Long tg_id) {
		this.tg_id = tg_id;
	}
	public Long getcreated_by(){
		return created_by;
	}
	public void setcreated_by(Long created_by){
		this.created_by=created_by;
	}
}
