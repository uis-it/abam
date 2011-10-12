package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jws.WebService;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.service.idm.ws.IdmWebService;
import no.uis.service.model.AffiliationType;
import no.uis.service.model.BaseText;
import no.uis.service.model.Organization;
import no.uis.service.model.Person;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceImpl implements AbamWebService {

	private Logger log = Logger.getLogger(AbamWebServiceImpl.class);
	
//	private List<Application> applicationList = new ArrayList<Application>();

	private List<Student> studentList = new ArrayList<Student>();
	private List<Thesis> savedThesesList = new ArrayList<Thesis>();
	private List<Thesis> archivedThesesList = new ArrayList<Thesis>();
	
	private List<Organization> departmentList;
	private String orgTreeRoot = "217_8_0_0"; // faculty TN 
 	
	private IdmWebService idmService;

	private AbamDao dao;
	
	public AbamWebServiceImpl() {
	}

	public void setAbamDao(AbamDao dao) {
	  this.dao = dao;
	}

	@Override
	public List<Assignment> getAllAssignments() {
	  return dao.getAssignments();
	}	
	
	@Override
	public List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode) {
	  return dao.getAssignmentsFromDepartmentCode(departmentCode);
//		TreeSet<Assignment> assignmentsToReturn = new TreeSet<Assignment>();
//		for (Assignment assignment : assignmentList) {
//			if(assignment.getDepartmentCode().equalsIgnoreCase(departmentCode)){
//				assignmentsToReturn.add(assignment);
//			}
//		}
//		return assignmentsToReturn;
	}

	@Override
	public List<Assignment> getActiveAssignments() {
	  return dao.getActiveAssignments();
//		TreeSet<Assignment> activeAssignments = new TreeSet<Assignment>();
//		for (Assignment assignment : assignmentList) {
//			if(!assignment.isExpired()) {
//			  activeAssignments.add(assignment);
//			}
//		}
//		return activeAssignments;
	}
	
	@Override
	public void saveAssignment(Assignment assignment) {
	  dao.saveAssignment(assignment);
	}
	
	@Override
	public void removeAssignment(Assignment assignment) {
	  dao.removeAssignment(assignment);
	}
	
	@Override
	public Assignment getCustomAssignmentFromStudentNumber(String studentNumber) {
		Student std = getStudentFromStudentNumber(studentNumber);
		if (std != null) {
		  return std.getCustomAssignment();
		}
		return null;
	}
	
	@Override
	public Assignment getAssignmentFromId(long id) {
	  return dao.getAssignment(id);
	}
	
	@Override
	public List<Organization> getDepartmentList() {
		if (departmentList == null) {
		  Organization orgTree = idmService.getOrgTree(this.orgTreeRoot);
		  List<Organization> children = orgTree.getChildren();
		  List<Organization> depList = new ArrayList<Organization>(children.size()+1);
		  depList.addAll(children);
		  depList.add(new Organization());
			departmentList = depList;
		}
		return departmentList;
	}	
		
	@Override
	public List<Application> getApplicationList() {
		return dao.getApplications();
	}
	
  @Override
	public List<Application> getMasterApplicationListFromDepartmentCode(String departmentCode) {
    return dao.getApplicationsByDepartmentCode(departmentCode, AssignmentType.MASTER);
//		List<Application> masterApplicationList = new ArrayList<Application>();
//		for (Application application : applicationList) {
//		  boolean isMaster = application.getAssignment().getType().equals(AssignmentType.MASTER);
//			if(isMaster && application.getAssignment().getDepartmentCode().equals(code)) {
//				masterApplicationList.add(application);
//			}
//		}
//		return masterApplicationList;
	}

  @Override
	public List<Application> getBachelorApplicationListFromDepartmentCode(String departmentCode) {
    return dao.getApplicationsByDepartmentCode(departmentCode, AssignmentType.BACHELOR);
//		List<Application> bachelorApplicationList = new ArrayList<Application>();
//		for (Application application : applicationList) {
//		  boolean isBachelor = application.getAssignment().getType().equals(AssignmentType.BACHELOR); 
//			if(isBachelor && application.getAssignment().getDepartmentCode().equals(code)) {
//				bachelorApplicationList.add(application);
//			}
//		}
//		return bachelorApplicationList;
	}
	
  @Override
	public void saveApplication(Application application) {
    dao.saveApplication(application);
//		Iterator<Application> iterator = applicationList.iterator();
//		while (iterator.hasNext()){	
//			Application app = iterator.next();
//			if(app != null) {
//				if (app.equals(application)) {
//					applicationList.remove(app);
//					break;
//				}
//			}
//		}
//		applicationList.add(application);
	}
	
  @Override
	public void removeApplication(Application application) {
    dao.removeApplication(application);
//		for (Application app : applicationList) {
//			if (app.equals(application)) {
//				applicationList.remove(app);
//				return;
//			}
//		}	
	}

  @Deprecated
  @Override
	public int getNextId() {
    throw new NotImplementedException(getClass());
//		return assignmentList.size()+1;
	}

  @Override
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
		for (Application application : student.getApplications()) {
			if (application != null) {
				removeApplication(application);
			}
		}			
	}
	
	private void removeAssignmentFromApplicationList(Assignment assignedAssignment) {
	  throw new NotImplementedException(getClass());
//		if(!applicationList.isEmpty()) {
//			for (Application application: applicationList) {
//				if(application.getAssignment().equals(assignedAssignment)) {
//					applicationList.remove(application);
//				}
//			}
//		}
	}

	@Override
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
	
	@Override
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

	@Override
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
	
	@Override
	public List<Thesis> getArchivedThesisListFromDepartmentCode(String depCode) {
		List<Thesis> listToReturn = new ArrayList<Thesis>();
		for (Thesis thesis : archivedThesesList) {
			if(thesis.getAssignedAssignment().getDepartmentCode().equalsIgnoreCase(depCode)) {
				listToReturn.add(thesis);
			}
		}
		return listToReturn;
	}
	
	@Override
	public List<Thesis> getArchivedThesisListFromUisLoginName(String uisLoginName) {
		List<Thesis> listToReturn = new ArrayList<Thesis>();
		for (Thesis thesis : archivedThesesList) {
			if(thesis.getFacultySupervisor().getEmployeeId().equals(uisLoginName)) {
				listToReturn.add(thesis);
			}
		}
		return listToReturn;
	}
	
  @Override
	public Employee getEmployeeFromUisLoginName(String loginName) {
    Person person = idmService.getPersonByEmployeeNumber(loginName);
    if (person != null) {
      return convertPersonToEmployee(person);
    }
    return null;
	}
	
	/*
	 * TODO use employee number for employees and zero for external persons 
	 */
  @Override
	public Employee getEmployeeFromFullName(String fullName) {

    try {
      Person p = idmService.findPersonByFullName(fullName);
      if (p != null) {
        return convertPersonToEmployee(p);
      }
    } catch (Exception e) {
      log.warn(fullName, e);
    }
    Employee employee = new Employee();
    employee.setName("");

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
	
  @Override
	public void updateApplications(List<Application> applications) {
    for (Application application : applications) {
      saveApplication(application);
    }
	}
		
  @Override
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
		  student.setType(AssignmentType.MASTER);
		} else if (foundBachelor) {
      student.setType(AssignmentType.BACHELOR);
		} else {
		  throw new java.lang.IllegalStateException("neither student nor master");
		}
		Organization org = idmService.getOrganizationByDN(person.getPrimaryOrgUnit());
		if (org != null) {
		  student.setDepartmentCode(org.getPlaceRef());
		  student.setDepartmentName(getText(org.getName(), null));
		}
		
		student.setName(person.getFullName());
		student.setStudentNumber(person.getUserId());
		student.setStudyProgramName(programName);
		
		return student;
	}
	
  @Override
	public void updateStudent(Student studentToUpdate) {
		for (Student student : studentList) {
			if (student.equals(studentToUpdate)) {
				studentList.remove(student);
				studentList.add(studentToUpdate);
				return;
			}
		}
	}
		
  public void setIdmService(IdmWebService idmService) {
	  this.idmService = idmService;
	}
  
  private String getText(List<BaseText> txtList, String lang) {
    if (lang == null) {
      lang = Locale.getDefault().getLanguage();
    }
    for (BaseText txt : txtList) {
      if (txt.getLang().equals(lang)) {
        return txt.getValue();
      }
    }
    return txtList.get(0).getValue();
  }
  
  private Employee convertPersonToEmployee(Person person) {
    Employee employee = new Employee();
    employee.setName(person.getFullName());
    employee.setEmployeeId(person.getUserId());

    List<Organization> gl = new ArrayList<Organization>();
    List<String> orgUnits = person.getOrgUnits();
    for (String orgUnit : orgUnits) {
      Organization org = idmService.getOrganizationByDN(orgUnit);
      gl.add(org);
    }
    // TODO either save the DN, the ID or the Organization object
    employee.setGroupMembership(orgUnits);
    return employee;
  }

  @Override
  public List<no.uis.service.model.StudyProgram> getStudyProgramsFromDepartmentFSCode(String departmentCode) {
    List<no.uis.service.model.StudyProgram> idmProgs = idmService.getProgramsForOrganization(departmentCode);
    return idmProgs;
  }

  @Override
  public no.uis.service.model.StudyProgram getStudyProgramFromCode(String programCode) {
    return idmService.getStudyProgramByCode(programCode);
  }

  @Override
  public List<AffiliationType> getAffiliation(String employeeId) {
    Person person = idmService.getPersonByEmployeeNumber(employeeId);
    return person.getAffiliations();
  }

  @Override
  public Organization getEmployeeDeptarment(String employeeId) {
    Person person = idmService.getPersonByEmployeeNumber(employeeId);
    String orgDn = person.getPrimaryOrgUnit();
    return idmService.getOrganizationByDN(orgDn);
  }
}
