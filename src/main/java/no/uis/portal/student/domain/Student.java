package no.uis.portal.student.domain;

public class Student extends Person {

	private Assignment customAssignment;
	private String department;
	private String studyProgram;
	private String type; 
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
