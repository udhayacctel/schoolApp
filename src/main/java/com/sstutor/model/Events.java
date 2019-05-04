package com.sstutor.model;

public class Events {
	Long id;
	Long student_id;
	String house_name;
	String activity;
	String name;
	String prize;
	Long class_id;
	String student_name;

	
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
	public Long getstudent_id() {
		return student_id;
	}
	public void setstudent_id(Long student_id) {
		this.student_id = student_id;
	}
	public String gethouse_name() {
		return house_name;
	}
	public void sethouse_name(String house_name) {
		this.house_name = house_name;
	}
	public String getactivity() {
		return activity;
	}
	public void setactivity(String activity) {
		this.activity = activity;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public String getprize() {
		return prize;
	}
	public void setprize(String prize) {
		this.prize = prize;
	}
	public String getstudent_name() {
		return student_name;
	}
	public void setstudent_name(String student_name) {
		this.student_name = student_name;
	}}
