package no.uis.portal.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.HtmlDataTable;


import no.uis.abam.dom.*;
import no.uis.abam.ws_abam.AbamWebService;

public class StudentService {

	private TreeSet<Assignment> assignmentList; 
	private Assignment selectedAssignment;
	
	private Student currentStudent;

	private AbamWebService abamStudentClient;
	
	//private LinkedList<SelectItem> departmentList;
	//private LinkedList<SelectItem> studyProgramList = new LinkedList<SelectItem>();
	//private LinkedList<LinkedList<SelectItem>> allStudyProgramsByDepartmentList;
	
	private ArrayList<Application> applicationList = new ArrayList<Application>();
	
	private String selectedDepartmentName;
	
	private int selectedDepartmentNumber;
	private int selectedStudyProgramNumber;
	
	public StudentService() {
		setCurrentStudentFromLoggedInUser();
	}

	public void setCurrentStudentFromLoggedInUser(){
		currentStudent = new BachelorStudent();
		currentStudent.setName("Studenten");
		currentStudent.setDepartment("Data- og elektroteknikk");
		currentStudent.setStudyProgram("Elektro");
	}

//	public void setCurrentStudentFromLoggedInUser(){
//		currentStudent = new MasterStudent();
//		currentStudent.setName("Studenten");
//		currentStudent.setDepartment("Petroleumsteknologi");
//		currentStudent.setStudyProgram("Boreteknologi");
//	}
	
	
	public int getNextId(){
		return abamStudentClient.getNextId();
	}
	
	
	public void saveAssignment(Assignment assignment) {
		currentStudent.setCustomAssignment(assignment);
	}
	
	public void setApplicationToStudent(Application application){
		currentStudent.addApplication(application);
	}
	
	public void setApplicationToAssignment(Application application){
		selectedAssignment.addApplication(application);
	}
	
	
	public TreeSet<Assignment> getAssignmentList() {
		if(assignmentList == null) 
			assignmentList = abamStudentClient.getAssignmentsFromDepartmentName(currentStudent.getDepartment());
		return assignmentList;		
	}

	
	public Assignment getSelectedAssignment() {
		return selectedAssignment;
	}

	
	public void setSelectedAssignment(Assignment selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
	
	public void actionRemoveApplication(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		currentStudent.removeApplication(application);
		selectedAssignment.removeApplication(application);
	}
	
	public void actionSetApplicationPriorityHigher(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		currentStudent.moveApplicationHigher(application);
	}
	
	public void actionSetApplicationPriorityLower(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		currentStudent.moveApplicationLower(application);
	}
	
	private Application getApplicationFromEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		return (Application)table.getRowData();
	}
	 
	
	public void actionClearStudyProgramAndDepartmentNumber(ActionEvent event){
		setSelectedStudyProgramNumber(0);
		setSelectedDepartmentNumber(0);
	}
	
	
	public void updateStudyProgramList(int index){
		selectedDepartmentName = (String) getDepartmentName(index);
		selectedDepartmentNumber = index;
		Set<Assignment> assignmentList = getAssignmentList();
		for (Assignment assignment : assignmentList) {
			if (assignment.getDepartmentName().equals(selectedDepartmentName)
				|| selectedDepartmentName.equals("")) { 
				if(currentStudentIsEligibleForAssignment(assignment))
					assignment.setDisplayAssignment(true);
				else assignment.setDisplayAssignment(false);
			} else assignment.setDisplayAssignment(false);
		}
		setAllEditExternalExaminerToFalse();
	}
	
	private boolean currentStudentIsEligibleForAssignment(Assignment assignment){
		return assignment.getType().equalsIgnoreCase(currentStudent.getType());
	}
	
	
//	public void actionUpdateStudyProgramListFromCreateAssignment(ValueChangeEvent event){
//		studyProgramList = allStudyProgramsByDepartmentList.get(Integer.parseInt(event.getNewValue().toString()));
//		selectedDepartmentNumber = Integer.parseInt(event.getNewValue().toString());
//	}
	
	
	public void actionSetDisplayAssignment(ValueChangeEvent event){
		String selectedStudyProgram = (String) getStudyProgramName(Integer.parseInt(event.getNewValue().toString()));
		assignmentList = getAssignmentList();
		selectedStudyProgramNumber = Integer.parseInt(event.getNewValue().toString());
		if (selectedDepartmentName == null) setSelectedDepartment("");
		for (Assignment assignment : assignmentList) {
			if (checkIfAssignmentShouldBeDisplayed(assignment, selectedStudyProgram)){ 
				if(currentStudentIsEligibleForAssignment(assignment))
					assignment.setDisplayAssignment(true);
			} else assignment.setDisplayAssignment(false);
		}
		setAllEditExternalExaminerToFalse();
	}
	
