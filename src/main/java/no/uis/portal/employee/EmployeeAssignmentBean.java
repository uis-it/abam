package no.uis.portal.employee;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.ApplicationInformation;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.ExternalExaminer;
import no.uis.abam.dom.Supervisor;
import no.uis.abam.dom.Thesis;

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
	
	private boolean backToAssignAssignment;
	private boolean backToDisplayAssignments;
	private boolean backToAssignmentAttachment;
	
	private boolean showExpired;
	
	private Locale locale;

	public EmployeeAssignmentBean(){
		  
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
		context = FacesContext.getCurrentInstance();		
	}
	
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
	
	public void actionCreateNewAssignment(ActionEvent event) {	
		employeeService.getDepartmentListFromWebService();
		setCurrentAssignment(new Assignment());
		currentAssignment.setId(employeeService.getNextId());
	}
	
	public void actionSaveAssignment(ActionEvent event) {
		employeeService.saveAssignment(currentAssignment);
	}
	
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
	
	public void actionSetSelectedAssignmentFromDisplayAssignments(ActionEvent event){
		actionSetSelectedAssignment(event);
		actionPrepareBackButtonFromDisplayAssignments(event);
	}
	
	public void actionRemoveAssignment(ActionEvent event) {
		Assignment assignment = (Assignment) getRowFromEvent(event);
		
		employeeService.removeAssignment(assignment);
	}
	
	public void actionAddSupervisor(ActionEvent event) {
		currentAssignment.getSupervisorList().add(new Supervisor());
	}
	
	public void actionRemoveSupervisor(ActionEvent event) {
		currentAssignment.getSupervisorList().remove(getRowFromEvent(event));		
	}
	
	public void actionPrepareBackButtonFromAssignmentAttachment(ActionEvent event) {
		setBackToAssignmentAttachment(true);
		setBackToAssignAssignment(false);
		setBackToDisplayAssignments(false);
	}
	
	public void actionPrepareBackButtonFromAssignAssignemnt(ActionEvent event) {
		setBackToAssignAssignment(true);
		setBackToAssignmentAttachment(false);
		setBackToDisplayAssignments(false);
	}
	
	public void actionPrepareBackButtonFromDisplayAssignments(ActionEvent event) {
		setBackToDisplayAssignments(true);
		setBackToAssignmentAttachment(false);
		setBackToAssignAssignment(false);
	}
	
	public void actionUpdateCurrentAssignment(ActionEvent event) {		
		debugToLog(Level.ERROR, event);
		
		currentAssignment.setDepartmentName(employeeService.getDepartmentNameFromIndex(currentAssignment.getDepartmentNumber()));
		currentAssignment.setStudyProgramName(employeeService.getSelectedStudyProgramNameFromIndex(currentAssignment.getStudyProgramNumber()));
		currentAssignment.setFileUploadErrorMessage("");
		GregorianCalendar calendar = new GregorianCalendar();
		currentAssignment.setAddedDate(calendar);
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
	}
	
	private void debugToLog(Level level, ActionEvent event) {
		String clientId = event.getComponent().getClientId(context);
		clientId = clientId.replaceAll("CreateButton", "");
		
		Map<?,?> parameterMap = context.getExternalContext().getRequestParameterMap();
		
		log.setLevel(Level.ERROR);
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
	
	public void actionShowExpired(ValueChangeEvent event) {
		if(event.getNewValue().equals(true)) {
			employeeService.getAllAssignmentsSet();			
		} else {
			employeeService.getActiveAssignmentsSet();
		}
		employeeService.setDisplayAssignments();
	}
	
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
	
	public void radioListener(ValueChangeEvent event){
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

	public void dispose() throws Exception {
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}
}
