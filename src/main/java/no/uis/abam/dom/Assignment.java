package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Assignment implements Comparable<Assignment>{

	public final static int ACTIVE_MONTHS = 6;

	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	private int id;
	private int departmentNumber;
	private int studyProgramNumber;

	private boolean master;
	private boolean bachelor;
	private boolean displayAssignment = true;

	private String title;
	private String description;
	private String studyProgramName;
	private String departmentCode;
	private String departmentName;
	private String type;
	private String numberOfStudents;
	private String numberOfStudentsError;
	private String facultySupervisor;
	private String fileUploadErrorMessage;
	private String attachedFilePath;

	private GregorianCalendar addedDate;
	private GregorianCalendar expireDate;

	private ArrayList<Supervisor> supervisorList;
	private ArrayList<String> attachedFileList;

	public Assignment() {
		bachelor = true;
		type = "Bachelor";

		attachedFileList = new ArrayList<String>();
		supervisorList = new ArrayList<Supervisor>();		
		supervisorList.add(new Supervisor());			
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

	public int getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(int departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public int getStudyProgramNumber() {
		return studyProgramNumber;
	}

	public void setStudyProgramNumber(int studyProgramNumber) {
		this.studyProgramNumber = studyProgramNumber;
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
		return expireDate.before(GregorianCalendar.getInstance());
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

	public String getStudyProgramName() {
		return studyProgramName;
	}

	public void setStudyProgramName(String studyProgramName) {
		this.studyProgramName = studyProgramName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(String numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public String getNumberOfStudentsError() {
		return numberOfStudentsError;
	}

	public void setNumberOfStudentsError(String numberOfStudentsError) {
		this.numberOfStudentsError = numberOfStudentsError;
	}

	public String getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(String facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
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

	public GregorianCalendar getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(GregorianCalendar addedDate) {
		this.addedDate = addedDate;
	}

	public GregorianCalendar getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(GregorianCalendar expireDate) {
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
		return simpleDateFormatter.format(getExpireDate().getTime());
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

}
