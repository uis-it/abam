package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Thesis {

	private int assignedAssignmentId;
	private long studentNumber1;
	private long studentNumber2;
	private long studentNumber3;
	
	private boolean submitted;
	private boolean editExternalExaminer = false;
	
	private String fileUploadErrorMessage;
	private String attachedFilePath;

	
	private Date deadlineForSubmissionOfTopic;
	private Date deadlineForSubmissionForEvalutation;	
	private Date actualSubmissionForEvalutation;
	
	private ExternalExaminer externalExaminer;

	private List<String> attachedFileList = new ArrayList<String>();
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public Thesis() {
		fileUploadErrorMessage = "The size limit of an attachment is 40MB";
	}

	public int getAssignedAssignmentId() {
		return assignedAssignmentId;
	}

	public void setAssignedAssignmentId(int assignedAssignmentId) {
		this.assignedAssignmentId = assignedAssignmentId;
	}
	
	public void addStudentNumber(long studentNumber) {
		if (studentNumber1 == 0) setStudentNumber1(studentNumber);
		else if (studentNumber2 == 0) setStudentNumber2(studentNumber);
		else if (studentNumber3 == 0) setStudentNumber3(studentNumber);
	}

	public long getStudentNumber1() {
		return studentNumber1;
	}

	public void setStudentNumber1(long studentNumber1) {
		this.studentNumber1 = studentNumber1;
	}

	public long getStudentNumber2() {
		return studentNumber2;
	}

	public void setStudentNumber2(long studentNumber2) {
		this.studentNumber2 = studentNumber2;
	}

	public long getStudentNumber3() {
		return studentNumber3;
	}

	public void setStudentNumber3(long studentNumber3) {
		this.studentNumber3 = studentNumber3;
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

	public Date getActualSubmissionForEvalutation() {
		return actualSubmissionForEvalutation;
	}

	public String getActualSubmissionForEvalutationAsString() {
		return simpleDateFormatter.format(actualSubmissionForEvalutation);
	}
	
	public String getFileUploadErrorMessage() {
		return fileUploadErrorMessage;
	}

	public void setFileUploadErrorMessage(String fileUploadErrorMessage) {
		this.fileUploadErrorMessage = fileUploadErrorMessage;
	}

	public String getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
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
	public boolean equals(Thesis thesis) {
		return this.getAssignedAssignmentId() == thesis
						.getAssignedAssignmentId();
	}

	public List<String> getAttachedFileList() {
		return attachedFileList;
	}

	public void setAttachedFileList(List<String> attachedFileList) {
		this.attachedFileList = attachedFileList;
	}
	
}
