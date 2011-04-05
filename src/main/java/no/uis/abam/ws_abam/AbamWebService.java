package no.uis.abam.ws_abam;

import java.util.List;
import java.util.TreeSet;

import javax.jws.WebService;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.StudyProgram;
import no.uis.abam.dom.Thesis;

@WebService
public interface AbamWebService {

	public TreeSet<Assignment> getAllAssignments();
	public void setAssignmentList(TreeSet<Assignment> assignmentList);
	public TreeSet<Assignment> getAssignmentsFromDepartmentCode(String departmentName);
	public TreeSet<Assignment> getActiveAssignments();
	
	
	public void saveAssignment(
			//@WebParam(targetNamespace="http://localhost/AbamWebService/AbamWebService", 
            //name="assignmentToSave",mode=Mode.IN)
            Assignment assignment);
	public void removeAssignment(Assignment assignment);
	
	public List<Department> getDepartmentList();
	public void setDepartmentList(List<Department> departmentList);
	
	public List<StudyProgram> getStudyProgramList(int departmentIndex);
	
	public String getStudyProgram(int departmentIndex, int studyProgramIndex);
	public String getDepartment(int index); 
	
	public List<Application> getApplicationList();
	public List<Application> getMasterApplicationListFromDepartmentCode(String code);
	public List<Application> getBachelorApplicationListFromDepartmentCode(String code);
	public void setApplicationList(List<Application> applicationList);
	
	public void saveApplication(Application application);
	public void removeApplication(Application application);
	
	public int getNextId();
	public void updateApplicationsFromCurrentStudent(
			Application[] tempApplicationPriorityArray);
	
	public Student getStudentFromStudentNumber(long studentNumber);
	
	public void addThesesFromList(List<Thesis> thesesToAdd);
	public Assignment getAssignmentFromId(int id);
	public void updateStudent(Student studentToUpdate);
	public List<Thesis> getThesisList();
	public void updateThesis(Thesis thesisToUpdate);
	public Employee getEmployeeFromUisLoginName(String loginName);
	public Employee getEmployeeFromFullName(String facultySupervisorName);
}
