package no.uis.portal.employee;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

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
		employeeService.setSelectedStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		employeeService.setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		employeeService.setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
		actionPrepareBackButtonFromAssignAssignemnt(event);
	}
	
	/**
	 * ActionListener that crates a new Assignment object and fills in id and employee name.
	 * @param event
	 */
	public void actionCreateNewAssignment(ActionEvent event) {	
		employeeService.getDepartmentListFromWebService();
		setCurrentAssignment(new Assignment());
		currentAssignment.setId(employeeService.getNextId());
		currentAssignment.setFacultySupervisor(getEmployeeFromUisLoginName());
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
		employeeService.setSelectedStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		employeeService.setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		employeeService.setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
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
		employeeService.setSelectedStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		employeeService.setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		employeeService.setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
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
		debugToLog(Level.ERROR, event);
		
		currentAssignment.setDepartmentName(employeeService.getDepartmentNameFromIndex(currentAssignment.getDepartmentNumber()));
		currentAssignment.setDepartmentCode(employeeService.getDepartmentCodeFromIndex(currentAssignment.getDepartmentNumber()));
		currentAssignment.setStudyProgramName(employeeService.getSelectedStudyProgramNameFromIndex(currentAssignment.getStudyProgramNumber()));
		currentAssignment.setFileUploadErrorMessage("");
		GregorianCalendar calendar = new GregorianCalendar();
		currentAssignment.setAddedDate(calendar);
		calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, Assignment.ACTIVE_MONTHS);
		currentAssignment.setExpireDate(calendar);
		currentAssignment.setType("Bachelor");
		
		String numberOfStudentsInput = currentAssignment.getNumberOfStudents(); 
		if (numberOfStudentsInput == null) numberOfStudentsInput = "1";
		String typeAssignment = currentAssignment.isBachelor() ? "true" : "false";
		if(typeAssignment != null && typeAssignment.equals("false")){
			if(!numberOfStudentsInput.equals("1")){
				currentAssignment.setNumberOfStudents("1");
				currentAssignment.setType("Master");
				if(!numberOfStudentsInput.equals(""))
					currentAssignment.setNumberOfStudentsError("Maximum number of students on a master assignment is 1.");				
			}
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
	
	private void debugToLog(Level level, ActionEvent event) {
		String clientId = event.getComponent().getClientId(context);
		clientId = clientId.replaceAll("CreateButton", "");
		
		Map<?,?> parameterMap = context.getExternalContext().getRequestParameterMap();
		
		log.setLevel(level);
		if (log.isDebugEnabled()) {
			log.debug("Title: "+parameterMap.get(clientId+"title"));
			log.debug("Des: "+parameterMap.get(clientId+"description"));
			log.debug("Supervisor: "+parameterMap.get(clientId+"supervisor"));
			log.debug("FacultySupervisor: "+parameterMap.get(clientId+"facultySupervisor"));
			log.debug("Department: "+parameterMap.get(clientId+"department"));
			log.debug("StudyProgram: "+parameterMap.get(clientId+"studyProgram"));
			log.debug("NumberOfStudents: "+parameterMap.get(clientId+"numberOfStudents"));
			log.debug("type: "+parameterMap.get(clientId+"type"));
		}
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
