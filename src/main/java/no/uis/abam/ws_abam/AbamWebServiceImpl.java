package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import no.uis.service.model.BaseText;
import no.uis.service.model.Organization;
import no.uis.service.model.Person;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.transaction.CannotCreateTransactionException;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceImpl implements AbamWebService {

	private Logger log = Logger.getLogger(AbamWebServiceImpl.class);
	
	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>();
	private List<Application> applicationList = new ArrayList<Application>();
	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private List<Thesis> archivedThesesList = new ArrayList<Thesis>();
	private List<Department> departmentList;
	private List<Employee> employeeList = new ArrayList<Employee>();
	
	private DepartmentDAO departmentDao;
	private EmployeeDAO employeeDao;
 	
	private IdmWebService idmService;
	
	public AbamWebServiceImpl(){
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
				supplyStudyPrograms(departmentList, idmService);
				Department blankDepartment = new Department(0, "");
				blankDepartment.setOeKode("");
				departmentList.add(0, blankDepartment);
			}
		} catch (CannotCreateTransactionException e) {
				
		}
		return departmentList;
	}	
		
	private static void supplyStudyPrograms(List<Department> departments, IdmWebService idmService) {
	  for (Department department : departments) {
	    String departmentCode = getDepartmentFSCode(department);
	    List<no.uis.service.model.StudyProgram> progs = idmService.getProgramsForOrganization(departmentCode);
	    List<StudyProgram> abamProgs = new ArrayList<StudyProgram>(progs.size());
	    int count = 0; 
	    for (no.uis.service.model.StudyProgram prog : progs) {
	      
        abamProgs.add(new StudyProgram(count++, prog.getName().get(0).getValue()));
      }
      department.setStudyPrograms(abamProgs);
    }
  }

	
  private static String getDepartmentFSCode(Department department) {
    StringBuilder sb = new StringBuilder();
    sb.append(department.getOeInstKode());
    sb.append('_');
    sb.append(department.getOe1());
    sb.append('_');
    sb.append(department.getOe2());
    sb.append('_');
    sb.append(department.getOe3());
    return sb.toString();
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
		if (!listToArchive.isEmpty()) {
		  archiveTheses(listToArchive);
		}
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
		if(std != null) {
		  std.setAssignedThesis(thesisToUpdate);
		}
		
		std = getStudentFromStudentNumber(thesisToUpdate.getStudentNumber2());
		if(std != null) {
		  std.setAssignedThesis(thesisToUpdate);
		}
		
		std = getStudentFromStudentNumber(thesisToUpdate.getStudentNumber3());
		if(std != null) {
		  std.setAssignedThesis(thesisToUpdate);
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
	
	public Employee getEmployeeFromUisLoginName(String loginName) {
		
	  return employeeDao.findEmployeeByEmployeeNumber(loginName);
	}
	
	/*
	 * TODO use employee number for employees and zero for external persons 
	 */
	public Employee getEmployeeFromFullName(String facultySupervisorName) {

	  Employee employee = employeeDao.findEmployeeByEmployeeFullName(facultySupervisorName);
	  if (employee == null) {
	    employee = new Employee();
	    employee.setName("");
	  }
    return employee;
	  //throw new NotImplementedException(getClass());
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
		Organization org = idmService.getOrganizationByDN(person.getPrimaryOrgUnit());
		if (org != null) {
		  student.setDepartmentCode(org.getAcronym());
		  student.setDepartmentName(getDefaultText(org.getName()));
		}
		
		student.setName(person.getFullName());
		student.setStudentNumber(person.getUserId());
		student.setStudyProgramName(programName);
		
		return student;
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
  
  private String getDefaultText(List<BaseText> txtList) {
    String lang = Locale.getDefault().getLanguage();
    for (BaseText txt : txtList) {
      if (txt.getLang().equals(lang)) {
        return txt.getValue();
      }
    }
    return txtList.get(0).getValue();
  }
}
