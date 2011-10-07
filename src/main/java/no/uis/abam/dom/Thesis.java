package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity(name="Thesis")
@Inheritance(strategy=InheritanceType.JOINED)
public class Thesis extends AbamType {

  private static final long serialVersionUID = 1L;

  @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
  @JoinColumn(name="ASSIGNMENT_ID")
  private Assignment assignedAssignment;
	
	private boolean submitted;
	private boolean editExternalExaminer = false;

	@Transient
	@Deprecated
	private String fileUploadErrorMessage;

  @Transient
  @Deprecated
	private String attachedFilePath;
  
	private String studentNumber1;
	private String studentNumber2;
	private String studentNumber3;
	
	
	private Date deadlineForSubmissionOfTopic;
	private Date deadlineForSubmissionForEvalutation;	
	private Date actualSubmissionForEvalutation;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="FACULTYSUPERVISOR_ID")
	private Employee facultySupervisor;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="EXTERNALEXAMINER_ID")
	private ExternalExaminer externalExaminer;

	@Transient
	@Deprecated
	private List<String> attachedFileList;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ThesisStatus> statusList;
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public Thesis() {
		fileUploadErrorMessage = "The size limit of an attachment is 40MB";
	}

	public void addStudentNumber(String studentNumber) {
		if (studentNumber1 == null || studentNumber1.isEmpty()) {
		  setStudentNumber1(studentNumber);
		} else if (studentNumber2 == null || studentNumber2.isEmpty()) {
		  setStudentNumber2(studentNumber);
		} else if (studentNumber3 == null || studentNumber3.isEmpty()) {
		  setStudentNumber3(studentNumber);
		}
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
	
	@Deprecated
	public String getFileUploadErrorMessage() {
		return fileUploadErrorMessage;
	}

  @Deprecated
	public void setFileUploadErrorMessage(String fileUploadErrorMessage) {
		this.fileUploadErrorMessage = fileUploadErrorMessage;
	}

  @Deprecated
	public String getAttachedFilePath() {
		return attachedFilePath;
	}

  @Deprecated
	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
	}

	public void setActualSubmissionForEvalutation(Date date) {
		this.actualSubmissionForEvalutation = date;
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

  @Deprecated
	public List<String> getAttachedFileList() {
    if (attachedFileList == null) {
      attachedFileList = new ArrayList<String>();
    }
		return attachedFileList;
	}

  @Deprecated
	public void setAttachedFileList(List<String> attachedFileList) {
		this.attachedFileList = attachedFileList;
	}

	public List<ThesisStatus> getStatusList() {
	  if (statusList == null) {
	    statusList = new ArrayList<ThesisStatus>();
	  }
		return statusList;
	}

	public void setStatusList(List<ThesisStatus> statusList) {		
		this.statusList = statusList;
	}
	
	@Deprecated
	public void addThesisStatus(ThesisStatus ts) {
		getStatusList().add(ts);		
	}
	
	@Deprecated
	public ThesisStatus getLastStatus() {
		return getStatusList().get(statusList.size()-1);
	}

	@Deprecated
	public boolean isActive() {
		Date inactiveDate = new Date(getDeadlineForSubmissionForEvalutation().getTime());
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(inactiveDate);
		gc.add(Calendar.MONTH, 4);
		inactiveDate = gc.getTime();
		return inactiveDate.after(GregorianCalendar.getInstance().getTime());
	}
}
