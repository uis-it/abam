package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name="Assignment")
@Inheritance(strategy=InheritanceType.JOINED)
public class Assignment extends AbamType {

  private static final long serialVersionUID = 1L;

  public final static int ACTIVE_MONTHS = 6;

	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	@Transient
	private boolean displayAssignment = true;
	
	@Transient
	@Deprecated
	private boolean loggedInUserIsAuthor;
	
	private String title;
	private String description;
	private String studyProgramCode;
	private String departmentCode;
	
	@Enumerated(EnumType.STRING)
	private AssignmentType type;
	
	private int numberOfStudents;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="AUTHOR_ID")
	private AbamPerson author;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="FACULTYSUPERVISOR_ID")
	private Employee facultySupervisor;
	
	@Basic
	@Temporal(TemporalType.DATE)
	private Calendar addedDate;
	
  @Basic
  @Temporal(TemporalType.DATE)
	private Calendar expireDate;

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="SUPERVISOR_ID")
	private List<Supervisor> supervisorList;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Attachment> attachments;

  private boolean custom;

  @Transient
  private Object supervisorLock = new Object();

  @Transient
  private Object attachmentsLock = new Object();
	
	public Assignment() {
	}

	public String getStudyProgramCode() {
		return this.studyProgramCode;
	}

	public void setStudyProgramCode(String studyProgramCode) {
		this.studyProgramCode = studyProgramCode;
	}

	@Deprecated
	public boolean isDisplayAssignment() {
		return displayAssignment;
	}

	@Deprecated
	public void setDisplayAssignment(boolean displayAssignment) {
		this.displayAssignment = displayAssignment;
	}

	public boolean isExpired() {
	  if (expireDate != null) {
	    return expireDate.before(GregorianCalendar.getInstance());
	  }
	  return false;
	}

	@Deprecated
	public boolean isLoggedInUserIsAuthor() {
		return loggedInUserIsAuthor;
	}

  @Deprecated
	public void setLoggedInUserIsAuthor(boolean loggedInUserIsAuthor) {
		this.loggedInUserIsAuthor = loggedInUserIsAuthor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AssignmentType getType() {
	  if (type == null) {
	    type = AssignmentType.BACHELOR;
	  }
		return type;
	}

	public void setType(AssignmentType type) {
		this.type = type;
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public Calendar getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Calendar addedDate) {
		this.addedDate = addedDate;
	}

	public Calendar getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Calendar expireDate) {
		this.expireDate = expireDate;
	}

	public List<Supervisor> getSupervisorList() {
	  synchronized (supervisorLock) {
  	  if (supervisorList == null) {
  	    supervisorList = new ArrayList<Supervisor>();
  	  }
  		return supervisorList;
	  }
	}

	public void setSupervisorList(List<Supervisor> supervisorList) {
	  synchronized(supervisorLock) {
	    this.supervisorList = supervisorList;
	  }
	}

	public List<Attachment> getAttachments() {
	  synchronized(attachmentsLock) {
	    if (attachments == null) {
	      attachments = new ArrayList<Attachment>();
	    }
	    return attachments;
	  }
  }

  public void setAttachments(List<Attachment> attachments) {
    synchronized(attachmentsLock) {
      this.attachments = attachments;
    }
  }

	public String getAddedDateAsString() {		
		return simpleDateFormatter.format(getAddedDate().getTime());
	}

	public String getExpireDateAsString() {	
	  if (expireDate != null) {
	    return simpleDateFormatter.format(expireDate.getTime());
	  }
	  return "";
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public AbamPerson getAuthor() {
		return author;
	}

	public void setAuthor(AbamPerson author) {
		this.author = author;
	}

	public Employee getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(Employee facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
	}

  public void setCustom(boolean custom) {
    this.custom = custom;
  }

  public boolean isCustom() {
    return this.custom;
  }
}
