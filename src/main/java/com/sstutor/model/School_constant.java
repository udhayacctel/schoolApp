package com.sstutor.model;
import java.sql.Timestamp;

public class School_constant {
Long field_id;
String field_name;
String field_value;
Boolean active;
Long school_id;
String start_time;
String end_time;

public Long getfield_id() {
	return field_id;
}
public void setfield_id(Long field_id) {
	this.field_id = field_id;
}
public String getfield_name() {
	return field_name;
}
public void setfield_name(String field_name) {
	this.field_name = field_name;
}
public String getfield_value() {
	return field_value;
}
public void setfield_value(String field_value) {
	this.field_value = field_value;
}
public Boolean getactive() {
	return active;
}
public void setactive(Boolean active) {
	this.active = active;
}
public Long getschool_id() {
	return school_id;
}
public void setschool_id(Long school_id) {
	this.school_id = school_id;
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
