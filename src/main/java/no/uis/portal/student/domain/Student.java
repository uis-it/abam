package no.uis.portal.student.domain;

public abstract class Student extends Person {

	private Assignment customAssignment;
	private String department;
	private String studyProgram;
	private String applicationsErrorMessage;
	
	private boolean maximumAllowedApplications;
	
	private Application[] applicationPriorityArray = new Application[3];
	
	public Student(){
		isAppliedForThreeAssignments();
	}

	public Assignment getCustomAssignment() {
		return customAssignment;
	}

	public void setCustomAssignment(Assignment customAssignment) {
		this.customAssignment = customAssignment;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(String studyProgram) {
		this.studyProgram = studyProgram;
	}
	
	public boolean isMasterStudent(){
		return this.getType().equals("Master");
	}
	
	public void addApplication(Application application) {
		//System.out.println("Resultat =: "+assignmentIsAlreadyAppliedFor(application));
		if(!assignmentIsAlreadyAppliedFor(application)) {
			if(applicationPriorityArray[0] == null) {
				application.setPriority(1);
				applicationPriorityArray[0] = application;
			}
			else if(applicationPriorityArray[1] == null) {
				application.setPriority(2);
				applicationPriorityArray[1] = application;
			}
			else if(applicationPriorityArray[2] == null) { 
				application.setPriority(3);
				applicationPriorityArray[2] = application;
			}
			else {
				applicationsErrorMessage = "You have the maximum allowed applications, remove at least one and try again";
				maximumAllowedApplications = true;
			}
		} else applicationsErrorMessage = "You have already applied for this assignment";
		
	}
	
	private boolean assignmentIsAlreadyAppliedFor(Application application){
		String assignmentTitle = application.getAssignment().getTitle();
		for(int index = 0; index < applicationPriorityArray.length; index++) {
			if(applicationPriorityArray[index] != null) {
				System.out.println("Index "+index+": "+applicationPriorityArray[index].getAssignment().getTitle() +" vs "+application.getAssignment().getTitle());
				if(applicationPriorityArray[index].getAssignment().getTitle().equals(assignmentTitle)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isAppliedForThreeAssignments(){
		return (applicationPriorityArray[0] != null) &&
				(applicationPriorityArray[1] != null) &&
				(applicationPriorityArray[2] != null);
	}
	
	public String getApplicationsErrorMessage() {
		return applicationsErrorMessage;
	}

	public void setApplicationsErrorMessage(
			String tooManyApplicationsErrorMessage) {
		this.applicationsErrorMessage = tooManyApplicationsErrorMessage;
	}
	
	public Application[] getApplicationPriorityArray() {
		return applicationPriorityArray;
	}

	public void setApplicationPriorityArray(Application[] applicationPriorityArray) {
		this.applicationPriorityArray = applicationPriorityArray;
	}

	public abstract String getType();
}
