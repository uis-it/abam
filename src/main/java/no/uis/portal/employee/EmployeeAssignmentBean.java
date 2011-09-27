package no.uis.portal.employee;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.*;
import no.uis.abam.util.NumberValidator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class EmployeeAssignmentBean implements DisposableBean {
		
	private FacesContext context;
	private EmployeeService employeeService;
	private Logger log = Logger.getLogger(EmployeeAssignmentBean.class);
	
	private Assignment currentAssignment;
	private Thesis currentThesis;
	
	private boolean backToAssignAssignment;
	private boolean backToDisplayAssignments;
	private boolean backToAssignmentAttachment;
	private boolean backToMyStudentThesis;
	
	private boolean showExpired;
	
	private Locale locale;

	public EmployeeAssignmentBean(){
		  
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
		context = FacesContext.getCurrentInstance();		
	}
	
	/**
	 * ActionListener that prepares the assignment summary from assignAssignment.jspx
	 * @param event
	 */
	public void actionSetSelectedAssignmentFromAssignAssignment(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		ApplicationInformation applicationInformation = (ApplicationInformation)table.getRowData();
		Assignment selectedAssignment = applicationInformation.getApplication().getAssignment();
		setCurrentAssignment(selectedAssignment);
		employeeService.setSelectedStudyProgramListFromDepartmentCode(selectedAssignment.getDepartmentCode());
		//employeeService.setSelectedStudyProgramListFromDepartmentIndex(selectedAssignment.getDepartmentCode());
		
		employeeService.setSelectedDepartmentCode(selectedAssignment.getDepartmentCode());
		employeeService.setSelectedStudyProgramCode(selectedAssignment.getStudyProgramCode());
		actionPrepareBackButtonFromAssignAssignemnt(event);
	}
	
	/**
	 * ActionListener that crates a new Assignment object and fills in id and employee name.
	 * @param event
	 */
	public void actionCreateNewAssignment(ActionEvent event) {	
		employeeService.getDepartmentListFromWebService();
		Assignment ass = new Assignment();
		ass.setId(employeeService.getNextId());
		ass.setFacultySupervisor(getEmployeeFromUisLoginName());
		ass.setDepartmentCode(employeeService.getSelectedDepartmentCode());
		ass.setStudyProgramCode(employeeService.getSelectedStudyProgramCode());

		setCurrentAssignment(ass);
	}
	
	/**
	 * ActionListener that saves currentAssignemnt
	 * @param event
	 */
	public void actionSaveAssignment(ActionEvent event) {
		employeeService.saveAssignment(currentAssignment);
	}
	
	/**
	 * ActionListener that gets the Assignment and sets to currentAssignment
	 * @param event
	 */
	public void actionSetSelectedAssignment(ActionEvent event){		
		Assignment selectedAssignment = (Assignment) getRowFromEvent(event);
		setCurrentAssignment(selectedAssignment);
		//employeeService.setSelectedStudyProgramListFromDepartmentIndex(selectedAssignment.getDepartmentNumber());
		employeeService.setSelectedStudyProgramListFromDepartmentCode(selectedAssignment.getDepartmentCode());
		employeeService.setSelectedDepartmentCode(selectedAssignment.getDepartmentCode());
		employeeService.setSelectedStudyProgramCode(selectedAssignment.getStudyProgramCode());
		//employeeService.setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
	}
	
	private Object getRowFromEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		return table.getRowData();
	}
	
	/**
	 * ActionListener that prepares the assignment summary from displayAssignments.jspx
	 * @param event
	 */
	public void actionSetSelectedAssignmentFromDisplayAssignments(ActionEvent event){
		actionSetSelectedAssignment(event);
		actionPrepareBackButtonFromDisplayAssignments(event);
	}
	
	/**
	 * ActionListener that prepares the assignment summary from myStudentTheses.jspx
	 * @param event
	 */
	public void actionSetSelectedAssignmentFromMyStudentTheses(ActionEvent event){
		ThesisInformation selectedThesis = (ThesisInformation) getRowFromEvent(event);
		setCurrentThesis(selectedThesis.getThesis());
		log.setLevel(Level.ERROR);
		Assignment selectedAssignment = selectedThesis.getThesis().getAssignedAssignment();
		setCurrentAssignment(selectedAssignment);
		employeeService.setSelectedStudyProgramListFromDepartmentCode(selectedAssignment.getDepartmentCode());
		
		employeeService.setSelectedDepartmentCode(selectedAssignment.getDepartmentCode());
		employeeService.setSelectedStudyProgramCode(selectedAssignment.getStudyProgramCode());
		actionPrepareBackButtonFromMyStudentTheses(event);
	}
	
	/**
	 * ActionListener that removes selected assignment
	 * @param event
	 */
	public void actionRemoveAssignment(ActionEvent event) {
		Assignment assignment = (Assignment) getRowFromEvent(event);
		
		employeeService.removeAssignment(assignment);
		
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
		currentAssignment.getSupervisorList().remove(getRowFromEvent(event));		
	}
	
	/**
	 * ActionListener that prepares back button from assignmentAttachment.jspx
	 * @param event
	 */
	public void actionPrepareBackButtonFromAssignmentAttachment(ActionEvent event) {
		setBackToAssignmentAttachment(true);
		setBackToAssignAssignment(false);
		setBackToDisplayAssignments(false);
		setBackToMyStudentThesis(false);
	}
	
	/**
	 * ActionListener that prepares back button from assignAssignment.jspx
	 * @param event
	 */	
	public void actionPrepareBackButtonFromAssignAssignemnt(ActionEvent event) {
		setBackToAssignAssignment(true);
		setBackToAssignmentAttachment(false);
		setBackToDisplayAssignments(false);
		setBackToMyStudentThesis(false);
	}
	
	/**
	 * ActionListener that prepares back button from displayAssignments.jspx
	 * @param event
	 */
	public void actionPrepareBackButtonFromDisplayAssignments(ActionEvent event) {
		setBackToDisplayAssignments(true);
		setBackToAssignmentAttachment(false);
		setBackToAssignAssignment(false);
		setBackToMyStudentThesis(false);
	}
	
	/**
	 * ActionListener that prepares back button from myStudentTheses.jspx
	 * @param event
	 */
	public void actionPrepareBackButtonFromMyStudentTheses(ActionEvent event) {
		setBackToDisplayAssignments(false);
		setBackToAssignmentAttachment(false);
		setBackToAssignAssignment(false);
		setBackToMyStudentThesis(true);
	}
	
	/**
	 * ActionListener that sets all fields on currentAssignment
	 * @param event
	 */
	public void actionUpdateCurrentAssignment(ActionEvent event) {				

	  // TODO what is this?
		//currentAssignment.setDepartmentName(employeeService.getDepartmentNameFromIndex(currentAssignment.getDepartmentNumber()));
		//currentAssignment.setDepartmentCode(employeeService.getDepartmentCodeFromIndex(currentAssignment.getDepartmentNumber()));
	  //employeeService.getStudyProgramNameFromCode(currentAssignment.getStudyProgramCode());
		//currentAssignment.setStudyProgramName(employeeService.getSelectedStudyProgramNameFromIndex(currentAssignment.getStudyProgramCode()));
		currentAssignment.setFileUploadErrorMessage("");
		Calendar calendar = Calendar.getInstance();
		currentAssignment.setAddedDate(calendar);
		calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, Assignment.ACTIVE_MONTHS);
		currentAssignment.setExpireDate(calendar);
		currentAssignment.setType("Bachelor");
		
		int numberOfStudentsInput = currentAssignment.getNumberOfStudents(); 
		if (numberOfStudentsInput <= 0) {
		  numberOfStudentsInput = 1;
		}
		if(!currentAssignment.isBachelor() && numberOfStudentsInput > 1) {
			currentAssignment.setNumberOfStudents(1);
			currentAssignment.setType("Master");
			// TODO use FacesMessage
			currentAssignment.setNumberOfStudentsError("Maximum number of students on a master assignment is 1.");				
		}
		
		for (Supervisor supervisor : currentAssignment.getSupervisorList()) {
			if(!supervisor.isExternal()) {
				Employee employee = employeeService.getEmployeeFromName(supervisor.getName());
				supervisor.setName(employee.getName());
			}
		}
		currentAssignment.setFacultySupervisor(employeeService.getEmployeeFromName(currentAssignment.getFacultySupervisor().getName()));
		
		currentAssignment.setAuthor(getEmployeeFromUisLoginName());		
	}
	
	private Employee getEmployeeFromUisLoginName() {				 
		return employeeService.getEmployeeFromUisLoginName();

	}
	
	/**
	 * ValueChangeListener updates list of Assignments on user action
	 * @param event
	 */
	public void actionShowExpired(ValueChangeEvent event) {
		if(event.getNewValue().equals(true)) {
			employeeService.getAllAssignmentsSet();			
		} else {
			employeeService.getActiveAssignmentsSet();
		}
		employeeService.setDisplayAssignments();
	}
	
	
	/**
	 * ActionListener that handles file uploading
	 * @param event
	 */
	public void actionFileUpload(ActionEvent event){
		InputFile inputFile =(InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();

        //file has been saved
        locale = context.getViewRoot().getLocale();
        ResourceBundle res = ResourceBundle.getBundle("Language", locale);
        
        if (fileInfo.isSaved()) {
        	currentAssignment.setFileUploadErrorMessage("");
        	currentAssignment.getAttachedFileList().add(fileInfo.getFileName());
        	currentAssignment.setAttachedFilePath(fileInfo.getPhysicalPath());
        	currentAssignment.getAttachedFilePath().replace(fileInfo.getFileName(), "");
        }
        //upload failed, generate custom messages
        if (fileInfo.isFailed()) {
            if(fileInfo.getStatus() == FileInfo.INVALID){
            	currentAssignment.setFileUploadErrorMessage(res.getObject("msg_could_not_upload").toString());
            }
            if(fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED){
            	currentAssignment.setFileUploadErrorMessage(res.getObject("msg_exceeded_size_limit").toString());            	
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_CONTENT_TYPE){
            	currentAssignment.setFileUploadErrorMessage(res.getObject("msg_could_not_upload").toString());
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_NAME_PATTERN){
            	currentAssignment.setFileUploadErrorMessage(res.getObject("msg_attachment_type_restrictions").toString());
            }
        }        
	}
	
	/**
	 * ValueChangeListener to set assignment type
	 * @param event
	 */
	public void actionChangeAssignmentTypeRadioListener(ValueChangeEvent event){
		if (event.getNewValue().equals(false)){
			currentAssignment.setMaster(true);
			currentAssignment.setBachelor(false);
			currentAssignment.setType("Master");
		} else {
			currentAssignment.setMaster(false);
			currentAssignment.setBachelor(true);
			currentAssignment.setType("Bachelor");
			currentAssignment.setNumberOfStudentsError("");
		}
	}
	
	/**
	 * Validator that checks that number of students is valid
	 * @param facesContext
	 * @param validate
	 * @param object
	 */
	@Deprecated
	public void validateNumberOfStudentsField(FacesContext facesContext, UIComponent validate, Object object) {				
		String text = object.toString();
        if (!NumberValidator.isValid(text)) {
        	((UIInput)validate).setValid(false);
        	FacesMessage msg = new FacesMessage(NumberValidator.getErrorMessage());
        	context.addMessage(validate.getClientId(context), msg);
        }
	}
	
	/**
	 * ActionListener that removes an uploaded attachment
	 * @param event
	 */
	public void actionRemoveAttachment(ActionEvent event){
		currentAssignment.getAttachedFileList().remove(getRowFromEvent(event));		
	}

	public boolean isBackToAssignAssignment() {
		return backToAssignAssignment;
	}

	public void setBackToAssignAssignment(boolean backToAssignAssignment) {
		this.backToAssignAssignment = backToAssignAssignment;
	}

	public boolean isBackToAssignmentAttachment() {
		return backToAssignmentAttachment;
	}

	public void setBackToAssignmentAttachment(boolean backToAssignmentAttachment) {
		this.backToAssignmentAttachment = backToAssignmentAttachment;
	}

	public boolean isBackToDisplayAssignments() {
		return backToDisplayAssignments;
	}

	public void setBackToDisplayAssignments(boolean backToDisplayAssignments) {
		this.backToDisplayAssignments = backToDisplayAssignments;
	}

	public boolean isShowExpired() {
		return showExpired;
	}

	public void setShowExpired(boolean showExpired) {
		this.showExpired = showExpired;
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public String getCurrentAssignmentDepartmentCode() {
	  return employeeService.getDepartmentNameFromCode(currentAssignment.getDepartmentCode());  
	}
	
	public String getCurrentAssignmentStudyProgramCode() {
	  return employeeService.getStudyProgramNameFromCode(currentAssignment.getStudyProgramCode());
	}
	
	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

	public boolean isBackToMyStudentThesis() {
		return backToMyStudentThesis;
	}

	public void setBackToMyStudentThesis(boolean backToMyStudentThesis) {
		this.backToMyStudentThesis = backToMyStudentThesis;
	}

	public Thesis getCurrentThesis() {
		return currentThesis;
	}

	public void setCurrentThesis(Thesis currentThesis) {
		this.currentThesis = currentThesis;
	}

	public void dispose() throws Exception {
	}	
}
