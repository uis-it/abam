package no.uis.portal.employee;

import no.uis.abam.dom.AbamType;
import no.uis.abam.dom.Application;


public class ApplicationInformation {
	
  private int priority;
	
	private boolean selected;
		
	private String assignmentTitle;
	private String studentName;
	private String coStudent1Name;
	private String coStudent2Name;
	private String facultySupervisor;
	private String studyProgramName;
	
	private Application application;
	
	public ApplicationInformation() {}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getAssignmentTitle() {
		return assignmentTitle;
	}

	public void setAssignmentTitle(String assignmentTitle) {
		this.assignmentTitle = assignmentTitle;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCoStudent1Name() {
		return coStudent1Name;
	}

	public void setCoStudent1Name(String coStudent1Name) {
		this.coStudent1Name = coStudent1Name;
	}

	public String getCoStudent2Name() {
		return coStudent2Name;
	}

	public void setCoStudent2Name(String coStudent2Name) {
		this.coStudent2Name = coStudent2Name;
	}

	public String getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(String facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
	}
	
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getStudyProgramName() {
		return studyProgramName;
	}

	public void setStudyProgramName(String studyProgramName) {
		this.studyProgramName = studyProgramName;
	}


}
