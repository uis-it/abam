package no.uis.abam.ws_abam;

import java.util.List;
import java.util.TreeSet;

import javax.jws.WebParam;
import javax.jws.WebService;

import no.uis.abam.dom.*;
import no.uis.service.model.AffiliationType;
import no.uis.service.model.Organization;

@WebService
public interface AbamWebService {

	
	/**
	 * @return a TreeSet containing all Assignment objects
	 */
	public List<Assignment> getAllAssignments();
	
	/**
	 * @param departmentCode - Which Department to return Assignments from
	 * @return a TreeSet containing all Assignment objects for the given Department
	 */
	public List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode);
	
	/**
	 * @return a TreeSet containing all active Assignment objects
	 */
	public List<Assignment> getActiveAssignments();
	
	/**
	 * @param assignment - Assignment that should be saved
	 */
	public void saveAssignment(@WebParam(name="assignment") Assignment assignment);
	
	/**
	 * @param assignment - Assignment that should be removed
	 */
	public void removeAssignment(@WebParam(name="assignment") Assignment assignment);
	
	/**
	 * @param studentNumber - Student to get custom Assignment from
	 * @return the custom Assignment of the given Student
	 */
	public Assignment getCustomAssignmentFromStudentNumber(@WebParam(name="studentNumber") String studentNumber);
	
	/**
	 * @param id to find Assignment by
	 * @return Assignment with given id or null if not found
	 */
	public Assignment getAssignmentFromId(@WebParam(name="assignmentId") long id);
	
	/**
	 * @return a List containing Department objects
	 */
	public List<Organization> getDepartmentList();
	
	/**
	 * @return A List with all Application objects
	 */
	public List<Application> getApplicationList();
	
	/**
	 * @param code of the department 
	 * @return A list with all master Applications on the given department
	 */
	public List<Application> getMasterApplicationListFromDepartmentCode(@WebParam(name="departmentCode") String code);
	
	/**
	 * @param code of the department
	 * @return A list with all bachelor Applications on the given department
	 */
	public List<Application> getBachelorApplicationListFromDepartmentCode(@WebParam(name="departmentCode") String code);
	
	/**
	 * @param application object that will be saved
	 */
	public void saveApplication(@WebParam(name="application") Application application);
	
	/**
	 * @param application object that will be removed
	 */
	public void removeApplication(@WebParam(name="application") Application application);
	
	/**
	 * @return next available id, assignmentList size plus one
	 */
	public int getNextId();
	
	/**
	 * @param thesesToAdd a List of Thesis object that should be added
	 */
	public void addThesesFromList(@WebParam(name="thesis") List<Thesis> thesesToAdd);

	/**
	 * @return a List with all active Thesis objects	  
	 */
	public List<Thesis> getThesisList();
	
	/**
	 * @param depCode of the department
	 * @return a List with all theses of the given department
	 */
	public List<Thesis> getThesisListFromDepartmentCode(@WebParam(name="departmentCode") String depCode);
	
	/**
	 * @param thesisToUpdate Thesis object that needs to be updated
	 */
	public void updateThesis(@WebParam(name="thesis") Thesis thesisToUpdate);
	
	/**
	 * @param departmentCode
	 * @return A List of Thesis objects that are archived for the given department
	 */
	public List<Thesis> getArchivedThesisListFromDepartmentCode(@WebParam(name="deparmentCode") String departmentCode);
	
	/**
	 * @param uisLoginName - employee id
	 * @return A List of Thesis objects that are archived for the given employee
	 */
	public List<Thesis> getArchivedThesisListFromUisLoginName(@WebParam(name="loginName") String uisLoginName);
	
	/**
	 * @param loginName - id of the employee 
	 * @return Employee object if found, null if not found
	 */
	public Employee getEmployeeFromUisLoginName(@WebParam(name="loginName") String loginName);
	
	/**
	 * @param name of the employee
	 * @return returns a Employee object for the correct employee if name is found, if not it returns a Employee object with empty name.
	 */
	@Deprecated
	public Employee getEmployeeFromFullName(@WebParam(name="name") String name);

	/**
	 * @param tempApplicationPriorityArray a array of Applications to update
	 */
	public void updateApplications(@WebParam(name="application") List<Application> applicationList);
	
	/**
	 * @param studentNumber
	 * @return Student object for given studentNumber
	 */
	public Student getStudentFromStudentNumber(@WebParam(name="studentNumber") String studentNumber);
	
	/**
	 * @param studentToUpdate - Student object with new information that needs to be saved in webservice
	 * @return 
	 */
	public Student updateStudent(@WebParam(name="student") Student studentToUpdate);

  public List<no.uis.service.model.StudyProgram> getStudyProgramsFromDepartmentFSCode(@WebParam(name="departmentCode") String departmentCode);
	
  public no.uis.service.model.StudyProgram getStudyProgramFromCode(@WebParam(name="programCode") String programCode);

  public List<AffiliationType> getAffiliation(@WebParam(name="employeeId") String employeeId);

  public Organization getEmployeeDeptarment(@WebParam(name="employeeId") String employeeId);
}
