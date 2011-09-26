package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Student extends AbamPerson {

	private static final String MAXIMUM_NUMBER_OF_ASSIGNMENTS_EXCEEDED = 
		"You have applied for the maximum allowed assignments, remove at least one and try again";
	private static final String ASSIGNMENT_ALREADY_APPLIED_FOR = 
		"You have already applied for this assignment";
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private String studentNumber;
	private String departmentCode;
	private String departmentName;
	private String studyProgramName;
	private String applicationsErrorMessage;
	
	private boolean bachelor;
	private boolean acceptedThesis;
	
	private Date actualSubmissionOfTopic;
	
	private Assignment customAssignment;	
	
	private Thesis assignedThesis; 
	
	private Application[] applicationPriorityArray = new Application[3];
	
	public Student(){}

	public void addApplication(Application application) {
		if (applicationIsLegitimate(application)) {	
			placeApplicationInArray(application);
		}
	}
	
	private boolean applicationIsLegitimate(Application application) {
		return !assignmentIsAlreadyAppliedFor(application.getAssignment()) && !isAppliedForThreeAssignments();
	}
	
	public boolean assignmentIsAlreadyAppliedFor(Assignment assignment){
		String assignmentTitle = assignment.getTitle();
		for(int index = 0; index < applicationPriorityArray.length; index++) {
			if(applicationPriorityArray[index] != null) {
				if(applicationPriorityArray[index].getAssignment().getTitle().equals(assignmentTitle)){
					applicationsErrorMessage = ASSIGNMENT_ALREADY_APPLIED_FOR;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isAppliedForThreeAssignments(){
		if ((applicationPriorityArray[0] != null) &&
				(applicationPriorityArray[1] != null) &&
				(applicationPriorityArray[2] != null)) {
			applicationsErrorMessage = MAXIMUM_NUMBER_OF_ASSIGNMENTS_EXCEEDED;
			return true;
		} else {
			return false;
		}
	}
	
	private void placeApplicationInArray(Application application) {
		for (int i = 0; i < applicationPriorityArray.length; i++) {
			if (applicationPriorityArray[i] == null) {
				application.setPriority(i+1);
				applicationPriorityArray[i] = application;
				return;
			}
		}
	}
	
	public boolean isHasAssignedThesis() {
		return assignedThesis != null;
	}
	
	public Application getApplicationFromAssignment(Assignment selectedAssignment){
		for(int index = 0; index < applicationPriorityArray.length; index++){
			if((applicationPriorityArray[index] != null) 
					&& (applicationPriorityArray[index].getAssignment().equals(selectedAssignment))){
				return applicationPriorityArray[index];
			}
		}
		return null;
	}
	
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
	
	public String getApplicationsErrorMessage() {
		return applicationsErrorMessage;
	}

	public void setApplicationsErrorMessage(
			String tooManyApplicationsErrorMessage) {
		this.applicationsErrorMessage = tooManyApplicationsErrorMessage;
	}
	
	public Assignment getCustomAssignment() {
		return customAssignment;
	}

	public void setCustomAssignment(Assignment customAssignment) {
		this.customAssignment = customAssignment;
	}
	
	public Application[] getApplicationPriorityArray() {
		return applicationPriorityArray;
	}

	public void setApplicationPriorityArray(Application[] applicationPriorityArray) {
		this.applicationPriorityArray = applicationPriorityArray;
	}

	public String getType(){
		if(bachelor) return "Bachelor";
		return "Master";
	}

	public boolean isMasterStudent(){
		return this.getType().equals("Master");
	}

	public Thesis getAssignedThesis() {
		return assignedThesis;
	}

	public void setAssignedThesis(Thesis assignedThesis) {
		this.assignedThesis = assignedThesis;
	}

	public boolean isBachelor() {
		return bachelor;
	}

	public void setBachelor(boolean bachelor) {
		this.bachelor = bachelor;
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
	
	@Override
  public boolean equals(Object obj) {
	  if (obj instanceof Student) {
	    Student student = (Student)obj;
	    return this.getStudentNumber().equals(student.getStudentNumber());
	  }
	  return false;
	}
}
