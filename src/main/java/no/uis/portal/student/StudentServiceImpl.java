package no.uis.portal.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.HtmlDataTable;


import no.uis.portal.student.domain.Application;
import no.uis.portal.student.domain.AssigmentIdComparator;
import no.uis.portal.student.domain.Assignment;
import no.uis.portal.student.domain.BachelorStudent;
import no.uis.portal.student.domain.ExternalExaminer;
import no.uis.portal.student.domain.MasterStudent;
import no.uis.portal.student.domain.Student;

public class StudentServiceImpl implements StudentService {

	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>(new AssigmentIdComparator()); 
	private Assignment selectedAssignment;
	
	private Student currentStudent;

	private LinkedList<SelectItem> departmentList;
	private LinkedList<SelectItem> studyProgramList = new LinkedList<SelectItem>();
	private LinkedList<LinkedList<SelectItem>> allStudyProgramsByDepartmentList;
	
	private ArrayList<Application> applicationList = new ArrayList<Application>();
	
	private String selectedDepartment;
	
	private int selectedDepartmentNumber;
	private int selectedStudyProgramNumber;
	
	public StudentServiceImpl() {
		if(departmentList == null) {
			initializeDepartmentAndStudyProgramLists();
			createTestData();
			setCurrentStudentFromLoggedInUser();
		}
	}
	
