package no.uis.abam.dom;

import java.util.LinkedList;
import java.util.List;


public class Department extends EditableSelectItem{
	private static final long serialVersionUID = 1L;

	private String name;
	
	private List<EditableSelectItem> studyPrograms;
	
	public Department(){
	}
	
	public Department(Integer value, String name) {
		super(value, name);
		this.name = name;
		studyPrograms = new LinkedList<EditableSelectItem>();
	}

	public List<EditableSelectItem> getStudyPrograms() {
		return studyPrograms;
	}

	public void setStudyPrograms(List<EditableSelectItem> studyPrograms) {
		this.studyPrograms = studyPrograms;
	}
	
	public void addStudyProgram(EditableSelectItem studyProgram) {
		studyPrograms.add(studyProgram);
	}
	
	public void removeStudyProgram(EditableSelectItem studyProgram) {
		studyPrograms.remove(studyProgram);
	}

	public String getName() {
		return name;
	}
}
