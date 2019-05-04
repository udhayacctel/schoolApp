package com.sstutor.model;

//For a student - the student can be mapped to multiple classes across years! So not fair to assume that student has only 1 class id mapping

public class User {
	Long id;
	String email;
	String name;
	String mobile;
	String otp;
	String otp_valid_till;
	Boolean is_login_valid;
	String user_token;
	AppRole userRole;
	Long school_id;
	String url;
	String start_time;
	String end_time;
	
	public User()
	{
		userRole = new AppRole();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getOtp_valid_till() {
		return otp_valid_till;
	}
	public void setOtp_valid_till(String otp_generated_date) {
		this.otp_valid_till = otp_generated_date;
	}
	public Boolean getIs_login_valid() {
		return is_login_valid;
	}
	public void setIs_login_valid(Boolean is_login_valid) {
		this.is_login_valid = is_login_valid;
	}
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Long school_id) {
		this.school_id = school_id;
	}

	public AppRole getUserRole() {
		return userRole;
	}

	public void setUserRole(AppRole userRole) {
		this.userRole = userRole;
	}

	public String geturl() {
		return url;
	}
	public void seturl(String url) {
		this.url = url;
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
}
