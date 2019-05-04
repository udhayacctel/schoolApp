package com.sstutor.model;

public class Notification {
    
	Long id;
    String day;
    String from_date;
    String to_date;
    String title;
    String message;
    Long school_id;
    String recepient;
    Long created_by;
    Long modified_by;
	String created_by_date;
	String modified_by_date;
	Long class_id;
    
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public Long getid(){
		return id;
	}
	public void setid(Long id){
		this.id=id;
	}
	public String getmessage(){
		return message;
	}
	public void setmessage(String message){
		this.message = message;
	}
	public String gettitle(){
		return title;
	}
	public void settitle(String title){
		this.title = title;
	}
	public Long getschool_id(){
		return school_id;
		}
	public void setschool_id(Long school_id){
		this.school_id=school_id;
	}
	public String getfrom_date(){
			return from_date;
	}
	public void setfrom_date(String from_date){
			this.from_date = from_date;
	}
	public String getto_date(){
		return to_date;
	}
	public void setto_date(String to_date){
		this.to_date = to_date;
	}
	public String getrecepient(){
		return recepient;
	}
	public void setrecepient(String recepient){
		this.recepient=recepient;
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
	
}
