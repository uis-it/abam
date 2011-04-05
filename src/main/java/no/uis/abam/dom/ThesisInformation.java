package no.uis.abam.dom;

public class ThesisInformation {
	
	private boolean selected;
		
	private String assignmentTitle;
	private String studentName;
	private String coStudent1Name;
	private String coStudent2Name;
	private String externalExaminerName;
	private String evaluationSubmissionDeadlineAsString;
		
	private Thesis thesis;
	
	public ThesisInformation() {}

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

	public Thesis getThesis() {
		return thesis;
	}

	public void setThesis(Thesis thesis) {
		this.thesis = thesis;
	}

	public String getExternalExaminerName() {
		return externalExaminerName;
	}

	public void setExternalExaminerName(String externalExaminerName) {
		this.externalExaminerName = externalExaminerName;
	}

	public String getEvaluationSubmissionDeadlineAsString() {
		return evaluationSubmissionDeadlineAsString;
	}

	public void setEvaluationSubmissionDeadlineAsString(
			String evaluationSubmissionDeadlineAsString) {
		this.evaluationSubmissionDeadlineAsString = evaluationSubmissionDeadlineAsString;
	}
	
}
