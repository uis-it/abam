package no.uis.portal.student;

import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.UIColumn;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Student;

public class ApplicationBean {
	
  private Calendar deadlineMaster = initDeadline(Calendar.DECEMBER, 1);
	private Calendar deadlineBachelor = initDeadline(Calendar.NOVEMBER, 15);

	private StudentService studentService;
	
	private Application currentApplication;
	private Assignment currentAssignment;
	
	
	public ApplicationBean() {}
		
	/**
	 * ActionListener that sets custom Assignment to existing or new Application
	 * @param event
	 */
	public void actionSetCustomAssignmentToApplication(ActionEvent event) {
		Student student = studentService.getCurrentStudent();
    Assignment selectedAssignment = student.getCustomAssignment();
		currentAssignment = selectedAssignment;
		if(assignmentIsAppliedFor(student, selectedAssignment)){
			setCurrentApplication(getApplicationFromAssignment(student, selectedAssignment));
		} else {
			createNewApplication(selectedAssignment);
		}
	}
	
	/**
	 * ActionListener that sets a selected Assignment to existing or new Application
	 * @param event
	 */
	public void actionSetSelectedAssignmentToApplication(ActionEvent event) {

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
		Student student = studentService.getCurrentStudent();
		if(assignmentIsAppliedFor(student, currentAssignment)) {
      setCurrentApplication(getApplicationFromAssignment(student, currentAssignment));
		} else {
			createNewApplication(currentAssignment);
		}
		
	}
	
	private void createNewApplication(Assignment selectedAssignment) {

		Application newApplication = new Application();
		newApplication.setAssignment(selectedAssignment);
		newApplication.setApplicantStudentNumber(studentService.getCurrentStudent().getStudentNumber());
		setCurrentAssignment(selectedAssignment);
		setCurrentApplication(newApplication);
	}
	
	private static boolean assignmentIsAppliedFor(Student student, Assignment assignment) {
    for (Application appl : student.getApplications()) {
      if (appl.getAssignment().equals(assignment)) {
        return true;
      }
    }
    return false;
	}
	
	
	/**
	 * ActionListener that edits an Application
	 * @param event
	 */
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
		Student student = studentService.getCurrentStudent();
		if(assignmentIsAppliedFor(student, currentAssignment)){
      setCurrentApplication(getApplicationFromAssignment(student, currentAssignment));
		} else {
			createNewApplication(currentAssignment);
		}
	}
	
	
	/**
	 * ActionListener that saves the current Application and sets it to the Student
	 * 
	 * @param event
	 */
	public void actionSaveApplication(ActionEvent event) {
		studentService.setApplicationToStudent(currentApplication);
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
	
  public String getCurrentDepartmentName() {
    // TODO show name instead
    return currentAssignment.getDepartmentCode();
  }
  
  public String getCurrentStudyProgram() {
    // TODO show name instead
    return currentAssignment.getStudyProgramCode();
  }
  
	
	public boolean isDeadlineForApplyingReached() {
	  Calendar now = Calendar.getInstance();
	  Calendar deadline = null;
	  switch(studentService.getCurrentStudent().getType()) {
	    case BACHELOR:
	      deadline = deadlineBachelor;
	      break;
	    case MASTER:
	      deadline = deadlineMaster;
	      break;
	  }
	  return now.after(deadline);
	}

	public boolean getAppliedForThreeAssignments() {
	  return studentService.getCurrentStudent().getApplications().size() > 2;
	}
	
  private static Application getApplicationFromAssignment(Student student, Assignment selectedAssignment) {
    
    for (Application appl : student.getApplications()) {
      if (appl.getAssignment().equals(selectedAssignment)) {
        return appl;
      }
    }
    return null;
  }

  private Calendar initDeadline(int month, int day) {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    cal.set(year, month, day, 23, 59, 59);
    return cal;
  }
}
