package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.jws.WebService;
import org.springframework.transaction.CannotCreateTransactionException;
import no.uis.abam.dao.DepartmentDAO;
import no.uis.abam.dom.*;
import no.uis.abam.util.LevenshteinDistance;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private List<Department> departmentList;
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private List<Employee> employeeList = new ArrayList<Employee>();
	private DepartmentDAO departmentDao;
 	
	public AbamWebServiceTestImpl(){
		
		//createAssignmentListContent();
		//initializeDepartmentAndStudyProgramLists();
		initializeStudentList();
		createEmployeeListContent();
		//initializeThesisList();
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
	
	private void createEmployeeListContent() {
		Employee employee = new Employee();
		employee.setName("Ansatt1 Ansatt1");
		employee.setEmployeeId("3456789");
		employeeList.add(employee);
		employee = new Employee();
		employee.setName("Ansatt2 Ansatt2");
		employee.setEmployeeId("2345678");
		employeeList.add(employee);
		employee = new Employee();
		employee.setName("Joe Bloggs");
		employee.setEmployeeId("1234567");
		employeeList.add(employee);
		employee = new Employee();
		employee.setName("Super Mann");
		employee.setEmployeeId("1111111");
		employeeList.add(employee);
	}
	
	private void createAssignmentListContent(){
		Assignment test1 = new Assignment();
		test1.setTitle("IDE Data oppgave");
		test1.setMaster(false);
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setDepartmentCode("TN-IDE");
		test1.setDepartmentNumber(2);
		test1.setStudyProgramName("Data");
		test1.setStudyProgramNumber(1);
		Employee emp = new Employee();
		emp.setName("Lois Lane");
		test1.setFacultySupervisor(emp);
		test1.getSupervisorList().get(0).setName("Superman");
		test1.setAddedDate(new GregorianCalendar(10, 11, 10));
		GregorianCalendar dato = test1.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test1.setExpireDate(dato);
		Application app = new Application();
		app.setApplicantStudentNumber("123456");
		app.setApplicationDate(new GregorianCalendar());				
		app.setPriority(1);		
		app.setAssignment(test1);
		applicationList.add(app);
		
		Assignment test2 = null;
		app = new Application();
		for (int j = 0, id = 2; j < 2; j++) {
			
			for (int i = 0; i < 3; i++) {
				app.setApplicantStudentNumber("123457");
				app.setApplicationDate(new GregorianCalendar());				
				app.setPriority(i+1);		
				test2 = new Assignment();
				test2.setTitle("IDE El oppgave " +i);
				test2.setBachelor(true);
				test2.setMaster(false);
				test2.setDescription("Beskrivelse av test" +i);
				test2.setNumberOfStudents("1");
				test2.setDepartmentCode("TN-IDE");
				test2.setDepartmentNumber(3);
				test2.setStudyProgramName("Elektro");
				test2.setStudyProgramNumber(2);
				test2.setId(id);
				emp = new Employee();
				emp.setName("Robin");
				test2.setFacultySupervisor(emp);
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
		testThesis.setAssignedAssignment(assignmentList.first());
		testThesis.setStudentNumber1(studentList.get(0).getStudentNumber());
		savedThesesList.add(testThesis);
		testThesis = new Thesis();
		testThesis.setAssignedAssignment(assignmentList.last());
		testThesis.setStudentNumber2("123456");
		testThesis.setStudentNumber1(studentList.get(1).getStudentNumber());
		savedThesesList.add(testThesis);
	}
	
	private void initializeDepartmentAndStudyProgramLists(){
		departmentList = new ArrayList<Department>();
		
		departmentList.add(new Department(new Integer(0), ""));
		departmentList.add(new Department(new Integer(1), "Institutt for industriell økonomi, risikostyring og planlegging"));
		departmentList.add(new Department(new Integer(2), "Petroleumsteknologi"));
		departmentList.add(new Department(new Integer(3), "Data- og elektroteknikk"));
		departmentList.add(new Department(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		departmentList.add(new Department(new Integer(5), "Matematikk og naturvitskap"));
		
		List<StudyProgram> listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		departmentList.get(0).setStudyPrograms(listToAdd);
		
		listToAdd = new ArrayList<StudyProgram>();
		listToAdd.add(new StudyProgram(new Integer(0), ""));
		listToAdd.add(new StudyProgram(new Integer(1), "Industriell økonomi"));
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
		newStudent.setStudentNumber("123456");
		newStudent.setBachelor(true);
		newStudent.setDepartmentCode("TN-IDE");
		newStudent.setStudyProgramName("Elektro");
		studentList.add(newStudent);
		
		newStudent = new Student();
		newStudent.setName("Annen Student");
		newStudent.setStudentNumber("234567");
		newStudent.setBachelor(true);
		newStudent.setDepartmentCode("TN-IDE");
		newStudent.setStudyProgramName("Elektro");
		studentList.add(newStudent);
		
		newStudent = new Student();
		newStudent.setName("Master Studenten");
		newStudent.setStudentNumber("123457");
		newStudent.setDepartmentCode("TN-IDE");
		newStudent.setStudyProgramName("Elektro");
		newStudent.setBachelor(false);
		studentList.add(newStudent);
		
	}
	
	private void initializeStudyPrograms(int departmentIndex) {
		
		List<StudyProgram> listToAdd = new ArrayList<StudyProgram>();
		if ( departmentList.get(departmentIndex).getOe2() == 3) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Industriell økonomi"));
			getDepartmentFromOe2(3).setStudyPrograms(listToAdd);
		} else if (departmentList.get(departmentIndex).getOe2() == 6) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Boreteknologi"));
			listToAdd.add(new StudyProgram(new Integer(2), "Petroleumsgeologi"));
			getDepartmentFromOe2(6).setStudyPrograms(listToAdd);
		} else if (departmentList.get(departmentIndex).getOe2() == 4) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Data"));
			listToAdd.add(new StudyProgram(new Integer(2), "Elektro"));
			listToAdd.add(new StudyProgram(new Integer(3), "Informasjonsteknologi"));
			getDepartmentFromOe2(4).setStudyPrograms(listToAdd);
		} else if (departmentList.get(departmentIndex).getOe2() == 5) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Byggeteknikk"));
			listToAdd.add(new StudyProgram(new Integer(2), "Maskinteknikk"));
			listToAdd.add(new StudyProgram(new Integer(3), "Offshoreteknologi"));
			getDepartmentFromOe2(5).setStudyPrograms(listToAdd);
		} else if(departmentList.get(departmentIndex).getOe2() == 2) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Matematikk"));
			listToAdd.add(new StudyProgram(new Integer(2), "Fysikk"));
			getDepartmentFromOe2(2).setStudyPrograms(listToAdd);
		} else {
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			departmentList.get(0).setStudyPrograms(listToAdd);			
		}

	}
	
	private Department getDepartmentFromOe2(int oe2) {
		for (Department dep : departmentList) {
			if(dep.getOe2() == oe2) return dep;
		}
		return departmentList.get(0);
	}

	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);		
	}

	public List<Department> getDepartmentList() {
		try {
			if ((departmentList == null) || (departmentList.isEmpty())) {
				departmentList = departmentDao.getDepartments();
				Department blankDepartment = new Department(0, "");
				blankDepartment.setOeKode("");
				departmentList.add(0, blankDepartment);
				for (int i = 0; i < departmentList.size(); i++) {
					initializeStudyPrograms(i);
				}
			}
		} catch (CannotCreateTransactionException e) {
			departmentList = new ArrayList<Department>();
			Department depToAdd = new Department(new Integer(0), "");
			depToAdd.setOe2((short)0);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(1), "Industriell økonomi, risikostyring og planlegging");
			depToAdd.setOe2((short)3);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(2), "Petroleumsteknologi");
			depToAdd.setOe2((short)6);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(3), "Data- og elektroteknikk");
			depToAdd.setOe2((short)4);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(4), "Konstruksjonsteknikk og materialteknologi");
			depToAdd.setOe2((short)5);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(5), "Matematikk og naturvitskap");
			depToAdd.setOe2((short)2);
			departmentList.add(depToAdd);			
			for (int i = 0; i < departmentList.size(); i++) {
				initializeStudyPrograms(i);
			}					
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
	
	public List<Application> getMasterApplicationListFromDepartmentCode(String code) {
		List<Application> masterApplicationList = new ArrayList<Application>();
		for (Application application : applicationList) {
			if(application.getAssignment().isMaster() && application.getAssignment().getDepartmentCode().equals(code)) {
				masterApplicationList.add(application);
			}
		}
		return masterApplicationList;
	}

	public List<Application> getBachelorApplicationListFromDepartmentCode(String code) {
		List<Application> bachelorApplicationList = new ArrayList<Application>();
		for (Application application : applicationList) {
			if(application.getAssignment().isBachelor() && application.getAssignment().getDepartmentCode().equals(code)) {
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

	public TreeSet<Assignment> getAssignmentsFromDepartmentCode(String departmentCode) {
		TreeSet<Assignment> assignmentsToReturn = new TreeSet<Assignment>();
		for (Assignment assignment : assignmentList) {
			if(assignment.getDepartmentCode().equalsIgnoreCase(departmentCode)){
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
	
	public Student getStudentFromStudentNumber(String studentNumber) {
		for (Student student : studentList) {
			if (student.getStudentNumber().equals(studentNumber)) {
				return student;
			}
		}
		return null;
	}
	
	public void addThesesFromList(List<Thesis> thesesToAdd) {		
		for (Thesis thesis : thesesToAdd) {			
			savedThesesList.add(thesis);			
			Student student = getStudentFromStudentNumber(thesis.getStudentNumber1());
			System.out.println("I WS: "+ thesis.getStudentNumber1());
			student.setAssignedThesis(thesis);			
			removeStudentsApplicationFromList(student);
			if (thesis.getStudentNumber2() != null && !thesis.getStudentNumber2().isEmpty()) {
				student = getStudentFromStudentNumber(thesis.getStudentNumber2());
				student.setAssignedThesis(thesis);			
				removeStudentsApplicationFromList(student);
			}
			if (thesis.getStudentNumber3() != null && !thesis.getStudentNumber3().isEmpty()) {
				student = getStudentFromStudentNumber(thesis.getStudentNumber3());
				student.setAssignedThesis(thesis);			
				removeStudentsApplicationFromList(student);
			}
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

	public Employee getEmployeeFromUisLoginName(String loginName) {
		for (Employee employee : employeeList) {
			if (employee.getEmployeeId().equals(loginName)) {
				return employee;
			}
		}
		return null;
	}

	public Employee getEmployeeFromFullName(String facultySupervisorName) {
		
		int countNames = (facultySupervisorName.split(" ")).length;
		int lengthOfName = facultySupervisorName.length();		
				
		int lowestLevenshteinDistance = lengthOfName/10 + countNames;
		
		Employee foundEmployee = new Employee();
		foundEmployee.setName("");
		for (Employee employee : employeeList) {
			String employeeName = employee.getName();
			int currentDistance = LevenshteinDistance.getLevenshteinDistance(employeeName, facultySupervisorName); 
			if (currentDistance < lowestLevenshteinDistance) {
				foundEmployee = employee;
				lowestLevenshteinDistance = currentDistance;
			}
		}
		return foundEmployee;
	}
}
