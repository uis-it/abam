package no.uis.portal.student.domain;

public abstract class Student extends Person {

	private Assignment customAssignment;
	private String department;
	private String studyProgram;
	
	public Student(){
		
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

	public abstract String getType();
}
