package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.jws.WebService;

import no.uis.abam.dao.DepartmentDAO;
import no.uis.abam.dao.EmployeeDAO;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.StudyProgram;
import no.uis.abam.dom.Thesis;
import no.uis.abam.util.LevenshteinDistance;
import no.uis.service.idm.ws.IdmWebService;
import no.uis.service.model.Person;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.transaction.CannotCreateTransactionException;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private Logger log = Logger.getLogger(AbamWebServiceTestImpl.class);
	
	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private List<Thesis> archivedThesesList = new ArrayList<Thesis>();
	private List<Department> departmentList;
	private List<Employee> employeeList = new ArrayList<Employee>();
	
	private DepartmentDAO departmentDao;
	private EmployeeDAO employeeDao;
 	
//	private PersonWebService personWebService;
	private IdmWebService idmService;
	
	public AbamWebServiceTestImpl(){
//		personWebService = getPersonWebService();
		//fillEmployeeListIfEmpty();
	}
	
	public TreeSet<Assignment> getAllAssignments() {
		return assignmentList;
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
	
	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);		
	}
	
	public Assignment getCustomAssignmentFromStudentNumber(String studentNumber) {
		Student std = getStudentFromStudentNumber(studentNumber);
		if (std != null) return std.getCustomAssignment();
		return null;
	}
	
	public Assignment getAssignmentFromId(int id) {
		for (Assignment assignment : assignmentList) {
			if(assignment.getId() == id){
				return assignment;
			}
		}
		return null;
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
				
		}
		return departmentList;
	}	
		
	private void initializeStudyPrograms(int departmentIndex) {
		
		List<StudyProgram> listToAdd = new ArrayList<StudyProgram>();
		if ( departmentList.get(departmentIndex).getOe2() == 3) {
			listToAdd = new ArrayList<StudyProgram>();
			listToAdd.add(new StudyProgram(new Integer(0), ""));
			listToAdd.add(new StudyProgram(new Integer(1), "Industriell Økonomi"));
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
	
	public List<StudyProgram> getStudyProgramListFromDepartmentIndex(int departmentIndex) {
		return departmentList.get(departmentIndex).getStudyPrograms();
	}

	public String getStudyProgramName(int departmentIndex, int studyProgramIndex) {
	  throw new NotImplementedException(getClass());
		//return getStudyProgramListFromDepartmentIndex(departmentIndex).get(studyProgramIndex).getName();
	}

	public String getDepartmentName(int index) {
		return  departmentList.get(index).getOeNavn_Engelsk();
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
	
	private void removeStudentsApplicationFromList(Student student) {
		for (Application application : student.getApplicationPriorityArray()) {
			if (application != null) {
				removeApplication(application);
			}
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
	
	private void archiveTheses(List<Thesis> thesesList) {
		for (Thesis thesis : thesesList) {
			if(archivedThesesList.add(thesis)) {
				savedThesesList.remove(thesis);
			}
		}
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
	
	public Employee getEmployeeFromUisLoginName(String loginName) {
		
		//fillEmployeeListIfEmpty();
	  return employeeDao.findEmployeeByEmployeeNumber(loginName);
	}
	
	private void fillEmployeeListIfEmpty() {
		if (employeeList.isEmpty()) {
			//employeeList = employeeDao.getAllTNEmployeesFromLdap();
		}
	}

	public Employee getEmployeeFromFullName(String facultySupervisorName) {
		
	  throw new NotImplementedException(getClass());
	  /*
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
			Employee emp = employeeDao.findEmployeeByEmployeeFullName(facultySupervisorName);
			if (emp != null) foundEmployee = emp;
		}
		
		return foundEmployee;
		*/
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
	  Person person = idmService.getPersonByStudentNumber(studentNumber);
	  return getStudentFromPersonTypeObject(person);
	}
	
	private Student getStudentFromPersonTypeObject(Person person) {
//		StudentDataType sdt = getStudentDataTypeFromPersonType(person);
		Student student = new Student();
		
		// TODO the logic behind this is not quite good
		List<no.uis.service.model.StudyProgram> programs = idmService.getProgramsForStudent(person.getUserId());
		boolean foundMaster = false;
		boolean foundBachelor = false;
		String programName = null;
		for (no.uis.service.model.StudyProgram prog : programs) {
      String progId = prog.getId();
      if (progId.startsWith("M-")) {
        foundMaster = true;
        programName = progId;
        break;
      } 
      if (progId.startsWith("B-")) {
        foundBachelor = true;
        programName = progId;
        break;
      }
    }
		
		if (foundMaster) {
		  student.setBachelor(false);
		} else if (foundBachelor) {
		  student.setBachelor(true);
		} else {
		  throw new java.lang.IllegalStateException("neither student nor master");
		}
		student.setDepartmentCode(person.getPrimaryOrgUnit());
		
		student.setName(person.getFullName());
		student.setStudentNumber(person.getUserId());
		student.setStudyProgramName(programName);
		return student;
	}
	
//	private StudentDataType getStudentDataTypeFromPersonType(Person person) {
//		List<AffiliationDataType> affiliationData = person.getAffiliationData();
//		for (AffiliationDataType affiliationDataType : affiliationData) {
//			if(affiliationDataType instanceof StudentDataType) {
//				return (StudentDataType)affiliationDataType;
//			}	
//		}
//		return null;
//	}
	
//	private boolean personIsBachelorStudent(StudentDataType sdt) {
//		List<AcademicAffiliationType> academicAT = sdt.getAcademicAffiliation();
//
//		for (AcademicAffiliationType academicAffiliationType : academicAT) {
//			List<TeachingLinkType> tLT = academicAffiliationType.getTeachingLink();
//			for (TeachingLinkType teachingLinkType : tLT) {
//				if (teachingLinkType.getRef().contains("BAC")) {
//					return true;
//				}
//			}
//
//		}		
//		return false;
//	}
	
//	private String getDepartmentCodeFromAcademicATList(List<AcademicAffiliationType> aatList) {		
//		for (AcademicAffiliationType academicAffiliationType : aatList) {
//			List<ProgramLinkType> programLink = academicAffiliationType.getProgramLink();
//			for (ProgramLinkType programLinkType : programLink) {
//				String ref = programLinkType.getRef();
//				return getDepartmentCodeFromProgramLinkRef(ref);
//			}
//		}
//		return "";
//	}
//	
	//TODO: Dette bør ikke være hardkodet inn, bør fikses. Burde være f.eks. Oe2 i stedet.
	private String getDepartmentCodeFromProgramLinkRef(String ref) {
		if (ref.contains("DATA") || ref.contains("ELEKTRO")) {
			return "TN-IDE";
		}
		return "";
	}
	
//	private String getStudyProgramNameFromAcademicATList(
//			List<AcademicAffiliationType> academicAffiliation) {
//		for (AcademicAffiliationType academicAffiliationType : academicAffiliation) {
//			List<ProgramLinkType> programLink = academicAffiliationType.getProgramLink();
//			for (ProgramLinkType programLinkType : programLink) {
//				String ref = programLinkType.getRef();
//				return getStudyProgramNameFromProgramLinkRef(ref);
//			}
//		}
//		return "";
//	}
//
	private String getStudyProgramNameFromProgramLinkRef(String ref) {
		if (ref.contains("DATA")) {
			return "Data";
		} else if (ref.contains("ELEKTRO")) {
			return "Elektro";
		}
		return "";
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
		
	public void setDepartmentDao(DepartmentDAO departmentDao) {
		this.departmentDao = departmentDao;
	}

	public void setEmployeeDao(EmployeeDAO employeeDao) {
    this.employeeDao = employeeDao;
  }

  public void setIdmService(IdmWebService idmService) {
	  this.idmService = idmService;
	}
//	private PersonWebService getPersonWebService() {
//		InputStream cfgStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
//		Properties props = new Properties();
//		try {
//			props.load(cfgStream);
//		} catch (IOException e) {
//			log.debug("In getPersonWebService: " + e.getMessage());
//		}
//		
//		PersonService ps = new PersonService();
//		PersonWebService port = ps.getPersonServicePort();
//		
//		String targetUrl = props.getProperty("personws.target.url");
//		if (targetUrl != null) {
//			BindingProvider bp = (BindingProvider) port;
//			bp.getRequestContext().put(
//					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//					targetUrl);
//		}
//
//		return port;
//	}

}
