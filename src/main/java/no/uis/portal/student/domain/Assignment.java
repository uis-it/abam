package no.uis.portal.student.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Assignment {

	public final static int ACTIVE_MONTHS = 6;
	
	private boolean master;
	private boolean bachelor;
	private boolean displayAssignment = true;
	private boolean editExternalExaminer = false;
	
	private int id;
	private int departmentNumber;
	private int studyProgramNumber;
		
	private String numberOfStudents;
	private String title;
	private String facultySupervisor;
	private String description;
	private String studyProgram;
	private String department;
	private String numberOfStudentsError;
	private String fileUploadErrorMessage;
	private String type;
	private String attachedFilePath;
	
	private GregorianCalendar addedDate;
	private GregorianCalendar expireDate;
	
	private ArrayList<Supervisor> supervisorList;
	private ArrayList<Application> applications;
	private ArrayList<String> attachedFileList;
	
	private ExternalExaminer externalExaminer;
	
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public Assignment() {
		attachedFileList = new ArrayList<String>();
		applications = new ArrayList<Application>();
		supervisorList = new ArrayList<Supervisor>();		
		supervisorList.add(new Supervisor());
		
		bachelor = true;
		type = "Bachelor";
	}

	public void addApplication(Application application){
		applications.add(application);
	}
	
	public void removeApplication(Application application){
		applications.remove(application);
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

	public String getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(String numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(String facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(String studyProgram) {
		this.studyProgram = studyProgram;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNumberOfStudentsError() {
		return numberOfStudentsError;
	}

	public void setNumberOfStudentsError(String numberOfStudentsError) {
		this.numberOfStudentsError = numberOfStudentsError;
	}

	public String getFileUploadErrorMessage() {
		return fileUploadErrorMessage;
	}

	public void setFileUploadErrorMessage(String fileUploadErrorMessage) {
		this.fileUploadErrorMessage = fileUploadErrorMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
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

	public boolean equals (Assignment ab) {
		return ab.getId() == this.getId();
	}
	
	public String getAddedDateAsString() {		
		return simpleDateFormatter.format(getAddedDate().getTime());
	}
	
	public String getExpireDateAsString() {		
		return simpleDateFormatter.format(getExpireDate().getTime());
	}

	public ExternalExaminer getExternalExaminer() {
		return externalExaminer;
	}

	public void setExternalExaminer(ExternalExaminer externalExaminer) {
		this.externalExaminer = externalExaminer;
	}

	public boolean isEditExternalExaminer() {
		return editExternalExaminer;
	}

	public void setEditExternalExaminer(boolean editExternalExaminer) {
		this.editExternalExaminer = editExternalExaminer;
	}

	public ArrayList<Application> getApplications() {
		return applications;
	}

	public void setApplications(ArrayList<Application> applications) {
		this.applications = applications;
	}
}
