package no.uis.portal.student;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import no.uis.portal.student.domain.Application;
import no.uis.portal.student.domain.Assignment;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.UIColumn;
import com.icesoft.faces.context.DisposableBean;

public class ApplicationBean implements DisposableBean {
	
	private StudentService studentService;
	
	private Application currentApplication;
	private Assignment currentAssignment;
	
	public ApplicationBean() {
		
	}
	
	public void actionSetApplicationFromCustomAssignment(ActionEvent event) {
		Assignment selectedAssignment = studentService.getCurrentStudent().getCustomAssignment();
		createNewApplication(selectedAssignment);
	}
	
	public void actionApplyForAssignment(ActionEvent event) {
		UIComponent uic = event.getComponent();

		Object parent = uic.getParent();
		Assignment selectedAssignment = null;
		if(parent instanceof UIColumn){
			UIColumn uiColumn = (UIColumn)parent;
			parent = uiColumn.getParent();
			if(parent instanceof HtmlDataTable){
				HtmlDataTable table = (HtmlDataTable)parent;
				selectedAssignment = (Assignment)table.getRowData();
				studentService.updateSelectedAssignmentInformation(selectedAssignment);
			}
		}
		currentAssignment = studentService.getSelectedAssignment();
		createNewApplication(currentAssignment);
	}
	
	private void createNewApplication(Assignment selectedAssignment) {
		Application newApplication = null;
		if(assignmentIsAppliedFor(selectedAssignment)){
			newApplication = studentService.getCurrentStudent().getApplicationFromAssignment(selectedAssignment);
		} else {
			newApplication = new Application();
			newApplication.setAssignment(selectedAssignment);
		}
		
		setCurrentAssignment(selectedAssignment);
		setCurrentApplication(newApplication);
	}
	
	private boolean assignmentIsAppliedFor(Assignment selectedAssignment){
		return studentService.getCurrentStudent().assignmentIsAlreadyAppliedFor(selectedAssignment);
		
	}
	
	public void actionEditApplication(ActionEvent event){
		UIComponent uic = event.getComponent();

		Object parent = uic.getParent();
		Application selectedApplication = null;
		if(parent instanceof UIColumn){
			UIColumn uiColumn = (UIColumn)parent;
			parent = uiColumn.getParent();
			if(parent instanceof HtmlDataTable){
				HtmlDataTable table = (HtmlDataTable)parent;
				selectedApplication = (Application)table.getRowData();
				studentService.updateSelectedAssignmentInformation(selectedApplication.getAssignment());
			}
		}
		currentAssignment = studentService.getSelectedAssignment();
		createNewApplication(currentAssignment);
	}
	
	public void actionSaveApplication(ActionEvent event) {
		studentService.getApplicationList().add(currentApplication);
		studentService.setApplicationToStudent(currentApplication);
		studentService.setApplicationToAssignment(currentApplication);
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
