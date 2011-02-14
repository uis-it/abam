package no.uis.portal.student.domain;

public abstract class Student extends Person {

	private Assignment customAssignment;
	private String department;
	private String studyProgram;
	private String applicationsErrorMessage;
	
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
		if(!assignmentIsAlreadyAppliedFor(application.getAssignment())) {
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
			if(isAppliedForThreeAssignments()) 
				applicationsErrorMessage = "You have the maximum allowed applications, remove at least one and try again";

		} else applicationsErrorMessage = "You have already applied for this assignment";
		
	}
	
	public boolean assignmentIsAlreadyAppliedFor(Assignment assignment){
		String assignmentTitle = assignment.getTitle();
		for(int index = 0; index < applicationPriorityArray.length; index++) {
			if(applicationPriorityArray[index] != null) {
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
	
	public void removeApplication(Application application) {
		for (int index = 0; index < applicationPriorityArray.length; index++) {
			if(applicationPriorityArray[index] == application) 
				applicationPriorityArray[index] = null;
		}
	}
	
	public void moveApplicationHigher(Application selectedApplication) {
		int selectedApplicationIndex = findApplicationIndex(selectedApplication);
		if(selectedApplicationIndex != 0) {
			int higherApplicationIndex = selectedApplicationIndex - 1;
			Application higherApplication = applicationPriorityArray[higherApplicationIndex];
			higherApplication.setPriority(selectedApplicationIndex + 1);
			selectedApplication.setPriority(higherApplicationIndex + 1);
			applicationPriorityArray[higherApplicationIndex] = selectedApplication;
			applicationPriorityArray[selectedApplicationIndex] = higherApplication;
		}
	}
	public void moveApplicationLower(Application selectedApplication) {
		int selectedApplicationIndex = findApplicationIndex(selectedApplication);
		if(selectedApplicationIndex != 2) {
			int lowerApplicationIndex = selectedApplicationIndex + 1;
			Application lowerApplication = applicationPriorityArray[lowerApplicationIndex];
			if(lowerApplication != null) {
				lowerApplication.setPriority(selectedApplicationIndex + 1);
				selectedApplication.setPriority(lowerApplicationIndex + 1);
				applicationPriorityArray[lowerApplicationIndex] = selectedApplication;
				applicationPriorityArray[selectedApplicationIndex] = lowerApplication;
			}
		}
	}
	
	
	private int findApplicationIndex(Application application) {
		for (int index = 0; index < applicationPriorityArray.length; index++) {
			if(applicationPriorityArray[index] == application) return index;
		}
		return -1;
	}
	public Application getApplicationFromAssignment(Assignment selectedAssignment){
		for(int index = 0; index < applicationPriorityArray.length; index++){
			if(applicationPriorityArray[index].getAssignment() == selectedAssignment){
				return applicationPriorityArray[index];
			}
		}
		return null;
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
