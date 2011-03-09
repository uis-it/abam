package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Thesis {

	private int assignedAssignmentId;
	private long studentNumber;
		
	private boolean accepted;
	private boolean submitted;
	private boolean editExternalExaminer = false;
	
	private String coStudent1;
	private String coStudent2;
	
	private Date deadlineForSubmissionOfTopic;
	private Date deadlineForSubmissionForEvalutation;	
	private Date actualSubmissionOfTopic;
	private Date actualSubmissionForEvalutation;
	
	private ExternalExaminer externalExaminer;

	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public Thesis() {
		
	}

	public int getAssignedAssignmentId() {
		return assignedAssignmentId;
	}

	public void setAssignedAssignmentId(int assignedAssignmentId) {
		this.assignedAssignmentId = assignedAssignmentId;
	}

	public long getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(long studentNumber) {
		this.studentNumber = studentNumber;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	public boolean isSubmitted() {
		return submitted;
	}

	public boolean isEditExternalExaminer() {
		return editExternalExaminer;
	}

	public void setEditExternalExaminer(boolean editExternalExaminer) {
		this.editExternalExaminer = editExternalExaminer;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public String getCoStudent1() {
		return coStudent1;
	}

	public void setCoStudent1(String coStudent1) {
		this.coStudent1 = coStudent1;
	}

	public String getCoStudent2() {
		return coStudent2;
	}

	public void setCoStudent2(String coStudent2) {
		this.coStudent2 = coStudent2;
	}

	public Date getDeadlineForSubmissionOfTopic() {
		return deadlineForSubmissionOfTopic;
	}
	
	public String getDeadlineForSubmissionOfTopicAsString() {
		return simpleDateFormatter.format(deadlineForSubmissionOfTopic);
	}
	
	public void setDeadlineForSubmissionOfTopic(Date deadlineForSubmissionOfTopic) {
		this.deadlineForSubmissionOfTopic = deadlineForSubmissionOfTopic;
	}

	public Date getDeadlineForSubmissionForEvalutation() {
		return deadlineForSubmissionForEvalutation;
	}
	
	public String getDeadlineForSubmissionForEvalutationAsString() {
		return simpleDateFormatter.format(deadlineForSubmissionForEvalutation);
	}

	public void setDeadlineForSubmissionForEvalutation(
			Date deadlineForSubmissionForEvalutation) {
		this.deadlineForSubmissionForEvalutation = deadlineForSubmissionForEvalutation;
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

	public Date getActualSubmissionForEvalutation() {
		return actualSubmissionForEvalutation;
	}

	public String getActualSubmissionForEvalutationAsString() {
		return simpleDateFormatter.format(actualSubmissionForEvalutation);
	}
	
	public void setActualSubmissionForEvalutation(
			Date actualSubmissionForEvalutation) {
		this.actualSubmissionForEvalutation = actualSubmissionForEvalutation;
	}
	
	public ExternalExaminer getExternalExaminer() {
		return externalExaminer;
	}

	public void setExternalExaminer(ExternalExaminer externalExaminer) {
		this.externalExaminer = externalExaminer;
	}

}
