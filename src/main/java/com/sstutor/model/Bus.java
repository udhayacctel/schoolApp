package com.sstutor.model;

public class Bus {

	Long route_no;
	String route;
	Long tg_id;
	String name;
	Long student_id;
	Long class_id;
	String student_roll_no;
	Boolean attendance;
	Long created_by;
	String date;


	public String getroute() {
		return route;
	}
	public void setroute(String route) {
		this.route = route;
	}
	public Long getroute_no() {
		return route_no;
	}
	public void setroute_no(Long route_no) {
		this.route_no = route_no;
	}	
	public Long gettg_id() {
		return tg_id;
	}
	public void settg_id(Long tg_id) {
		this.tg_id = tg_id;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public Long getstudent_id() {
		return student_id;
	}
	public void setstudent_id(Long student_id) {
		this.student_id = student_id;
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
	public Boolean getattendance() {
		return attendance;
	}
	public void setattendance(Boolean attendance) {
		this.attendance = attendance;
	}
	public Long getcreated_by(){
		return created_by;
	}
	public void setcreated_by(Long created_by){
		this.created_by=created_by;
	}
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
}
