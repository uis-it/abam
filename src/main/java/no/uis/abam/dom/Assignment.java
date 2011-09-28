package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Assignment implements Comparable<Assignment> {

	public final static int ACTIVE_MONTHS = 6;

	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	private int id;

	private boolean displayAssignment = true;
	private boolean loggedInUserIsAuthor;
	
	private String title;
	private String description;
	private String studyProgramCode;
	private String departmentCode;
	private AssignmentType type;
	private int numberOfStudents;
	private String numberOfStudentsError;
	private String attachedFilePath;
	
	private AbamPerson author;
	private Employee facultySupervisor;

	private Calendar addedDate;
	private Calendar expireDate;

	private ArrayList<Supervisor> supervisorList;
	private ArrayList<String> attachedFileList;

	public Assignment() {
	}

	public int compareTo(Assignment arg0) {
		if (getId() > arg0.getId()) {
		  return 1;
		} else if (getId() < arg0.getId()) {
		  return -1;
		} else {
		  return 0; 
		}
	}

	public boolean equals (Assignment ab) {
		return ab.getId() == this.getId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudyProgramCode() {
		return this.studyProgramCode;
	}

	public void setStudyProgramCode(String studyProgramCode) {
		this.studyProgramCode = studyProgramCode;
	}

	public boolean isDisplayAssignment() {
		return displayAssignment;
	}

	public void setDisplayAssignment(boolean displayAssignment) {
		this.displayAssignment = displayAssignment;
	}

	public boolean isExpired() {
	  if (expireDate != null) {
	    return expireDate.before(GregorianCalendar.getInstance());
	  }
	  return false;
	}

	public boolean isLoggedInUserIsAuthor() {
		return loggedInUserIsAuthor;
	}

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

	public String getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
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
	  if (supervisorList == null) {
	    supervisorList = new ArrayList<Supervisor>();
	  }
		return supervisorList;
	}

	public void setSupervisorList(ArrayList<Supervisor> supervisorList) {
		this.supervisorList = supervisorList;
	}

	public List<String> getAttachedFileList() {
	  if (attachedFileList == null) {
	    attachedFileList = new ArrayList<String>();
	  }
		return attachedFileList;
	}

	public void setAttachedFileList(ArrayList<String> attachedFileList) {
		this.attachedFileList = attachedFileList;
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

}
