package no.uis.portal.employee;

import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.ApplicationInformation;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Supervisor;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisInformation;

import org.apache.log4j.Logger;
import org.apache.myfaces.shared_impl.util.MessageUtils;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class EmployeeAssignmentBean implements DisposableBean {
		
	private EmployeeService employeeService;
	private Logger log = Logger.getLogger(EmployeeAssignmentBean.class);
	
	private Assignment currentAssignment;
	private Thesis currentThesis;
	
	private boolean backToAssignAssignment;
	private boolean backToDisplayAssignments;
	private boolean backToAssignmentAttachment;
	private boolean backToMyStudentThesis;
	
	private boolean showExpired;
	
	//private Locale locale;

	public EmployeeAssignmentBean(){
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
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
		Calendar calendar = Calendar.getInstance();
		currentAssignment.setAddedDate(calendar);
		calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, Assignment.ACTIVE_MONTHS);
		currentAssignment.setExpireDate(calendar);
		currentAssignment.setType(AssignmentType.BACHELOR);
		
		int numberOfStudentsInput = currentAssignment.getNumberOfStudents(); 
		if (numberOfStudentsInput <= 0) {
		  numberOfStudentsInput = 1;
		}
		if(!currentAssignment.getType().equals(AssignmentType.BACHELOR) && numberOfStudentsInput > 1) {
			currentAssignment.setNumberOfStudents(1);
			currentAssignment.setType(AssignmentType.MASTER);

			// add an error message  to the context
			FacesContext fc = FacesContext.getCurrentInstance();
			UIComponent component = (UIComponent)event.getSource();
			String forClientId = component.getClientId(fc);
			MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, "max_master_assignment", new Object[] {1}, forClientId);
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

    if (fileInfo.isSaved()) {
    	currentAssignment.getAttachedFileList().add(fileInfo.getFileName());
    	currentAssignment.setAttachedFilePath(fileInfo.getPhysicalPath());
    	currentAssignment.getAttachedFilePath().replace(fileInfo.getFileName(), "");
    } else if (fileInfo.isFailed()) {
      //upload failed, generate custom messages
      switch (fileInfo.getStatus()) {
        case FileInfo.INVALID:
          MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, "msg_could_not_upload", null);
          break;
        case FileInfo.SIZE_LIMIT_EXCEEDED:
          MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, "msg_exceeded_size_limit", null);
          break;
        case FileInfo.INVALID_CONTENT_TYPE:
          MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, "msg_could_not_upload", null);
          break;
        case FileInfo.INVALID_NAME_PATTERN:
          MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, "msg_attachment_type_restrictions", null);
          break;
      }
    }        
	}
	
	/**
	 * ValueChangeListener to set assignment type
	 * @param event
	 */
	public void actionChangeAssignmentTypeRadioListener(ValueChangeEvent event){
		String newValue = event.getNewValue().toString();
		AssignmentType type = AssignmentType.valueOf(newValue);
    if (type.equals(AssignmentType.MASTER)){
			currentAssignment.setType(AssignmentType.MASTER);
		} else {
			currentAssignment.setType(AssignmentType.BACHELOR);
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
