package com.sstutor.model;

public class Quiz_structure {
	
	String title;
	String description;
	String quiz_type;
	String start_date;
	String end_date;
	Long duration_mins;
	Long attempts_allowed;
	Boolean randomized;
	Long question_number;
	String question;
	Long sequence_number;
	String answer_type;
	Long correct_answer_count;
	String explanation;
	String answer;
	Boolean is_right_answer;
	Long qid;
	Long user_id;
	String start_time;
	String submit_time;
	Long score;
	Long attempt_id;


	public String gettitle() {
		return title;
	}
	public void settitle(String title) {
		this.title = title;
	}	
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
	}
	public String getquiz_type() {
		return quiz_type;
	}
	public void setquiz_type(String quiz_type) {
		this.quiz_type = quiz_type;
	}
	public String getstart_date() {
		return start_date;
	}
	public void setstart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getend_date() {
		return end_date;
	}
	public void setend_date(String end_date) {
		this.end_date = end_date;
	}
	public Long getduration_mins() {
		return duration_mins;
	}
	public void setduration_mins(Long duration_mins) {
		this.duration_mins = duration_mins;
	}
	public Long getattempts_allowed() {
		return attempts_allowed;
	}
	public void setattempts_allowed(Long attempts_allowed) {
		this.attempts_allowed = attempts_allowed;
	}
	public Boolean getrandomized() {
		return randomized;
	}
	public void setrandomized(Boolean randomized) {
		this.randomized = randomized;
	}	
	
	public Long getquestion_number() {
		return question_number;
	}
	public void setquestion_number(Long question_number) {
		this.question_number = question_number;
	}
	public String getquestion() {
		return question;
	}
	public void setquestion(String question) {
		this.question = question;
	}
	public Long getsequence_number() {
		return sequence_number;
	}
	public void setsequence_number(Long sequence_number) {
		this.sequence_number = sequence_number;
	}
	public String getanswer_type() {
		return answer_type;
	}
	public void setanswer_type(String answer_type) {
		this.answer_type = answer_type;
	}
	public Long getcorrect_answer_count() {
		return correct_answer_count;
	}
	public void setcorrect_answer_count(Long correct_answer_count) {
		this.correct_answer_count = correct_answer_count;
	}
	public String getexplanation() {
		return explanation;
	}
	public void setexplanation(String explanation) {
		this.explanation = explanation;
	}	
	public String getanswer() {
		return answer;
	}
	public void setanswer(String answer) {
		this.answer = answer;
	}
	public Boolean getis_right_answer() {
		return is_right_answer;
	}
	public void setis_right_answer(Boolean is_right_answer) {
		this.is_right_answer = is_right_answer;
	}
	public Long getqid() {
		return qid;
	}
	public void setqid(Long qid) {
		this.qid = qid;
	}
	public Long getuser_id() {
		return user_id;
	}
	public void setuser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getstart_time() {
		return start_time;
	}
	public void setstart_time(String start_time) {
		this.start_time = start_time;
	}	
	public String getsubmit_time() {
		return submit_time;
	}
	public void setsubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}	
	public Long getscore() {
		return score;
	}
	public void setscore(Long score) {
		this.score = score;
	}
	public Long getattempt_id() {
		return attempt_id;
	}
	public void setattempt_id(Long attempt_id) {
		this.attempt_id = attempt_id;
	}
}