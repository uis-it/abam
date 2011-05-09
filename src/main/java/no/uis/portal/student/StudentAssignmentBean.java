package no.uis.portal.student;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Supervisor;
import no.uis.abam.util.NumberValidator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class StudentAssignmentBean implements DisposableBean {

	private FacesContext context;
	private StudentService studentService;
	
	private Logger log = Logger.getLogger(StudentAssignmentBean.class); 
	
	private Assignment currentAssignment;
	
	private String customAssignmentStudentNumber;
	
	private boolean renderGetCustomAssignment;
	private boolean getCustomAssignmentFailed;
	private boolean displayCustomAssignmentFailedError;
	
	public StudentAssignmentBean(){
	}
			
	/**
	 * ActionListener that prepares create custom assignment.
	 * @param event
	 */
	public void actionPrepareCreateCustomAssignment(ActionEvent event) {
		setGetCustomAssignmentFailed(true);
		setDisplayCustomAssignmentFailedError(false);
		setCustomAssignmentStudentNumber("");
		Assignment assignment = studentService.getCurrentStudent().getCustomAssignment();
		Student student = studentService.getCurrentStudent(); 
		student.setDepartmentName(studentService
				.getDepartmentNameFromIndex(studentService
						.findDepartmentOe2ForCurrentStudent()));
		if(assignment == null) {
			assignment = new Assignment();
			assignment.setFacultySupervisor(new Employee());
			studentService.getCurrentStudent().setCustomAssignment(assignment);
			studentService.updateStudentInWebServiceFromCurrentStudent();
			
		}
		studentService.setSelectedAssignment(assignment);
		setCurrentAssignment(assignment);
	}
	
	/**
	 * ActionListener that prepares to display the clicked assignment
	 * @param event
	 */
	public void actionSetSelectedAssignment(ActionEvent event){
		Assignment selectedAssignment = (Assignment) getRowFromActionEvent(event);
		
		setCurrentAssignment(selectedAssignment);
		
		studentService.updateSelectedAssignmentInformation(selectedAssignment);
	}
	
	private Object getRowFromActionEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		return table.getRowData();
	}
	
	/**
	 * ActionListener that adds a new Supervisor object to currentAssignment
	 * @param event
	 */
	public void actionAddSupervisor(ActionEvent event) {
		currentAssignment.getSupervisorList().add(new Supervisor());
	}
	
	/**
	 * ActionListener that removes a Supervisor object from currentAssignment
	 * @param event
	 */	
	public void actionRemoveSupervisor(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable) uic.getParent().getParent().getParent().getParent().getParent();
		currentAssignment.getSupervisorList().remove(table.getRowData());		
	}
	
	/**
	 * ActionListener that sets all fields on currentAssignment
	 * @param event
	 */
	public void actionUpdateCurrentAssignment(ActionEvent event) {
		String clientId = event.getComponent().getClientId(context);
		clientId = clientId.replaceAll("CreateButton", "");
		
		Map<?,?> parameterMap = context.getExternalContext().getRequestParameterMap();
		
		log.setLevel(Level.DEBUG);
		currentAssignment.setDepartmentCode(studentService.findDepartmentCodeForCurrentStudent());
		currentAssignment.setDepartmentName(studentService
				.getDepartmentNameFromIndex(studentService
						.findDepartmentOe2ForCurrentStudent()));
		currentAssignment.setStudyProgramName(studentService.getCurrentStudent().getStudyProgramName());
		currentAssignment.setFileUploadErrorMessage("");
		
		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
		currentAssignment.setAddedDate(calendar);
		
		calendar = (GregorianCalendar) GregorianCalendar.getInstance();
		calendar.add(Calendar.MONTH, Assignment.ACTIVE_MONTHS);
		currentAssignment.setExpireDate(calendar);
		
		currentAssignment.updateType(studentService.getCurrentStudent().getType());
		
		currentAssignment.setFacultySupervisor(
				studentService.getEmployeeFromFullName(
						currentAssignment.getFacultySupervisor().getName()));
		
		for (Supervisor supervisor : currentAssignment.getSupervisorList()) {
			if(!supervisor.isExternal()) {
				Employee employee = studentService.getEmployeeFromFullName(supervisor.getName());
				supervisor.setName(employee.getName());
			}
		}
		
		//Using negative studentNumber as id on custom assignment.
		int stdNr = Integer.parseInt(studentService.getCurrentStudent().getStudentNumber());
		currentAssignment.setId(-stdNr);		
		
		String numberOfStudentsInput = (String)parameterMap.get(clientId+"numberOfStudents");
		if (numberOfStudentsInput == null) numberOfStudentsInput = "1";
	}
	
	
	/**
	 * ActionListener that gets a custom assignment from a student number
	 * @param event
	 */
	public void actionGetCustomAssignmentFromStudentNumber(ActionEvent event) {
		Assignment assignment = studentService.getCustomAssignmentFromStudentNumber(getCustomAssignmentStudentNumber());
		if (assignment != null && !assignment.getTitle().isEmpty()) {
			setCurrentAssignment(assignment);
			studentService.setSelectedAssignment(assignment);
			studentService.getCurrentStudent().setCustomAssignment(assignment);
			studentService.updateStudentInWebServiceFromCurrentStudent();
			setGetCustomAssignmentFailed(false);
			setDisplayCustomAssignmentFailedError(false);
		} else {
			setDisplayCustomAssignmentFailedError(true);
			setGetCustomAssignmentFailed(true);
		}
	}
	
	/**
	 * ValueChangeListener to set if get custom assignment should be rendered
	 * @param event
	 */
	public void actionCustomAssignmentRadioListener(ValueChangeEvent event) {
		setRenderGetCustomAssignment((Boolean) event.getNewValue());
	}
	
	/**
	 * ActionListener that handles file uploading
	 * @param event
	 */
	public void fileUploadListen(ActionEvent event){
		InputFile inputFile =(InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        //file has been saved
        if (fileInfo.isSaved()) {
        	currentAssignment.setFileUploadErrorMessage("");
        	currentAssignment.getAttachedFileList().add(fileInfo.getFileName());
        	currentAssignment.setAttachedFilePath(fileInfo.getPhysicalPath());
        	currentAssignment.getAttachedFilePath().replace(fileInfo.getFileName(), "");
        }
        //upload failed, generate custom messages
        if (fileInfo.isFailed()) {
            if(fileInfo.getStatus() == FileInfo.INVALID){
            	currentAssignment.setFileUploadErrorMessage("The attachment could not be uploaded.");
            }
            if(fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED){
            	currentAssignment.setFileUploadErrorMessage("The attachment exceeded the size limit.");
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_CONTENT_TYPE){
            	currentAssignment.setFileUploadErrorMessage("The attachment could not be uploaded.");
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_NAME_PATTERN){
            	currentAssignment.setFileUploadErrorMessage("The attachment can only be a pdf, zip, gif, png, jpeg, jpg, doc or docx file.");
            }
        }
	}
	
	/**
	 * ActionListener that removes an uploaded attachment
	 * @param event
	 */
	public void actionRemoveAttachment(ActionEvent event){
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable) uic.getParent().getParent().getParent().getParent();
	    currentAssignment.getAttachedFileList().remove(table.getRowData());		
	}
	
	/**
	 * Validator that checks that number of students is valid
	 * @param facesContext
	 * @param validate
	 * @param object
	 */
	public void validateNumberOfStudentsField(FacesContext facesContext, UIComponent validate, Object object) {				
		String text = object.toString();
        if (!NumberValidator.isValid(text)) {
        	((UIInput)validate).setValid(false);
        	FacesMessage msg = new FacesMessage(NumberValidator.getErrorMessage());
        	context.addMessage(validate.getClientId(context), msg);
        }
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
		context = FacesContext.getCurrentInstance();
	}

	public String getCustomAssignmentStudentNumber() {
		return customAssignmentStudentNumber;
	}

	public void setCustomAssignmentStudentNumber(
			String customAssignmentStudentNumber) {
		this.customAssignmentStudentNumber = customAssignmentStudentNumber;
	}

	public boolean isRenderGetCustomAssignment() {
		return renderGetCustomAssignment;
	}

	public void setRenderGetCustomAssignment(boolean renderGetCustomAssignment) {
		this.renderGetCustomAssignment = renderGetCustomAssignment;
	}

	public boolean isGetCustomAssignmentFailed() {
		return getCustomAssignmentFailed;
	}

	public void setGetCustomAssignmentFailed(boolean getCustomAssignmentFailed) {
		this.getCustomAssignmentFailed = getCustomAssignmentFailed;
	}

	public boolean isDisplayCustomAssignmentFailedError() {
		return displayCustomAssignmentFailedError;
	}

	public void setDisplayCustomAssignmentFailedError(
			boolean displayCustomAssignmentFailedError) {
		this.displayCustomAssignmentFailedError = displayCustomAssignmentFailedError;
	}

	public void dispose() throws Exception {
	}

}
