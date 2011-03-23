package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.jws.WebService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import no.uis.abam.dao.DepartmentDAO;
import no.uis.abam.dom.*;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private List<Department> departmentList;
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private DepartmentDAO departmentDao;
 	
	public AbamWebServiceTestImpl(){
		
		createAssignmentListContent();
		//initializeDepartmentAndStudyProgramLists();
		initializeStudentList();
		initializeThesisList();
	}
	
	public TreeSet<Assignment> getAllAssignments() {
		return assignmentList;
	}	
	
	public TreeSet<Assignment> getActiveAssignments() {
		TreeSet<Assignment> activeAssignments = new TreeSet<Assignment>();
		for (Assignment assignment : assignmentList) {
			if(!assignment.isExpired()) activeAssignments.add(assignment);
		}
		return activeAssignments;
	}
	
	public void saveAssignment(Assignment assignment){
		assignmentList.remove(assignment);
		assignmentList.add(assignment);
	}
	
	private void createAssignmentListContent(){
		Assignment test1 = new Assignment();
		test1.setTitle("Pet Bor oppgave");
		test1.setMaster(false);
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setDepartmentName("Petroleumsteknologi");
		test1.setDepartmentNumber(2);
		test1.setStudyProgramName("Boreteknologi");
		test1.setStudyProgramNumber(1);
		test1.setFacultySupervisor("Louis Lane");
		test1.getSupervisorList().get(0).setName("Superman");
		test1.setAddedDate(new GregorianCalendar(10, 11, 10));
		GregorianCalendar dato = test1.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test1.setExpireDate(dato);
		Application app = new Application();
		app.setApplicantStudentNumber((123456));
		app.setApplicationDate(new GregorianCalendar());				
		app.setPriority(1);		
		app.setAssignment(test1);
		applicationList.add(app);
		
		Assignment test2 = null;
		app = new Application();
		for (int j = 0, id = 2; j < 2; j++) {
			
			for (int i = 0; i < 3; i++) {
				app.setApplicantStudentNumber((123456+j));
				app.setApplicationDate(new GregorianCalendar());				
				app.setPriority(i+1);		
				test2 = new Assignment();
				test2.setTitle("IDE El oppgave " +i);
				test2.setBachelor(true);
				test2.setMaster(false);
				test2.setDescription("Beskrivelse av test" +i);
				test2.setNumberOfStudents("1");
				test2.setDepartmentName("Data- og elektroteknikk");
				test2.setDepartmentNumber(3);
				test2.setStudyProgramName("Elektro");
				test2.setStudyProgramNumber(2);
				test2.setId(id);
				test2.setFacultySupervisor("Robin");
				test2.getSupervisorList().get(0).setName("Batman");
				test2.setAddedDate(new GregorianCalendar(2010, 10, 10));
				dato = test2.getAddedDate();
				dato.add(Calendar.MONTH, 6);
				test2.setExpireDate(dato);
				assignmentList.add(test2);
				app.setAssignment(test2);
				//applicationList.add(app);
				app = new Application();	
				id++;
			}
		}
		
		assignmentList.add(test1);
		
	}
	
	private void initializeThesisList() {
		Thesis testThesis = new Thesis();
		testThesis.setAssignedAssignmentId(assignmentList.first().getId());
		testThesis.setStudentNumber1(studentList.get(0).getStudentNumber());
		savedThesesList.add(testThesis);
		testThesis = new Thesis();
		testThesis.setAssignedAssignmentId(assignmentList.last().getId());
		testThesis.setStudentNumber2(123456);
		testThesis.setStudentNumber1(studentList.get(1).getStudentNumber());
		savedThesesList.add(testThesis);
	}
	
	private void initializeDepartmentAndStudyProgramLists(){
		departmentList = new ArrayList<Department>();
		
		departmentList.add(new Department(new Integer(0), ""));
		departmentList.add(new Department(new Integer(1), "Institutt for industriell �konomi, risikostyring og planlegging"));
		departmentList.add(new Department(new Integer(2), "Petroleumsteknologi"));
		departmentList.add(new Department(new Integer(3), "Data- og elektroteknikk"));
		departmentList.add(new Department(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		departmentList.add(new Department(new Integer(5), "Matematikk og naturvitskap"));
		
		List<StudyProgram> listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		departmentList.get(0).setStudyPrograms(listToAdd);
		
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Industriell �konomi"));
		departmentList.get(1).setStudyPrograms(listToAdd);
		
	
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Boreteknologi"));
		listToAdd.add(new StudyProgram(new Integer(2), "Petroleumsgeologi"));
		departmentList.get(2).setStudyPrograms(listToAdd);
		
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Data"));
		listToAdd.add(new StudyProgram(new Integer(2), "Elektro"));
		listToAdd.add(new StudyProgram(new Integer(3), "Informasjonsteknologi"));
		departmentList.get(3).setStudyPrograms(listToAdd);
		
	
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Byggeteknikk"));
		listToAdd.add(new StudyProgram(new Integer(2), "Maskinteknikk"));
		listToAdd.add(new StudyProgram(new Integer(3), "Offshoreteknologi"));
		departmentList.get(4).setStudyPrograms(listToAdd);
		
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Matematikk"));
		listToAdd.add(new StudyProgram(new Integer(2), "Fysikk"));
		departmentList.get(5).setStudyPrograms(listToAdd);
		//studyProgramList = departmentList.get(0).getStudyPrograms();	
	}
	
	private void initializeStudentList() { 
		Student newStudent = new Student();
		newStudent.setName("Bachelor Studenten");
		newStudent.setStudentNumber(123456);
		newStudent.setBachelor(true);
		newStudent.setDepartmentName("Data- og elektroteknikk");
		newStudent.setStudyProgramName("Elektro");
		studentList.add(newStudent);
		
		newStudent = new Student();
		newStudent.setName("Master Studenten");
		newStudent.setStudentNumber(123457);
		newStudent.setDepartmentName("Data- og elektroteknikk");
		newStudent.setStudyProgramName("Elektro");
		newStudent.setBachelor(false);
		studentList.add(newStudent);
		
	}

	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);		
	}

	public List<Department> getDepartmentList() {
		System.out.println("Inne i getDepartmentList i WS");
		if ((departmentList == null) || (departmentList.isEmpty())) {
			departmentList = departmentDao.getDepartments();
			System.out.println("Virker: " + departmentList.size());
		}
		return departmentList;
	}

	public List<StudyProgram> getStudyProgramList(int departmentIndex) {
		return departmentList.get(departmentIndex).getStudyPrograms();
	}

	public String getStudyProgram(int departmentIndex, int studyProgramIndex) {
		return getStudyProgramList(departmentIndex).get(studyProgramIndex).getName();
	}

	public String getDepartment(int index) {
		return  departmentList.get(index).getOeNavn_Engelsk();
	}
	
	public void removeDepartment(Department department){
		departmentList.remove(department);
	}
	
