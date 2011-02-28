package no.uis.abam.dom;

import java.util.GregorianCalendar;

public class Application {

	private Assignment assignment;
	
	private int priority;
	
	private String applicant;
	
	private String coStudentName1;
	private String coStudentName2;
	
	private GregorianCalendar applicationDate;
	
	public Application() {
	
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	
	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getCoStudentName1() {
		return coStudentName1;
	}

	public void setCoStudentName1(String coStudentName1) {
		this.coStudentName1 = coStudentName1;
	}

	public String getCoStudentName2() {
		return coStudentName2;
	}

	public void setCoStudentName2(String coStudentName2) {
		this.coStudentName2 = coStudentName2;
	}

	public GregorianCalendar getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(GregorianCalendar applicationDate) {
		this.applicationDate = applicationDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean equals(Application inApplication) {
		return this.getApplicant().equals(inApplication.getApplicant()) 
		&& this.getAssignment().equals(inApplication.getAssignment());
	}
	
}
