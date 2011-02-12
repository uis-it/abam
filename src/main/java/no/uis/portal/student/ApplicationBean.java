package no.uis.portal.student;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import no.uis.portal.student.domain.Application;
import no.uis.portal.student.domain.Assignment;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.context.DisposableBean;

public class ApplicationBean implements DisposableBean {
	
	private StudentService studentService;
	
	private Application currentApplication;
	private Assignment currentAssignment;
	
	public ApplicationBean() {
		
	}
	
	public void actionCreateNewApplication(ActionEvent event) {
		UIComponent uic = event.getComponent();
		Assignment selectedAssignment = null;
		System.out.println("Parent = " + uic.getParent().getParent());
		if( (uic.getParent().getParent()) instanceof HtmlDataTable){
			HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
			selectedAssignment = (Assignment)table.getRowData();
		} else {
			selectedAssignment = studentService.getCurrentStudent().getCustomAssignment();
		}
		Application newApplication = new Application();
		newApplication.setAssignment(selectedAssignment);
		setCurrentAssignment(selectedAssignment);
		setCurrentApplication(newApplication);
	}
	
	public void actionSaveApplication(ActionEvent event) {
		studentService.getApplicationList().add(currentApplication);
		studentService.setApplicationToStudent(currentApplication);
		studentService.setApplicationToAssignment(currentApplication);
		System.out.println("CurrentAppl.: "+currentApplication);
		System.out.println("Appl. ass. title: " + currentApplication.getAssignment().getTitle());
	}
	
	public void dispose() throws Exception {
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	public Application getCurrentApplication() {
		return currentApplication;
	}

	public void setCurrentApplication(Application currentApplication) {
		this.currentApplication = currentApplication;
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

}
