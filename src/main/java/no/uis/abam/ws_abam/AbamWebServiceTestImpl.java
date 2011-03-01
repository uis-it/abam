package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.jws.WebService;

import no.uis.abam.dom.*;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private LinkedList<Department> departmentList;
	private List<EditableSelectItem> studyProgramList = new LinkedList();
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
 	
	public AbamWebServiceTestImpl(){
		
		createAssignmentListContent();
		initializeDepartmentAndStudyProgramLists();
		initializeStudentList();
	}
	
	public TreeSet<Assignment> getAllAssignments() {
		return assignmentList;
	}	
	
	public void saveAssignment(Assignment assignment){
		assignmentList.add(assignment);
	}

	private void createAssignmentListContent(){
		Assignment test1 = new Assignment();
		test1.setTitle("Pet Bor oppgave");
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setDepartmentName("Petroleumsteknologi");
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
		test2.setBachelor(false);
		test2.setMaster(true);
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setDepartmentName("Data- og elektroteknikk");
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
	}
	
	private void initializeDepartmentAndStudyProgramLists(){
		departmentList = new LinkedList<Department>();
		
		departmentList.add(new Department(new Integer(0), ""));
		departmentList.add(new Department(new Integer(1), "Institutt for industriell økonomi, risikostyring og planlegging"));
		departmentList.add(new Department(new Integer(2), "Petroleumsteknologi"));
		departmentList.add(new Department(new Integer(3), "Data- og elektroteknikk"));
		departmentList.add(new Department(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		departmentList.add(new Department(new Integer(5), "Matematikk og naturvitskap"));
		
		LinkedList<EditableSelectItem> listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		departmentList.get(0).setStudyPrograms(listToAdd);
		
		listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Industriell økonomi"));
		departmentList.get(1).setStudyPrograms(listToAdd);
		
		listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Boreteknologi"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Petroleumsgeologi"));
		departmentList.get(2).setStudyPrograms(listToAdd);
		
		listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Data"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Elektro"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Informasjonsteknologi"));
		departmentList.get(3).setStudyPrograms(listToAdd);
		
		listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Byggeteknikk"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Maskinteknikk"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Offshoreteknologi"));
		departmentList.get(4).setStudyPrograms(listToAdd);
		
		listToAdd = new LinkedList<EditableSelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Matematikk"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Fysikk"));
		departmentList.get(5).setStudyPrograms(listToAdd);
		studyProgramList = departmentList.get(0).getStudyPrograms();	
	}
	
	private void initializeStudentList() { 
		Student newStudent = new BachelorStudent();
		newStudent.setName("Bachelor Studenten");
		newStudent.setStudentNumber(123456);
		newStudent.setDepartment("Data- og elektroteknikk");
		newStudent.setStudyProgram("Elektro");
		studentList.add(newStudent);
		
		newStudent = new MasterStudent();
		newStudent.setName("Master Studenten");
		newStudent.setStudentNumber(654321);
		newStudent.setDepartment("Data- og elektroteknikk");
		newStudent.setStudyProgram("Elektro");
		studentList.add(newStudent);
		
	}

	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);		
	}

	public LinkedList<Department> getDepartmentList() {
		return departmentList;
	}

	public List<EditableSelectItem> getStudyProgramList(int departmentIndex) {
		return departmentList.get(departmentIndex).getStudyPrograms();
	}

//	public List<LinkedList<EditableSelectItem>> getAllStudyProgramsByDepartmentList() {
//		return allStudyProgramsByDepartmentList;
//	}

//	public void setAllStudyProgramsByDepartmentList(
//			List<LinkedList<EditableSelectItem>> allStudyProgramsByDepartmentList) {
//		this.allStudyProgramsByDepartmentList = allStudyProgramsByDepartmentList;
//	}

	public String getStudyProgram(int departmentIndex, int studyProgramIndex) {
		return getStudyProgramList(departmentIndex).get(studyProgramIndex).getLabel();
	}

	public String getDepartment(int index) {
		return  departmentList.get(index).getLabel();
	}
	
	public void removeDepartment(EditableSelectItem department){
		departmentList.remove(department);
	}
	
	public void setDepartmentList(LinkedList<Department> departmentList){
		this.departmentList = departmentList;
	}
	
	public List<Application> getApplicationList() {
		return applicationList;
	}
	
	public void setApplicationList(List<Application> applicationList) {
		this.applicationList = applicationList;
	}
	
	public void saveApplication(Application application) {
		applicationList.add(application);
	}
	
	public void removeApplication(Application application) {
		for (Application app : applicationList) {
			if (app.equals(application)) {
					applicationList.remove(app);
				return;
			}
		}
	}

	public int getNextId() {
		return assignmentList.size()+1;
	}

	public void setAssignmentList(TreeSet<Assignment> assignmentList) {
		this.assignmentList = assignmentList;
	}

	public TreeSet<Assignment> getAssignmentsFromDepartmentName(String departmentName) {
		TreeSet<Assignment> assignmentsToReturn = new TreeSet<Assignment>();
		for (Assignment assignment : assignmentList) {
			if(assignment.getDepartmentName().equalsIgnoreCase(departmentName)){
				assignmentsToReturn.add(assignment);
			}
		}
		return assignmentsToReturn;
	}

	public void updateApplicationsFromCurrentStudent(
			Application[] tempApplicationPriorityArray) {
		for (int i = 0; i < tempApplicationPriorityArray.length; i++) {
			
		}
	}
	
	public Student getStudentFromStudentNumber(long studentNumber) {
		for (Student student : studentList) {
			if (student.getStudentNumber() == studentNumber) return student;
		}
		return null;
	}
}
