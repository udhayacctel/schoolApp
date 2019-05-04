package com.sstutor.model;

public class Payroll {

	String date;
	Long teacher_id;
	Boolean credited;
	String name;
	
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
	public Long getteacher_id() {
		return teacher_id;
	}
	public void setteacher_id(Long teacher_id) {
		this.teacher_id = teacher_id;
	}
	public Boolean getcredited() {
		return credited;
	}
	public void setcredited(Boolean credited) {
		this.credited = credited;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
}
