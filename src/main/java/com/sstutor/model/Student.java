package com.sstutor.model;

public class Student {
Long tg_id;
String name;
String login_key;
String access_role;
Long student_id;
Long class_id;
String student_roll_no;
String section;
Long school_id;
String standard;
String acad_year;
		
		public Long gettg_id() {
			return tg_id;
		}
		public void settg_id(Long tg_id) {
			this.tg_id = tg_id;
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
		public Long getstudent_id() {
			return student_id;
		}
		public void setstudent_id(Long student_id) {
			this.student_id = student_id;
		}
		public Long getclass_id() {
			return class_id;
		}
		public void setclass_id(Long class_id) {
			this.class_id = class_id;
		}
		public String getstudent_roll_no() {
			return student_roll_no;
		}
		public void setstudent_roll_no(String student_roll_no) {
			this.student_roll_no = student_roll_no;
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
