package no.uis.abam.dom;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Application {
	
	private int priority;
	
	private boolean assigned;
	
	private String applicantStudentNumber;
	private String coStudentName1;
	private String coStudentName2;
	
	private Assignment assignment;
	private Calendar applicationDate;
	
	public Application() {}

	public Assignment getAssignment() {
		return assignment;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String getApplicantStudentNumber() {
		return applicantStudentNumber;
	}

	public void setApplicantStudentNumber(String applicantStudentNumber) {
		this.applicantStudentNumber = applicantStudentNumber;
	}
	
	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
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

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	
	public Calendar getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(GregorianCalendar applicationDate) {
		this.applicationDate = applicationDate;
	}
	
	public boolean equals(Application inApplication) {
		return this.getApplicantStudentNumber().equals(inApplication.getApplicantStudentNumber()) 
		&& this.getAssignment().equals(inApplication.getAssignment());
	}
	
}
