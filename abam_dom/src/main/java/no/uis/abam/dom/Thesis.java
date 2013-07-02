package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name="Thesis")
@Inheritance(strategy=InheritanceType.JOINED)
public class Thesis extends AbamType {

  private static final long serialVersionUID = 1L;

  @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
  @JoinColumn(name="ASSIGNMENT_ID")
  private Assignment assignment;
	
	private boolean submitted;
	private boolean editExternalExaminer = false;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Attachment> attachments;

	@Transient
	@Deprecated
	private String fileUploadErrorMessage;

  @Transient
  @Deprecated
	private String attachedFilePath;
  
	private String studentNumber1;
	private String studentNumber2;
	private String studentNumber3;
	
	/**
	 * Deadline for accepting the thesis.
	 * The thesis must be accepted by the student after it is assigned by the supervisor.
	 * Accepting a thesis is like signing a contract.  
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	private Calendar acceptionDeadline;

  @Basic
  @Temporal(TemporalType.DATE)
	private Calendar submissionDeadline;

  @Basic
  @Temporal(TemporalType.DATE)
	private Calendar submissionDate;
	
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
	
	@Deprecated
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

	public List<Attachment> getAttachments() {
	  synchronized(this) {
  	  if (attachments == null) {
  	    attachments = new ArrayList<Attachment>();
  	  }
      return attachments;
	  }
  }

  public void setAttachments(List<Attachment> attachments) {
    synchronized(this) {
      this.attachments = attachments;
    }
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

	public Calendar getAcceptionDeadline() {
		return acceptionDeadline;
	}
	
	@Deprecated
	public String getDeadlineForSubmissionOfTopicAsString() {
		return simpleDateFormatter.format(acceptionDeadline.getTime());
	}
	
	public void setAcceptionDeadline(Calendar acceptionDeadline) {
		this.acceptionDeadline = acceptionDeadline;
	}

	public Calendar getSubmissionDeadline() {
		return submissionDeadline;
	}
	
	@Deprecated
	public String getDeadlineForSubmissionForEvalutationAsString() {
		return simpleDateFormatter.format(submissionDeadline.getTime());
	}

	public void setSubmissionDeadline(Calendar deadlineForSubmissionForEvalutation) {
		this.submissionDeadline = deadlineForSubmissionForEvalutation;
	}

	public Calendar getSubmissionDate() {
		return submissionDate;
	}

	@Deprecated
	public String getActualSubmissionForEvalutationAsString() {
		return simpleDateFormatter.format(submissionDate.getTime());
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

	public void setSubmissionDate(Calendar date) {
		this.submissionDate = date;
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

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignedAssignment) {
		this.assignment = assignedAssignment;
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
		Calendar in4Months = Calendar.getInstance();
		in4Months.add(Calendar.MONTH, 4);
		
		return submissionDeadline.before(in4Months);
	}
}