	private boolean checkIfAssignmentShouldBeDisplayed(Assignment abIn, String selectedStudyProgram) {
		return (selectedStudyProgram.equals("") && abIn.getDepartmentName().equals(selectedDepartmentName)) 
		|| abIn.getStudyProgram().equals(selectedStudyProgram);
	}
	
	
	public void updateSelectedAssignmentInformation(Assignment selectedAssignment){
		setSelectedAssignment(selectedAssignment);
		//setStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
	}
	
	
	public void actionPrepareAvailableAssignments(ActionEvent event) {
		assignmentList = abamStudentClient.getAssignmentsFromDepartmentName(currentStudent.getDepartment());
		updateStudyProgramList(findDepartmentNumberForCurrentStudent());
		
		System.out.println(assignmentList.getClass());
		Iterator<Assignment> iterator = assignmentList.iterator();
		while(iterator.hasNext()){
			Assignment ass = iterator.next();		
			System.out.println(ass.getId() + "|" + ass.getTitle());
		}
		
		
	}
	
	private int findDepartmentNumberForCurrentStudent() {
		String name = currentStudent.getDepartment();
		for (SelectItem department : getDepartmentList()) {
			if(department.getLabel().equalsIgnoreCase(name)) return Integer.parseInt(department.getValue().toString());
		}
		return 0;
	}
	
	public void setAllEditExternalExaminerToFalse() {
		for (Assignment assignment : getAssignmentList()) {
			assignment.setEditExternalExaminer(false);
		}
	}
	
	
	public LinkedList<Department> getDepartmentList() {
		return abamStudentClient.getDepartmentList();
	}

	public List<EditableSelectItem> getStudyProgramList() {
		return abamStudentClient.getStudyProgramList(selectedDepartmentNumber);
	}
	
	public String getStudyProgramName(int index) {
		return abamStudentClient.getStudyProgram(selectedDepartmentNumber,index);
	}
	
	public String getDepartmentName(int index) {
		return  abamStudentClient.getDepartmentList().get(index).getLabel();
	}

	
	public String getSelectedDepartment() {
		return selectedDepartmentName;
	}

	
	public void setSelectedDepartment(String selectedDepartment) {
		this.selectedDepartmentName = selectedDepartment;
	}

	
	public void removeAssignment(Assignment assignment) {
		abamStudentClient.removeAssignment(assignment);
	}

	
//	public void setStudyProgramListFromDepartmentNumber(int departmentNumber) {
//		setStudyProgramList(getAllStudyProgramsByDepartmentsList().
//				get(departmentNumber));
//	}
//	
	
	public int getSelectedDepartmentNumber() {
		return selectedDepartmentNumber;
	}

	
	public void setSelectedDepartmentNumber(int selectedDepartmentNumber) {
		this.selectedDepartmentNumber = selectedDepartmentNumber;
	}

	
	public int getSelectedStudyProgramNumber() {
		return selectedStudyProgramNumber;
	}

	
	public void setSelectedStudyProgramNumber(int selectedStudyProgramNumber) {
		this.selectedStudyProgramNumber = selectedStudyProgramNumber;
	}

	public Student getCurrentStudent() {
		return currentStudent;
	}

	public void setCurrentStudent(Student currentStudent) {
		this.currentStudent = currentStudent;
	}

	public ArrayList<Application> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(ArrayList<Application> applicationList) {
		this.applicationList = applicationList;
	}



	public void setAbamStudentClient(AbamWebService abamStudentClient) {
		this.abamStudentClient = abamStudentClient;
	}
}
