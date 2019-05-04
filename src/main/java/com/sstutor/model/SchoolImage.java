package com.sstutor.model;

public class SchoolImage {

		Long id;
		String url;
		String  base64img;
		Long tg_id;
		Long school_id;
		String file;
		String update_ind;
		 

		public Long getid() {
			return id;
		}
		public void setid(Long id) {
			this.id = id;
		}
		public String geturl() {
			return url;
		}
		public void seturl(String url) {
			this.url = url;
		}
		public String getbase64img() {
			return  base64img;
		}
		public void setbase64img(String base64img) {
			this.base64img = base64img;
		}
		public Long gettg_id() {
			return tg_id;
		}
		public void settg_id(Long tg_id) {
			this.tg_id = tg_id;
		}
		public Long getschool_id() {
			return school_id;
		}
		public void setschool_id(Long school_id) {
			this.school_id = school_id;
		}	
		public String getfile() {
			return  file;
		}
		public void setfile(String file) {
			this.file = file;
		}
		public String getupdate_ind() {
			return update_ind;
		}
		public void setupdate_ind(String update_ind) {
			this.update_ind = update_ind;
		}

}

