package com.sstutor.model;

public class Exam {

	Long school_id;
	String name;
	String acad_year;
	Long id;

	public Long getid(){
		return id;
	}
	public void setid(Long id){
		this.id=id;
	}
	public Long getschool_id() {
		return school_id;
	}
	public void setschool_id(Long school_id) {
		this.school_id = school_id;
	}
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name= name;
	}
	public String getacad_year() {
		return acad_year;
	}
	public void setacad_year(String acad_year) {
		this.acad_year = acad_year;
	}
}
