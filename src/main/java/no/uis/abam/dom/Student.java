package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student extends AbamPerson {

  private static final long serialVersionUID = 1L;
//  private static final String MAXIMUM_NUMBER_OF_ASSIGNMENTS_EXCEEDED = 
//		"You have applied for the maximum allowed assignments, remove at least one and try again";
//	private static final String ASSIGNMENT_ALREADY_APPLIED_FOR = 
//		"You have already applied for this assignment";
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private String studentNumber;
	private String departmentCode;
	private String departmentName;
	private String studyProgramName;
	
	private boolean acceptedThesis;
	
	private Date actualSubmissionOfTopic;
	
	private Assignment customAssignment;	
	
	private Thesis assignedThesis; 
	
	private List<Application> applications;
  private AssignmentType type;
	
	public Student(){}

	private void addApplication(Application application) {
		if (applicationIsLegitimate(application)) {	
			placeApplicationInArray(application);
		}
	}
	
	private boolean applicationIsLegitimate(Application application) {
		return !assignmentIsAlreadyAppliedFor(application.getAssignment()) && !isAppliedForThreeAssignments();
	}
	
	private boolean assignmentIsAlreadyAppliedFor(Assignment assignment){
		String assignmentTitle = assignment.getTitle();
//		for(int index = 0; index < applicationPriorityArray.length; index++) {
//			if(applicationPriorityArray[index] != null) {
//				if(applicationPriorityArray[index].getAssignment().getTitle().equals(assignmentTitle)){
//					//applicationsErrorMessage = ASSIGNMENT_ALREADY_APPLIED_FOR;
//					return true;
//				}
//			}
//		}
		return false;
	}
	
	private boolean isAppliedForThreeAssignments(){
//		if ((applicationPriorityArray[0] != null) &&
//				(applicationPriorityArray[1] != null) &&
//				(applicationPriorityArray[2] != null)) {
//			//applicationsErrorMessage = MAXIMUM_NUMBER_OF_ASSIGNMENTS_EXCEEDED;
//			return true;
//		} else {
			return false;
//		}
	}
	
	private void placeApplicationInArray(Application application) {
//		for (int i = 0; i < applicationPriorityArray.length; i++) {
//			if (applicationPriorityArray[i] == null) {
//				application.setPriority(i+1);
//				applicationPriorityArray[i] = application;
//				return;
//			}
//		}
	}
	
	public boolean isHasAssignedThesis() {
		return assignedThesis != null;
	}
	
	/**
	 * TODO move somewhere else
	 */
	@Deprecated
	public Application getApplicationFromAssignment(Assignment selectedAssignment) {
	  
	  for (Application appl : getApplications()) {
      if (appl.getAssignment().equals(selectedAssignment)) {
        return appl;
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
	
	@Override
  public boolean equals(Object obj) {
	  if (obj instanceof Student) {
	    Student student = (Student)obj;
	    return this.getStudentNumber().equals(student.getStudentNumber());
	  }
	  return false;
	}
}
