package com.sstutor.model;
public class Attendance {
	
	Long tt_id;
	Boolean attendance;
	Long student_id;
	String date;
	String section;
	Long school_id;
	String standard;
	String acad_year;
	Long created_by;
	Long modified_by;
	String created_by_date;
	String modified_by_date;
    Long id;
    String name;
    String student_roll_no;
	Long class_id;
	String period;
	Boolean class_attendance;
	Boolean bus_attendance;

	public Long getstudent_id() {
		return student_id;
	}
	public void setstudent_id(Long student_id) {
		this.student_id = student_id;
	}
	public Boolean getattendance() {
		return attendance;
	}
	public void setattendance(Boolean attendance) {
		this.attendance = attendance;
	}	
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
	public Long gettt_id() {
		return tt_id;
	}
	public void settt_id(Long tt_id) {
		this.tt_id = tt_id;
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
	public Long getid() {
		return id;
	}
	public void setid(Long id) {
		this.id = id;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public String getstudent_roll_no() {
		return student_roll_no;
	}
	public void setstudent_roll_no(String student_roll_no) {
		this.student_roll_no = student_roll_no;
	}
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public String getperiod() {
		return period;
	}
	public void setperiod(String period) {
		this.period = period;
	}
	public Boolean getclass_attendance() {
		return class_attendance;
	}
	public void setclass_attendance(Boolean class_attendance) {
		this.class_attendance = class_attendance;
	}	
	public Boolean getbus_attendance() {
		return bus_attendance;
	}
	public void setbus_attendance(Boolean bus_attendance) {
		this.bus_attendance = bus_attendance;
	}	
}
