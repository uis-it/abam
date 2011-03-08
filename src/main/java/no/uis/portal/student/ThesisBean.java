package no.uis.portal.student;

import javax.faces.event.ActionEvent;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Thesis;

import com.icesoft.faces.context.DisposableBean;

public class ThesisBean implements DisposableBean {

	private StudentService studentService;
	
	private Thesis currentStudentsThesis;
	private Assignment currentAssignment;
	
	public ThesisBean() {
		
	}
	
	public void actionGetInformationForStudent(ActionEvent event) {
		currentStudentsThesis = studentService.getCurrentStudent().getAssignedThesis();
		if (currentStudentsThesis.getAssignedAssignmentId() == 0) {
			currentAssignment = studentService.getCurrentStudent().getCustomAssignment();
		} else {
			currentAssignment = studentService.getAssignmentFromId(currentStudentsThesis.getAssignedAssignmentId());
		}
	}
	
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
	
	public Thesis getCurrentStudentsThesis() {
		return currentStudentsThesis;
	}

	public void setCurrentStudentsThesis(Thesis currentStudentsThesis) {
		this.currentStudentsThesis = currentStudentsThesis;
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

	public void dispose() throws Exception {}

}
