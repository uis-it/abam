package no.uis.abam.ws_abam;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.transaction.CannotCreateTransactionException;
import no.uis.abam.dao.DepartmentDAO;
import no.uis.abam.dao.EmployeeDAO;
import no.uis.abam.dao.EmployeeDAOImpl;
import no.uis.abam.dom.*;
import no.uis.abam.util.LevenshteinDistance;
import no.uis.service.affiliation.AffiliationDataType;
import no.uis.service.person.PersonType;
import no.uis.service.person.ws.PersonService;
import no.uis.service.person.ws.PersonWebService;
import no.uis.service.person.ws.WebServiceException_Exception;
import no.uis.service.student.AcademicAffiliationType;
import no.uis.service.student.ProgramLinkType;
import no.uis.service.student.StudentDataType;
import no.uis.service.student.TeachingLinkType;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private Logger log = Logger.getLogger(AbamWebServiceTestImpl.class);
	
	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private List<Department> departmentList;
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private List<Thesis> archivedThesesList = new ArrayList<Thesis>();
	private List<Employee> employeeList = new ArrayList<Employee>();
	private DepartmentDAO departmentDao;
 	
	private PersonWebService personWebService;
	
	public AbamWebServiceTestImpl(){
		personWebService = getWS();
		fillEmployeeListIfEmpty();
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
			depToAdd.setOeKode("TN-ABC");
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(1), "Industriell økonomi, risikostyring og planlegging");
			depToAdd.setOeKode("TN-ABC");
			depToAdd.setOe2((short)3);
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(2), "Petroleumsteknologi");
			depToAdd.setOe2((short)6);
			depToAdd.setOeKode("TN-ABC");
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(3), "Data- og elektroteknikk");
			depToAdd.setOe2((short)4);
			depToAdd.setOeKode("TN-IDE");
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(4), "Konstruksjonsteknikk og materialteknologi");
			depToAdd.setOe2((short)5);
			depToAdd.setOeKode("TN-ABC");
			departmentList.add(depToAdd);
			depToAdd = new Department(new Integer(5), "Matematikk og naturvitskap");
			depToAdd.setOe2((short)2);
			depToAdd.setOeKode("TN-ABC");
			departmentList.add(depToAdd);			
			for (int i = 0; i < departmentList.size(); i++) {
				initializeStudyPrograms(i);
			}					
		}
		return departmentList;
	}

	public List<StudyProgram> getStudyProgramListFromDepartmentIndex(int departmentIndex) {
		return departmentList.get(departmentIndex).getStudyPrograms();
	}

	public String getStudyProgramName(int departmentIndex, int studyProgramIndex) {
		return getStudyProgramListFromDepartmentIndex(departmentIndex).get(studyProgramIndex).getName();
	}

	public String getDepartmentName(int index) {
		return  departmentList.get(index).getOeNavn_Engelsk();
	}
	
	public void removeDepartment(Department department){
		departmentList.remove(department);
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
	
	public Assignment getCustomAssignmentFromStudentNumber(String studentNumber) {
		Student std = getStudentFromStudentNumber(studentNumber);
		if (std != null) return std.getCustomAssignment();
		return null;
	}
	
	public void updateApplications(
			Application[] tempApplicationPriorityArray) {
		for (int i = 0; i < tempApplicationPriorityArray.length; i++) {
			if(tempApplicationPriorityArray[i].getAssignment() != null) {
				saveApplication(tempApplicationPriorityArray[i]);
			}
		}
	}
		
	public Student getStudentFromStudentNumber(String studentNumber) {
		log.setLevel(Level.ERROR);
		Student student = getStudentFromAbamStudentList(studentNumber);		
		if (student == null) {
			PersonType person = null;
			try {
				person = personWebService
						.getPersonByStudentNumber(studentNumber);
				if (person != null) {
					student = getStudentFromPersonTypeObject(person);
					studentList.add(student);
				}
			} catch (WebServiceException_Exception e) {
				log.debug("getStudentFromStudentNumber: " + e.getMessage());
			}
		}
		return student;
	}
	
	private Student getStudentFromPersonTypeObject(PersonType person) {
		StudentDataType sdt = getStudentDataTypeFromPersonType(person);
		Student student = new Student();
		student.setBachelor(personIsBachelorStudent(sdt));
		student.setDepartmentCode(getDepartmentCodeFromAcademicATList(sdt.getAcademicAffiliation()));
		student.setStudyProgramName(getStudyProgramNameFromAcademicATList(sdt.getAcademicAffiliation()));
		student.setName(person.getFirstName() + " " + person.getLastName());
		student.setStudentNumber(sdt.getStudentNumber());	
		return student;
	}
	
	private String getStudyProgramNameFromAcademicATList(
			List<AcademicAffiliationType> academicAffiliation) {
		for (AcademicAffiliationType academicAffiliationType : academicAffiliation) {
			List<ProgramLinkType> programLink = academicAffiliationType.getProgramLink();
			for (ProgramLinkType programLinkType : programLink) {
				String ref = programLinkType.getRef();
				return getStudyProgramNameFromProgramLinkRef(ref);
			}
		}
		return "";
	}

	private String getStudyProgramNameFromProgramLinkRef(String ref) {
		if (ref.contains("DATA")) {
			return "Data";
		} else if (ref.contains("ELEKTRO")) {
			return "Elektro";
		}
		return "";
	}

	private StudentDataType getStudentDataTypeFromPersonType(PersonType person) {
		List<AffiliationDataType> affiliationData = person.getAffiliationData();
		for (AffiliationDataType affiliationDataType : affiliationData) {
			if(affiliationDataType instanceof StudentDataType) {
				return (StudentDataType)affiliationDataType;
			}	
		}
		return null;
	}

	private String getDepartmentCodeFromAcademicATList(List<AcademicAffiliationType> aatList) {		
		for (AcademicAffiliationType academicAffiliationType : aatList) {
			List<ProgramLinkType> programLink = academicAffiliationType.getProgramLink();
			for (ProgramLinkType programLinkType : programLink) {
				String ref = programLinkType.getRef();
				return getDepartmentCodeFromProgramLinkRef(ref);
			}
		}
		return "";
	}
	
	//TODO: Dette bør ikke være hardkodet inn, bør fikses. Burde være f.eks. Oe2 i stedet.
	private String getDepartmentCodeFromProgramLinkRef(String ref) {
		if (ref.contains("DATA") || ref.contains("ELEKTRO")) {
			return "TN-IDE";
		}
		return "";
	}

	private boolean personIsBachelorStudent(StudentDataType sdt) {
		List<AcademicAffiliationType> academicAT = sdt.getAcademicAffiliation();

		for (AcademicAffiliationType academicAffiliationType : academicAT) {
			List<TeachingLinkType> tLT = academicAffiliationType.getTeachingLink();
			for (TeachingLinkType teachingLinkType : tLT) {
				if (teachingLinkType.getRef().contains("BAC")) {
					return true;
				}
			}

		}		
		return false;
	}

	private Student getStudentFromAbamStudentList(String studentNumber) {
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
			removeAssignmentFromApplicationList(thesis.getAssignedAssignment());
			
		}
		
	}
	
	private void removeAssignmentFromApplicationList(
			Assignment assignedAssignment) {
		if(!applicationList.isEmpty()) {
			for (Application application: applicationList) {
				if(application.getAssignment().equals(assignedAssignment)) {
					applicationList.remove(application);
				}
			}
		}
	}

	public void updateThesis(Thesis thesisToUpdate) {
		for (Thesis thesis : savedThesesList) {
			if(thesis.equals(thesisToUpdate)) {
				savedThesesList.remove(thesis);
				savedThesesList.add(thesisToUpdate);
				updateThesisForInvolvedStudents(thesisToUpdate);
				return;
			}
		}
		
	}
	
	private void updateThesisForInvolvedStudents(Thesis thesisToUpdate) {
		Student std = getStudentFromStudentNumber(thesisToUpdate.getStudentNumber1());
		if(std != null) std.setAssignedThesis(thesisToUpdate);
		
		std = getStudentFromStudentNumber(thesisToUpdate.getStudentNumber2());
		if(std != null) std.setAssignedThesis(thesisToUpdate);
		
		std = getStudentFromStudentNumber(thesisToUpdate.getStudentNumber3());
		if(std != null) std.setAssignedThesis(thesisToUpdate);
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
		List<Thesis> listToArchive = new ArrayList<Thesis>();
		for (Thesis thesis : savedThesesList) {
			if(!thesis.isActive()) {
				listToArchive.add(thesis);
			}
		}
		if (!listToArchive.isEmpty()) archiveTheses(listToArchive);
		return savedThesesList;
	}
	
	public List<Thesis> getThesisListFromDepartmentCode(String depCode) {
		List<Thesis> listToReturn = new ArrayList<Thesis>();
		List<Thesis> listToArchive = new ArrayList<Thesis>();
		for (Thesis thesis : savedThesesList) {
			if(!thesis.isActive()) {
				listToArchive.add(thesis);
			} else if(thesis.getAssignedAssignment().getDepartmentCode().equalsIgnoreCase(depCode)) {
				listToReturn.add(thesis);
			}
		}
		if (!listToArchive.isEmpty()) archiveTheses(listToArchive);
		return listToReturn;
	}
	
	private void archiveTheses(List<Thesis> thesesList) {
		for (Thesis thesis : thesesList) {
			if(archivedThesesList.add(thesis)) {
				savedThesesList.remove(thesis);
			}
		}
	}
	
	public List<Thesis> getArchivedThesisListFromDepartmentCode(String depCode) {
		List<Thesis> listToReturn = new ArrayList<Thesis>();
		for (Thesis thesis : archivedThesesList) {
			if(thesis.getAssignedAssignment().getDepartmentCode().equalsIgnoreCase(depCode)) {
				listToReturn.add(thesis);
			}
		}
		return listToReturn;
	}
	
	public List<Thesis> getArchivedThesisListFromUisLoginName(String uisLoginName) {
		List<Thesis> listToReturn = new ArrayList<Thesis>();
		for (Thesis thesis : archivedThesesList) {
			if(thesis.getFacultySupervisor().getEmployeeId().equals(uisLoginName)) {
				listToReturn.add(thesis);
			}
		}
		return listToReturn;
	}
	
	public List<Thesis> getArchivedThesesList() {
		return archivedThesesList;
	}
	
	public void setDepartmentDao(DepartmentDAO departmentDao) {
		this.departmentDao = departmentDao;
	}

	public Employee getEmployeeFromUisLoginName(String loginName) {
		
		fillEmployeeListIfEmpty();
		for (Employee employee : employeeList) {
			if (employee.getEmployeeId().equals(loginName)) {
				return employee;
			}
		}
		return null;
	}

	public Employee getEmployeeFromFullName(String facultySupervisorName) {
		
		fillEmployeeListIfEmpty();
		
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
		
		if (foundEmployee.getName().equals("")) {
			EmployeeDAO edao = new EmployeeDAOImpl();
			Employee emp = edao.findEmployeeByEmployeeFullName(facultySupervisorName);
			if (emp != null) foundEmployee = emp;
		}
		
		return foundEmployee;
	}

	private void fillEmployeeListIfEmpty() {
		if (employeeList.isEmpty()) {
			EmployeeDAO edao = new EmployeeDAOImpl();
			employeeList = edao.getAllTNEmployeesFromLdap();
		}
	}
	
	private PersonWebService getWS() {
		InputStream cfgStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties props = new Properties();
		try {
			props.load(cfgStream);
		} catch (IOException e) {
			log.debug("I getWS: " + e.getMessage());
		}
		
		PersonService ps = new PersonService();
		PersonWebService port = ps.getPersonServicePort();
		
		String targetUrl = props.getProperty("personws.target.url");
		if (targetUrl != null) {
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					targetUrl);
		}

		return port;
	}

}
