package no.uis.portal.student;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import no.uis.portal.student.domain.Application;
import no.uis.portal.student.domain.Assignment;
import no.uis.portal.student.domain.Student;

public interface StudentService {

	public abstract void actionClearStudyProgramAndDepartmentNumber(
			ActionEvent event);
	
	public abstract void actionRemoveApplication(ActionEvent event);
	
	public abstract void actionSetApplicationPriorityHigher(ActionEvent event);
	
	public abstract void actionSetApplicationPriorityLower(ActionEvent event);

	public abstract void actionUpdateStudyProgramList(ValueChangeEvent event);

	public abstract void actionUpdateStudyProgramListFromCreateAssignment(
			ValueChangeEvent event);

	public abstract void actionSetDisplayAssignment(ValueChangeEvent event);

	public abstract void setAllEditExternalExaminerToFalse();
	
	public abstract void saveAssignment(Assignment assignment);
	
	public abstract void removeAssignment(Assignment assignment);
	
	public abstract void setApplicationToStudent(Application application);
	
	public abstract void setApplicationToAssignment(Application application);

	public abstract TreeSet<Assignment> getAssignmentList();

	public abstract Assignment getSelectedAssignment();

	public abstract void setSelectedAssignment(Assignment selectedAssignment);
	
	public abstract LinkedList<SelectItem> getDepartmentList();

	public abstract void setDepartmentList(LinkedList<SelectItem> instituteList);

	public abstract int getSelectedDepartmentNumber();

	public abstract void setSelectedDepartmentNumber(int selectedInstituteNumber);

	public abstract int getSelectedStudyProgramNumber();

	public abstract void setSelectedStudyProgramNumber(
			int selectedStudyProgramNumber);
	
	public abstract LinkedList<SelectItem> getStudyProgramList();

	public abstract void setStudyProgramList(LinkedList<SelectItem> studyProgramList);

	public abstract LinkedList<LinkedList<SelectItem>> getAllStudyProgramsByDepartmentsList();

	public abstract void setAllStudyProgramsByDepartmentList(
			LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesListIn);

	public abstract String getStudyProgram(int index);

	public abstract String getDepartment(int index);

	public abstract String getSelectedDepartment();

	public abstract void setSelectedDepartment(String selectedInstitute);

	public abstract void setStudyProgramListFromDepartmentNumber(
			int instituteNumber);

	public abstract int getNextId();
	
	public abstract Student getCurrentStudent();
	
	public abstract void setCurrentStudent(Student currentStudent);
	
	public abstract void setApplicationList(ArrayList<Application> applicationList);
	
	public abstract ArrayList<Application> getApplicationList();
}