package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Thesis {

	private Assignment assignedAssignment;
	
	private boolean submitted;
	private boolean editExternalExaminer = false;
	
	private String fileUploadErrorMessage;
	private String attachedFilePath;
	private String studentNumber1;
	private String studentNumber2;
	private String studentNumber3;
	
	
	private Date deadlineForSubmissionOfTopic;
	private Date deadlineForSubmissionForEvalutation;	
	private Date actualSubmissionForEvalutation;
	
	private Employee facultySupervisor;
	
	private ExternalExaminer externalExaminer;

	private List<String> attachedFileList = new ArrayList<String>();
	private List<ThesisStatus> statusList = new ArrayList<ThesisStatus>();
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public Thesis() {
		fileUploadErrorMessage = "The size limit of an attachment is 40MB";
	}

	public void addStudentNumber(String studentNumber) {
		if (studentNumber1 == null || studentNumber1.isEmpty()) setStudentNumber1(studentNumber);
		else if (studentNumber2 == null || studentNumber2.isEmpty()) setStudentNumber2(studentNumber);
		else if (studentNumber3 == null || studentNumber3.isEmpty()) setStudentNumber3(studentNumber);
	}

	public String getStudentNumber1() {
		return studentNumber1;
	}

	public void setStudentNumber1(String studentNumber1) {
		this.studentNumber1 = studentNumber1;
	}

	public String getStudentNumber2() {
		return studentNumber2;
	}

	public void setStudentNumber2(String studentNumber2) {
		this.studentNumber2 = studentNumber2;
	}

	public String getStudentNumber3() {
		return studentNumber3;
	}

	public void setStudentNumber3(String studentNumber3) {
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
	
	public Employee getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(Employee facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
	}

	public Assignment getAssignedAssignment() {
		return assignedAssignment;
	}

	public void setAssignedAssignment(Assignment assignedAssignment) {
		this.assignedAssignment = assignedAssignment;
	}

	public List<String> getAttachedFileList() {
		return attachedFileList;
	}

	public void setAttachedFileList(List<String> attachedFileList) {
		this.attachedFileList = attachedFileList;
	}

	public List<ThesisStatus> getStatusList() {		
		return statusList;
	}

	public void setStatusList(List<ThesisStatus> statusList) {		
		this.statusList = statusList;
	}
	
	public void addThesisStatus(ThesisStatus ts) {
		statusList.add(ts);		
	}
	
	public ThesisStatus getLastStatus() {
		return statusList.get(statusList.size()-1);
	}

	public boolean isActive() {
		Date inactiveDate = new Date(getDeadlineForSubmissionForEvalutation().getTime());
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(inactiveDate);
		gc.add(Calendar.MONTH, 4);
		inactiveDate = gc.getTime();
		return inactiveDate.after(GregorianCalendar.getInstance().getTime());
	}
}
