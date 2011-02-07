package no.uis.portal.employee;

import java.util.LinkedList;
import java.util.TreeSet;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import no.uis.portal.employee.domain.Assignment;

public interface EmployeeService {

	public abstract int getSelectedInstituteNumber();

	public abstract void setSelectedInstituteNumber(int selectedInstituteNumber);

	public abstract int getSelectedStudyProgramNumber();

	public abstract void setSelectedStudyProgramNumber(
			int selectedStudyProgramNumber);

	public abstract void createTestData();

	public abstract int getNextId();

	public abstract void saveAssignment(Assignment assignment);

	public abstract TreeSet<Assignment> getAssignmentList();

	public abstract EmployeeService getSelectedAssignment();

	public abstract void setSelectedAssignment(
			EmployeeService selectedAssignment);

	public abstract void actionClearStudyProgramAndInstituteNumber(
			ActionEvent event);

	public abstract void actionUpdateStudyProgramList(ValueChangeEvent event);

	public abstract void actionUpdateStudyProgramListFromCreateAssignment(
			ValueChangeEvent event);

	public abstract void actionSetDisplayAssignment(ValueChangeEvent event);

	public abstract LinkedList<SelectItem> getInstituteList();

	public abstract void setInstituteList(LinkedList<SelectItem> instituteList);

	public abstract LinkedList<SelectItem> getStudyProgramList();

	public abstract void setStudyProgramList(
			LinkedList<SelectItem> studyProgramList);

	public abstract LinkedList<LinkedList<SelectItem>> getAllStudyProgramsByInstitutesList();

	public abstract void setAllStudyProgramsByInstitutesList(
			LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesListIn);

	public abstract String getStudyProgram(int index);

	public abstract String getInstitute(int index);

	public abstract String getSelectedInstitute();

	public abstract void setSelectedInstitute(String selectedInstitute);

	public abstract void removeAssignment(Assignment assignment);

}