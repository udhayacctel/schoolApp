package com.sstutor.model;

public class TimeTable{

	String start_time;
	String end_time;
	String period;
	Boolean attendance;
	String date;
	Long id;
	String day;
	Long class_id;
	Long subject_id;
	Boolean active;
	Long teacher_id;
	Long created_by;
	Long modified_by;
	String created_by_date;
	String modified_by_date;
	String standard;
	String section;
	Long school_id;
	String acad_year;
	String subject;
	String active_till_date;
	String name;
	String teacher_number;
	
	public Long getteacher_id() {
		return teacher_id;
	}
	public void setteacher_id(Long teacher_id) {
		this.teacher_id = teacher_id;
	}
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
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
	public String getperiod() {
		return period;
	}
	public void setperiod(String period) {
		this.period = period;
	}
	public Boolean getattendance() {
		return attendance;
	}
	public void setattendance(Boolean attendance) {
		this.attendance = attendance;
	}
	public Boolean getactive() {
		return active;
	}
	public void setactive(Boolean active) {
		this.active = active;
	}
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
	public String getday() {
		return day;
	}
	public void setday(String day) {
		this.day = day;
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
	public String getsubject() {
		return subject;
	}
	public void setsubject(String subject) {
		this.subject = subject;
	}
	public String getactive_till_date() {
		return active_till_date;
	}
	public void setactive_till_date(String active_till_date) {
		this.active_till_date = active_till_date;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public String getteacher_number() {
		return teacher_number;
	}
	public void setteacher_number(String teacher_number) {
		this.teacher_number = teacher_number;
	}
}
