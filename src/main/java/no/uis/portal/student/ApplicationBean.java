package no.uis.portal.student;

import javax.faces.event.ActionEvent;

import no.uis.portal.student.domain.Application;

import com.icesoft.faces.context.DisposableBean;

public class ApplicationBean implements DisposableBean {
	
	private StudentService studentService;
	
	private Application currentApplication;
	
	public ApplicationBean() {
		
	}
	
	public void actionCreateNewApplication(ActionEvent event) {
		setCurrentApplication(new Application());
	}
	
	public void actionSaveApplication(ActionEvent event) {
		studentService.getApplicationList().add(currentApplication);
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

}
