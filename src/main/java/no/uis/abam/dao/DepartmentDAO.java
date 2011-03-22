package no.uis.abam.dao;

import java.util.List;

import no.uis.abam.dom.Department;
import no.uis.abam.dom.StudyProgram;

public interface DepartmentDAO {

	public abstract List<Department> getDepartments();

	public abstract List<StudyProgram> getStudyPrograms(int departmentId);

}