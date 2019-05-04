package com.sstutor.model;

public class Route {

	Long route_number;
	Long route_id;                        /*Auto Generated Number */
	String route_type;
	String route_name;
	String stop_name;
	Long  stop_num;
	
	public Long getRoute_number() {
		return route_number;
	}
	public void setRoute_number(Long route_number) {
		this.route_number = route_number;
	}
	public Long getRoute_id() {
		return route_id;
	}
	public void setRoute_id(Long route_id) {
		this.route_id = route_id;
	}
	public String getRoute_type() {
		return route_type;
	}
	public void setRoute_type(String route_type) {
		this.route_type = route_type;
	}
	public String getRoute_name() {
		return route_name;
	}
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}
	public String getStop_name() {
		return stop_name;
	}
	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}
	public Long getStop_num() {
		return stop_num;
	}
	public void setStop_num(Long stop_num) {
		this.stop_num = stop_num;
	}
	
}	

