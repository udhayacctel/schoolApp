package com.sstutor.model;

public class ClassReferenceTime {

	Long id;
	Long class_id;
	String start_time;
	String end_time;
	String period_type;
	String description;
	String standard;
	String section;
	Long school_id;
	String acad_year;
	
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
	public String getstart_time() {
		return start_time;
	}
	public void setstart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getend_time() {
		return end_time;
	}
	public void setend_time(String end_time) {
		this.end_time = end_time;
	}
	public String getperiod_type() {
		return period_type;
	}
	public void setperiod_type(String period_type) {
		this.period_type = period_type;
	}
	
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
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
	
}
