package no.uis.abam.ws_abam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import no.uis.abam.dom.AbamGroup;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.service.idm.ws.IdmWebService;
import no.uis.service.model.AffiliationType;
import no.uis.service.model.Contact;
import no.uis.service.model.Email;
import no.uis.service.model.Organization;
import no.uis.service.model.Person;
import no.uis.service.model.PhoneNumber;
import no.uis.service.model.TypeOfEmail;
import no.uis.service.model.TypeOfPhoneNumber;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceImpl implements AbamWebService {

	private Logger log = Logger.getLogger(AbamWebServiceImpl.class);
	
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
	}
	
  @Override
	public void saveApplication(Application application) {
    dao.saveApplication(application);
	}
	
  @Override
	public void removeApplication(Application application) {
    dao.removeApplication(application);
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
		  thesis = dao.saveThesis(thesis);
		  
			Student student = getStudentFromStudentNumber(thesis.getStudentNumber1());
			student.setAssignedThesis(thesis);

			student.getApplications().clear();
			student = dao.saveStudent(student);
			
			removeStudentsApplication(student);
			
			if (thesis.getStudentNumber2() != null && !thesis.getStudentNumber2().isEmpty()) {
				student = getStudentFromStudentNumber(thesis.getStudentNumber2());
				student.setAssignedThesis(thesis);			
				removeStudentsApplication(student);
			}
			
			if (thesis.getStudentNumber3() != null && !thesis.getStudentNumber3().isEmpty()) {
				student = getStudentFromStudentNumber(thesis.getStudentNumber3());
				student.setAssignedThesis(thesis);			
				removeStudentsApplication(student);
			}
		}
	}
	
	private void removeStudentsApplication(Student student) {
		for (Application application : student.getApplications()) {
			removeApplication(application);
		}			
		student.getApplications().clear();
		dao.saveStudent(student);
	}
	
	@Override
	public List<Thesis> getThesisList() {
	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.MONTH, 4);
	  return dao.getThesisesBeforeEvaluationDeadline(cal, null);
	}
	
	@Override
	public List<Thesis> getThesisListFromDepartmentCode(String depCode) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, 4);
    return dao.getThesisesBeforeEvaluationDeadline(cal, depCode);
	}

	@Override
	public void updateThesis(Thesis thesisToUpdate) {
	  dao.saveThesis(thesisToUpdate);
		updateThesisForInvolvedStudents(thesisToUpdate);
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
	  Calendar now = Calendar.getInstance();
	  now.set(Calendar.MONTH, 4);
	  return dao.getThesisesAfterEvaluationDeadline(now, depCode, null);
	}
	
	@Override
	public List<Thesis> getArchivedThesisListFromUisLoginName(String employeeId) {
    Calendar now = Calendar.getInstance();
    now.set(Calendar.MONTH, 4);
    return dao.getThesisesAfterEvaluationDeadline(now, null, employeeId);
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
    Student student = null;
    try {
      Person person = idmService.getPersonByStudentNumber(studentNumber);
      student = getStudentFromPersonTypeObject(person);
    } catch(Exception ex) {
      log.info(studentNumber, ex);
    }
	  return student;
	}
	
	private Student getStudentFromPersonTypeObject(Person person) {
		String email = null;
		if (!person.getAffiliations().contains(AffiliationType.STUDENT)) {
		  return null;
		}
		for(Contact contact: person.getContact()) {
		  if (contact instanceof Email) {
		    Email mail = (Email)contact;
		    if (mail.getTypeOfEmail().equals(TypeOfEmail.INTERNAL)) {
		      email = mail.getEmailAddress();
		      break;
		    } else if (email == null) {
		      email = mail.getEmailAddress();
		    }
		  }
		}

    Student student = dao.findOrCreateStudent(person.getUserId(), person.getFullName(), email);
		
		// TODO the logic behind this is not quite good
		List<no.uis.service.model.StudyProgram> programs = idmService.getProgramsForStudent(person.getUserId());
		boolean foundMaster = false;
		boolean foundBachelor = false;
		String programCode = null;
		for (no.uis.service.model.StudyProgram prog : programs) {
      String progId = prog.getId();
      if (progId.startsWith("M-")) {
        foundMaster = true;
        programCode = progId;
        break;
      } 
      if (progId.startsWith("B-")) {
        foundBachelor = true;
        programCode = progId;
        break;
      }
    }
		
		if (foundMaster) {
		  student.setType(AssignmentType.MASTER);
		} else if (foundBachelor) {
      student.setType(AssignmentType.BACHELOR);
		} else {
		  throw new java.lang.IllegalStateException("neither bachelor nor master student");
		}
		Organization org = idmService.getOrganizationByDN(person.getPrimaryOrgUnit());
		if (org != null) {
		  student.setDepartmentCode(org.getPlaceRef());
		  student.setDepartmentName(org.getPlaceRef());
		}
		
		student.setStudyProgramName(programCode);
		student.setStudyProgramCode(programCode);
		student = dao.saveStudent(student);
		return student;
	}
	
  @Override
	public Student updateStudent(Student studentToUpdate) {
    return dao.saveStudent(studentToUpdate);
//		for (Student student : studentList) {
//			if (student.equals(studentToUpdate)) {
//				studentList.remove(student);
//				studentList.add(studentToUpdate);
//				return;
//			}
//		}
	}
		
  public void setIdmService(IdmWebService idmService) {
	  this.idmService = idmService;
	}
  
  private Employee convertPersonToEmployee(Person person) {
    
    String email = null;
    String phone = null;
    for (Contact contact : person.getContact()) {
      if (contact instanceof Email) {
        Email mail = (Email)contact;
        TypeOfEmail mailType = mail.getTypeOfEmail();
        if (mailType.equals(TypeOfEmail.INTERNAL) || mailType.equals(TypeOfEmail.WORK)) {
          email = mail.getEmailAddress();
        }
      } else if(contact instanceof PhoneNumber) {
        PhoneNumber phoneNumber = (PhoneNumber)contact;
        TypeOfPhoneNumber phoneType = phoneNumber.getTypeOfPhoneNumber();
        if (phoneType.equals(TypeOfPhoneNumber.OFFICE)) {
          phone = phoneNumber.getPhoneNumber();
        }
      }
      if (email != null && phone != null) {
        break;
      }
    }
    Employee employee = dao.findOrCreateEmployee(person.getUserId(), person.getFullName(), email, phone);

    List<String> orgUnits = person.getOrgUnits();
    for (String orgUnit : orgUnits) {
      Organization org = idmService.getOrganizationByDN(orgUnit);
      
      employee.getGroups().add(findGroup(org));
    }
    return employee;
  }

  private AbamGroup findGroup(Organization org) {
    return dao.findOrCreateGroup(org.getPlaceRef());
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
