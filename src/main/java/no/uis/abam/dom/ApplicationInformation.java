package no.uis.abam.dom;

public class ApplicationInformation {

	private Application application;
	
	private String assignmentTitle;
	private String studentName;
	private String coStudent1Name;
	private String coStudent2Name;
	private String facultySupervisor;
	
	private boolean assigned;
	private boolean selected;
	private int priority;
	
	public ApplicationInformation() {
		
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
