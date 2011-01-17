package no.uis.portal.employee;

import com.icesoft.faces.context.DisposableBean;

public class AssignmentBean implements DisposableBean {

	private int id;
	private int numberOfStudents;
	
	private String title;
	private String instructor; //Change this to a seperate class later?
	private String technicalResponsible;
	private String description;
	private String studyProgram;
	private String institute;
	
	private boolean master;
	private boolean bachelor;
	
	public void dispose() throws Exception {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getTechnicalResponsible() {
		return technicalResponsible;
	}

	public void setTechnicalResponsible(String technicalResponsible) {
		this.technicalResponsible = technicalResponsible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public String getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(String studyProgram) {
		this.studyProgram = studyProgram;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
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

}
