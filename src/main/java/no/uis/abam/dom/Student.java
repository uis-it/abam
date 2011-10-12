package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity(name="Student")
@Inheritance(strategy=InheritanceType.JOINED)
public class Student extends AbamPerson {

  private static final long serialVersionUID = 1L;
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private String studentNumber;
	private String departmentCode;
	private String departmentName;
	private String studyProgramName;
	
	private boolean acceptedThesis;
	
	private Date actualSubmissionOfTopic;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="CUSTOMASSIGNMENT_ID")
	private Assignment customAssignment;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ASSIGNEDTHESIS_ID")
	private Thesis assignedThesis; 
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<Application> applications;

  @Enumerated(EnumType.STRING)
	private AssignmentType type;
	
	public Student(){}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getStudyProgramName() {
		return studyProgramName;
	}

	public void setStudyProgramName(String studyProgramName) {
		this.studyProgramName = studyProgramName;
	}
	
	public Assignment getCustomAssignment() {
		return customAssignment;
	}

	public void setCustomAssignment(Assignment customAssignment) {
		this.customAssignment = customAssignment;
	}
	
	public List<Application> getApplications() {
	  if (applications == null) {
	    applications = new ArrayList<Application>(3);
	  }
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public AssignmentType getType() {
	  return type;
	}

	public void setType(AssignmentType type) {
	  this.type = type;
	}
	
	public Thesis getAssignedThesis() {
		return assignedThesis;
	}

	public void setAssignedThesis(Thesis assignedThesis) {
		this.assignedThesis = assignedThesis;
	}

	public boolean isAcceptedThesis() {
		return acceptedThesis;
	}

	public void setAcceptedThesis(boolean acceptedThesis) {
		this.acceptedThesis = acceptedThesis;
	}

	public Date getActualSubmissionOfTopic() {
		return actualSubmissionOfTopic;
	}
	
	public String getActualSubmissionOfTopicAsString() {
		return simpleDateFormatter.format(actualSubmissionOfTopic);
	}

	public void setActualSubmissionOfTopic(Date actualSubmissionOfTopic) {
		this.actualSubmissionOfTopic = actualSubmissionOfTopic;
	}
}
