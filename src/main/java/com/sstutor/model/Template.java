package com.sstutor.model;

public class Template {

	Long id;
	String template_message;
	Long school_id;
	String message;
	String template_title;
	String update_ind;

	public Long getid(){
		return id;
	}
	public void setid(Long id){
		this.id=id;
	}
	public String gettemplate_message(){
		return template_message;
	}
	public void settemplate_message(String template_message){
		this.template_message = template_message;
	}
	public Long getschool_id() {
		return school_id;
	}
	public void setschool_id(Long school_id) {
		this.school_id = school_id;
	}
	public String getmessage(){
		return message;
	}
	public void setmessage(String message){
		this.message = message;
	}
	public String gettemplate_title(){
		return template_title;
	}
	public void settemplate_title(String template_title){
		this.template_title = template_title;
	}
	public String getupdate_ind(){
		return update_ind;
	}
	public void setupdate_ind(String update_ind){
		this.update_ind = update_ind;
	}
}
