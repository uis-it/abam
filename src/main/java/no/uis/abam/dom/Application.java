package no.uis.abam.dom;

import java.util.GregorianCalendar;

public class Application {

	private Assignment assignment;
	
	private int priority;
	
	private long applicantStudentNumber;
	
	private boolean assigned;
	
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

	public long getApplicantStudentNumber() {
		return applicantStudentNumber;
	}

	public void setApplicantStudentNumber(long applicantStudentNumber) {
		this.applicantStudentNumber = applicantStudentNumber;
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
	
	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public boolean equals(Application inApplication) {
		return this.getApplicantStudentNumber() == inApplication.getApplicantStudentNumber() 
		&& this.getAssignment().equals(inApplication.getAssignment());
	}
	
}
