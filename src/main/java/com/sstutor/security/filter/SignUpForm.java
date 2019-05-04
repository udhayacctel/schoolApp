package com.sstutor.security.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpForm {

	private String mobile;
	private String dob;
	private String momName;
	private String name;
	
	@JsonCreator
    public SignUpForm(@JsonProperty("mobile") String mobile, @JsonProperty("dob") String dob, 
    		@JsonProperty("momname") String momName, @JsonProperty("name") String name)
    {
        this.mobile = mobile;
        this.dob = dob;
        this.momName = momName;
        this.name = name;
    }
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getMomName() {
		return momName;
	}
	public void setMomName(String momName) {
		this.momName = momName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