	private void initializeDepartmentAndStudyProgramLists(){
		departmentList = new LinkedList<SelectItem>();
		allStudyProgramsByDepartmentList = new LinkedList<LinkedList<SelectItem>>();
		
		departmentList.add(new EditableSelectItem(new Integer(0), ""));
		departmentList.add(new EditableSelectItem(new Integer(1), "Institutt for industriell økonomi, risikostyring og planlegging"));
		departmentList.add(new EditableSelectItem(new Integer(2), "Petroleumsteknologi"));
		departmentList.add(new EditableSelectItem(new Integer(3), "Data- og elektroteknikk"));
		departmentList.add(new EditableSelectItem(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		departmentList.add(new EditableSelectItem(new Integer(5), "Matematikk og naturvitskap"));
		
		LinkedList<SelectItem> listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		allStudyProgramsByDepartmentList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Industriell økonomi"));
		allStudyProgramsByDepartmentList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Boreteknologi"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Petroleumsgeologi"));
		allStudyProgramsByDepartmentList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Data"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Elektro"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Informasjonsteknologi"));
		allStudyProgramsByDepartmentList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Byggeteknikk"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Maskinteknikk"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Offshoreteknologi"));
		allStudyProgramsByDepartmentList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Matematikk"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Fysikk"));
		allStudyProgramsByDepartmentList.add(listToAdd);
		studyProgramList = allStudyProgramsByDepartmentList.get(0);
	}
	
	public void createTestData(){
		Assignment test1 = new Assignment();
		test1.setTitle("Pet Bor oppgave");
		test1.setBachelor(false);
		test1.setMaster(true);
		test1.setType("Master");
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setDepartment("Petroleumsteknologi");
		test1.setDepartmentNumber(2);
		test1.setStudyProgram("Boreteknologi");
		test1.setStudyProgramNumber(1);
		test1.setFacultySupervisor("Louis Lane");
		test1.getSupervisorList().get(0).setName("Superman");
		test1.setExternalExaminer(new ExternalExaminer("tester"));
		test1.setAddedDate(new GregorianCalendar(10, 11, 10));
		GregorianCalendar dato = test1.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test1.setExpireDate(dato);
		
		Assignment test2 = new Assignment();
		test2.setTitle("IDE El oppgave");
		test2.setBachelor(true);
		test2.setMaster(false);
		test2.setType("Bachelor");
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setDepartment("Data- og elektroteknikk");
		test2.setDepartmentNumber(3);
		test2.setStudyProgram("Elektro");
		test2.setStudyProgramNumber(2);
		test2.setId(2);
		test2.setFacultySupervisor("Robin");
		test2.getSupervisorList().get(0).setName("Batman");
		test2.setAddedDate(new GregorianCalendar(2010, 10, 10));
		dato = test2.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test2.setExpireDate(dato);
		assignmentList.add(test1);
		assignmentList.add(test2);
		
		Assignment test3 = new Assignment();
		test3.setTitle("Hei hopp");
		test3.setBachelor(true);
		test3.setMaster(false);
		test3.setType("Bachelor");
		test3.setDescription("Beskrivelse av test3");
		test3.setNumberOfStudents("1");
		test3.setDepartment("Data- og elektroteknikk");
		test3.setDepartmentNumber(3);
		test3.setStudyProgram("Elektro");
		test3.setStudyProgramNumber(2);
		test3.setId(3);
		test3.setFacultySupervisor("Robin");
		test3.getSupervisorList().get(0).setName("Batman");
		test3.setAddedDate(new GregorianCalendar(2010, 10, 10));
		dato = test3.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test3.setExpireDate(dato);
		assignmentList.add(test3);
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
	
	@Override
	public int getNextId(){
		return assignmentList.size()+1;
	}
	
	@Override
	public void saveAssignment(Assignment assignment) {
		currentStudent.setCustomAssignment(assignment);
	}
	
	public void setApplicationToStudent(Application application){
		currentStudent.addApplication(application);
	}
	
	public void setApplicationToAssignment(Application application){
		selectedAssignment.addApplication(application);
	}
	
	@Override
	public TreeSet<Assignment> getAssignmentList() {
		return assignmentList;
	}

	@Override
	public Assignment getSelectedAssignment() {
		return selectedAssignment;
	}

	@Override
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
	 
	@Override
	public void actionClearStudyProgramAndDepartmentNumber(ActionEvent event){
		setSelectedStudyProgramNumber(0);
		setSelectedDepartmentNumber(0);
	}
	
	@Override
	public void updateStudyProgramList(int index){
		studyProgramList = allStudyProgramsByDepartmentList.get(index);
		selectedDepartment = (String) departmentList.get(index).getLabel();
		selectedDepartmentNumber = index;
		TreeSet<Assignment> assignmentList = getAssignmentList();
		for (Assignment assignment : assignmentList) {
			if (assignment.getDepartment().equals(selectedDepartment)
				|| selectedDepartment.equals("")) { 
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
	
	@Override
	public void actionUpdateStudyProgramListFromCreateAssignment(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByDepartmentList.get(Integer.parseInt(event.getNewValue().toString()));
		selectedDepartmentNumber = Integer.parseInt(event.getNewValue().toString());
	}
	
	@Override
	public void actionSetDisplayAssignment(ValueChangeEvent event){
		String selectedStudyProgram = (String) studyProgramList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		TreeSet<Assignment> assignmentList = getAssignmentList();
		selectedStudyProgramNumber = Integer.parseInt(event.getNewValue().toString());
		if (selectedDepartment == null) setSelectedDepartment("");
		for (Assignment assignment : assignmentList) {
			if (checkIfAssignmentShouldBeDisplayed(assignment, selectedStudyProgram)){ 
				if(currentStudentIsEligibleForAssignment(assignment))
					assignment.setDisplayAssignment(true);
			} else assignment.setDisplayAssignment(false);
		}
		setAllEditExternalExaminerToFalse();
	}
	
	private boolean checkIfAssignmentShouldBeDisplayed(Assignment abIn, String selectedStudyProgram) {
		return (selectedStudyProgram.equals("") && abIn.getDepartment().equals(selectedDepartment)) 
		|| abIn.getStudyProgram().equals(selectedStudyProgram);
	}
	
	@Override
	public void updateSelectedAssignmentInformation(Assignment selectedAssignment){
		setSelectedAssignment(selectedAssignment);
		setStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
	}
	
	@Override
	public void actionUpdateDepartmentNumberFromCurrentStudent(ActionEvent event) {
		updateStudyProgramList(findDepartmentNumberForCurrentStudent());
	}
	
	private int findDepartmentNumberForCurrentStudent() {
		String name = currentStudent.getDepartment();
		for (SelectItem department : departmentList) {
			if(department.getLabel().equalsIgnoreCase(name)) return Integer.parseInt(department.getValue().toString());
		}
		return 0;
	}
	
	public void setAllEditExternalExaminerToFalse() {
		for (Assignment assignment : assignmentList) {
			assignment.setEditExternalExaminer(false);
		}
	}
	
	@Override
	public LinkedList<SelectItem> getDepartmentList() {
		return departmentList;
	}

	@Override
	public void setDepartmentList(LinkedList<SelectItem> departmentList) {
		this.departmentList = departmentList;
	}
	@Override
	public LinkedList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	@Override
	public void setStudyProgramList(LinkedList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}
	
	@Override
	public LinkedList<LinkedList<SelectItem>> getAllStudyProgramsByDepartmentsList() {
		return allStudyProgramsByDepartmentList;
	}

	@Override
	public void setAllStudyProgramsByDepartmentList(
			LinkedList<LinkedList<SelectItem>> allStudyProgramsByDepartmentListIn) {
		  allStudyProgramsByDepartmentList = allStudyProgramsByDepartmentListIn;
	}

	@Override
	public String getStudyProgram(int index) {
		return studyProgramList.get(index).getLabel();
	}
	@Override
	public String getDepartment(int index) {
		return  departmentList.get(index).getLabel();
	}

	@Override
	public String getSelectedDepartment() {
		return selectedDepartment;
	}

	@Override
	public void setSelectedDepartment(String selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	@Override
	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);
	}

	@Override
	public void setStudyProgramListFromDepartmentNumber(int departmentNumber) {
		setStudyProgramList(getAllStudyProgramsByDepartmentsList().
				get(departmentNumber));
	}
	
	@Override
	public int getSelectedDepartmentNumber() {
		return selectedDepartmentNumber;
	}

	@Override
	public void setSelectedDepartmentNumber(int selectedDepartmentNumber) {
		this.selectedDepartmentNumber = selectedDepartmentNumber;
	}

	@Override
	public int getSelectedStudyProgramNumber() {
		return selectedStudyProgramNumber;
	}

	@Override
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


}
