package com.sstutor.security.config;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

//Taking a variation of SimpleGrantedAuthority that is provided by default
public final class ModifiedGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String role;
	private HashSet<Long> classIDs;
	private HashSet<Long> studentIDs;
	private HashSet<Long> teacherIDs;

	public ModifiedGrantedAuthority(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}

	public String getAuthority() {
		return role;
	}

	/*
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SimpleGrantedAuthority) {
			return role.equals(((SimpleGrantedAuthority) obj).role);
		}

		return false;
	}*/

	public int hashCode() {
		return this.role.hashCode();
	}

	public String toString() {
		return this.role;
	}

	public HashSet<Long> getClassIDs() {
		return classIDs;
	}

	public void setClassIDs(HashSet<Long> classIDs) {
		this.classIDs = classIDs;
	}

	public HashSet<Long> getStudentIDs() {
		return studentIDs;
	}

	public void setStudentIDs(HashSet<Long> studentIDs) {
		this.studentIDs = studentIDs;
	}

	public HashSet<Long> getTeacherIDs() {
		return teacherIDs;
	}

	public void setTeacherIDs(HashSet<Long> teacherIDs) {
		this.teacherIDs = teacherIDs;
	}
}
