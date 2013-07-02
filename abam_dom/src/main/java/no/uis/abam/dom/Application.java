package no.uis.abam.dom;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="Application")
@Inheritance(strategy=InheritanceType.JOINED)
public class Application extends AbamType {
	
  private static final long serialVersionUID = 1L;

	private int priority;
	
	private boolean assigned;
	
	private String applicantStudentNumber;
	private String coStudentName1;
	private String coStudentName2;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ASSIGNMENT_ID")
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

	public void setApplicationDate(Calendar applicationDate) {
		this.applicationDate = applicationDate;
	}
}