//	public void setDepartmentList(ArrayList<Department> departmentList){
//		this.departmentList = departmentList;
//	}
	
	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	
	public List<Application> getApplicationList() {
		return applicationList;
	}
	
	public List<Application> getMasterApplicationList() {
		List<Application> masterApplicationList = new ArrayList<Application>();
		for (Application application : applicationList) {
			if(application.getAssignment().isMaster()) {
				masterApplicationList.add(application);
			}
		}
		return masterApplicationList;
	}

	public List<Application> getBachelorApplicationList() {
		List<Application> bachelorApplicationList = new ArrayList<Application>();
		for (Application application : applicationList) {
			if(application.getAssignment().isBachelor()) {
				bachelorApplicationList.add(application);
			}
		}
		return bachelorApplicationList;
	
	}
	
	public void setApplicationList(List<Application> applicationList) {
		this.applicationList = applicationList;
	}
	
	public void saveApplication(Application application) {
		Iterator<Application> iterator = applicationList.iterator();
		while (iterator.hasNext()){	
			Application app = iterator.next();
			if(app != null) {
				if (app.equals(application)) {
					applicationList.remove(app);
					break;
				}
			}
		}
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

	public Assignment getAssignmentFromId(int id) {
		for (Assignment assignment : assignmentList) {
			if(assignment.getId() == id){
				return assignment;
			}
		}
		return null;
	}
	
	public void updateApplicationsFromCurrentStudent(
			Application[] tempApplicationPriorityArray) {
		for (int i = 0; i < tempApplicationPriorityArray.length; i++) {
			if(tempApplicationPriorityArray[i].getAssignment() != null) {
				saveApplication(tempApplicationPriorityArray[i]);
			}
		}
	}
	
	public Student getStudentFromStudentNumber(long studentNumber) {
		for (Student student : studentList) {
			if (student.getStudentNumber() == studentNumber) return student;
		}
		return null;
	}
	
	public void addThesesFromList(List<Thesis> thesesToAdd) {
		for (Thesis thesis : thesesToAdd) {
			savedThesesList.add(thesis);
			Student student = getStudentFromStudentNumber(thesis.getStudentNumber1());
			student.setAssignedThesis(thesis);
			removeStudentsApplicationFromList(student);
		}
	}
	
	public void updateThesis(Thesis thesisToUpdate) {
		for (Thesis thesis : savedThesesList) {
			if(thesis.equals(thesisToUpdate)) {
				savedThesesList.remove(thesis);
				savedThesesList.add(thesisToUpdate);
				return;
			}
		}
	}
	
	private void removeStudentsApplicationFromList(Student student) {
		for (Application application : student.getApplicationPriorityArray()) {
			if (application != null) {
				removeApplication(application);
			}
		}			
	}
	
	public void updateStudent(Student studentToUpdate) {
		for (Student student : studentList) {
			if (student.equals(studentToUpdate)) {
				studentList.remove(student);
				studentList.add(studentToUpdate);
				return;
			}
		}
	}
	
	public List<Thesis> getThesisList() {
		return savedThesesList;
	}

	public void setDepartmentDao(DepartmentDAO departmentDao) {
		this.departmentDao = departmentDao;
	}
	
}
