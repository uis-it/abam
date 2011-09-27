package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Assignment implements Comparable<Assignment>{

	public final static int ACTIVE_MONTHS = 6;

	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	private int id;
	//private int departmentNumber;
	//private int studyProgramNumber;

	private boolean master;
	private boolean bachelor;
	private boolean displayAssignment = true;
	private boolean loggedInUserIsAuthor;
	
	private String title;
	private String description;
	private String studyProgramCode;
	private String departmentCode;
	//private String departmentName;
	private String type;
	private int numberOfStudents;
	private String numberOfStudentsError;
	private String fileUploadErrorMessage;
	private String attachedFilePath;
	
	private AbamPerson author;
	private Employee facultySupervisor;

	private Calendar addedDate;
	private Calendar expireDate;

	private ArrayList<Supervisor> supervisorList;
	private ArrayList<String> attachedFileList;

	public Assignment() {
		bachelor = true;
		type = "Bachelor";

		attachedFileList = new ArrayList<String>();
		supervisorList = new ArrayList<Supervisor>();		
		//supervisorList.add(new Supervisor());	
		
		//facultySupervisor = new Employee();
	}

	public void updateType(String type) {
		setType(type);
		if(type.equalsIgnoreCase("Bachelor")) {
			setBachelor(true);
			setMaster(false);
		}
		else {
			setBachelor(false);
			setMaster(true);
		}
	}

	public int compareTo(Assignment arg0) {
		if (getId() > arg0.getId()) return 1;
		else if (getId() < arg0.getId()) return -1;
		else return 0; 
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

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public boolean isBachelor() {
		return bachelor;
	}

	public void setBachelor(boolean bachelor) {
		this.bachelor = bachelor;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public String getNumberOfStudentsError() {
		return numberOfStudentsError;
	}

	public void setNumberOfStudentsError(String numberOfStudentsError) {
		this.numberOfStudentsError = numberOfStudentsError;
	}

	public String getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
	}

	public String getFileUploadErrorMessage() {
		return fileUploadErrorMessage;
	}

	public void setFileUploadErrorMessage(String fileUploadErrorMessage) {
		this.fileUploadErrorMessage = fileUploadErrorMessage;
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

	public ArrayList<Supervisor> getSupervisorList() {
		return supervisorList;
	}

	public void setSupervisorList(ArrayList<Supervisor> supervisorList) {
		this.supervisorList = supervisorList;
	}

	public ArrayList<String> getAttachedFileList() {
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
