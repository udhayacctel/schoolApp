package com.sstutor.model;

public class Master{
	
	Long tg_id;
	Long id;
	String name;
	String login_key;
	String access_role;
	String teacher_number;
	String student_name;
	Long student_class_id;
	String student_standard;
	String student_section;
	Long school_id;
	String acad_year;
	Long teacher_class_id;
	String teacher_standard;
	String teacher_section;
	String student_roll_no;
	Long teacher_id;
	Long student_id;
	String standard;
	String section;
	String student_access_role;
	String student_key;
	Long class_id;
	Long parent_id;
	String user_token;
	String mobile;
	AppRole userRole;
	
	public Master()
	{
		userRole = new AppRole();
	}

	public Long gettg_id() {
		return tg_id;
	}
	public void settg_id(Long tg_id) {
		this.tg_id = tg_id;
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
	public String getlogin_key() {
		return login_key;
	}
	public void setlogin_key(String login_key) {
		this.login_key = login_key;
	}
	public String getaccess_role() {
		return access_role;
	}
	public void setaccess_role(String access_role) {
		this.access_role = access_role;
	}
	public String getteacher_number() {
		return teacher_number;
	}
	public void setteacher_number(String teacher_number) {
		this.teacher_number = teacher_number;
	}
	public String getstudent_name() {
		return student_name;
	}
	public void setstudent_name(String student_name) {
		this.student_name = student_name;
	}
	
	public Long getstudent_class_id() {
		return student_class_id;
	}
	public void setstudent_class_id(Long student_class_id) {
		this.student_class_id = student_class_id;
	}

	public String getstudent_standard() {
		return student_standard;
	}
	public void setstudent_standard(String student_standard) {
		this.student_standard = student_standard;
	}
	public String getstudent_section() {
		return student_section;
	}
	public void setstudent_section(String student_section) {
		this.student_section = student_section;
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
	public Long getteacher_class_id() {
		return teacher_class_id;
	}
	public void setteacher_class_id(Long teacher_class_id) {
		this.teacher_class_id= teacher_class_id;
	}

	public String getteacher_standard() {
		return teacher_standard;
	}
	public void setteacher_standard(String teacher_standard) {
		this.teacher_standard = teacher_standard;
	}
	public String getteacher_section() {
		return teacher_section;
	}
	public void setteacher_section(String teacher_section) {
		this. teacher_section =  teacher_section;
	}
	
	public String getstudent_roll_no() {
		return student_roll_no;
	}
	public void setstudent_roll_no(String student_roll_no) {
		this.student_roll_no = student_roll_no;
	}
	public Long getteacher_id() {
		return teacher_id;
	}
	public void setteacher_id(Long teacher_id) {
		this.teacher_id = teacher_id;
	}
	public Long getstudent_id() {
		return student_id;
	}
	public void setstudent_id(Long student_id) {
		this.student_id = student_id;
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
	public String getstudent_access_role() {
		return student_access_role;
	}
	public void setstudent_access_role(String student_access_role) {
		this.student_access_role = student_access_role;
	}
	public String getstudent_key() {
		return student_key;
	}
	public void setstudent_key(String student_key) {
		this.student_key = student_key;
	}
	public Long getclass_id() {
		return class_id;
	}
	public void setclass_id(Long class_id) {
		this.class_id = class_id;
	}
	public Long getparent_id() {
		return parent_id;
	}
	public void setparent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public String getuser_token() {
		return user_token;
	}
	public void setuser_token(String user_token) {
		this.user_token = user_token;
	}
	public String getmobile() {
		return mobile;
	}
	public void setmobile(String mobile) {
		this.mobile = mobile;
	}
	public AppRole getUserRole() {
		return userRole;
	}

	public void setUserRole(AppRole userRole) {
		this.userRole = userRole;
	}

}