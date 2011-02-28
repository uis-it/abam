package no.uis.abam.ws_abam;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jws.WebService;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.EditableSelectItem;

@WebService
public interface AbamWebService {

	public TreeSet<Assignment> getAllAssignments();
	public void setAssignmentList(TreeSet<Assignment> assignmentList);
	public TreeSet<Assignment> getAssignmentsFromDepartmentName(String departmentName);
	
	public void saveAssignment(Assignment assignment);
	public void removeAssignment(Assignment assignment);
	
	public LinkedList<Department> getDepartmentList();
	public void setDepartmentList(LinkedList<Department> departmentList);
	
	public List<EditableSelectItem> getStudyProgramList(int departmentIndex);
	
	public String getStudyProgram(int departmentIndex, int studyProgramIndex);
	public String getDepartment(int index); 
	
	public List<Application> getApplicationList();
	public void setApplicationList(List<Application> applicationList);
	
	public void saveApplication(Application application);
	public void removeApplication(Application application);
	
	public int getNextId();
}
