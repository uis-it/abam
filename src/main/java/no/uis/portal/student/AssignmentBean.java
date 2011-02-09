package no.uis.portal.student;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.portal.student.domain.Assignment;
import no.uis.portal.student.domain.ExternalExaminer;
import no.uis.portal.student.domain.Supervisor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class AssignmentBean implements DisposableBean {

	private FacesContext context;
	private StudentService studentService;
	private Logger log = Logger.getLogger(AssignmentBean.class); 
	
	private Assignment currentAssignment;
	
	public AssignmentBean(){
	}
	
	public void actionEditExternalExaminer(ActionEvent event) {
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		Assignment selectedAssignment = (Assignment)table.getRowData();
		if(selectedAssignment != currentAssignment) currentAssignment.setEditExternalExaminer(false);
		
		setCurrentAssignment(selectedAssignment);
				
		if(currentAssignment.getExternalExaminer() == null) {
			currentAssignment.setExternalExaminer(new ExternalExaminer());
		}
		
		if(currentAssignment.isEditExternalExaminer()) {
				currentAssignment.setEditExternalExaminer(false);
		} else currentAssignment.setEditExternalExaminer(true);
	}
	
	public void actionEditExternalExaminerSetAllFalse(ActionEvent event) {
		studentService.setAllEditExternalExaminerToFalse();
	}
	
	public void actionGetCustomAssignment(ActionEvent event) {
		Assignment assignment = studentService.getCurrentStudent().getCustomAssignment();
		if(assignment == null) assignment = new Assignment();
		setCurrentAssignment(assignment);
	}
	
	public void actionCreateNewAssignment(ActionEvent event) {		
		setCurrentAssignment(new Assignment());
		currentAssignment.setId(studentService.getNextId());
	}
	
	public void actionSaveAssignment(ActionEvent event) {
		studentService.saveAssignment(currentAssignment);
	}
	
	public void actionSetSelectedAssignment(ActionEvent event){
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		Assignment selectedAssignment = (Assignment)table.getRowData();
		setCurrentAssignment(selectedAssignment);
		studentService.setStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		studentService.setSelectedDepartmentNumber(selectedAssignment.getDepartmentNumber());
		studentService.setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
	}
	
	public void actionRemoveAssignment(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		Assignment assignment = (Assignment)table.getRowData();
		
		studentService.removeAssignment(assignment);
	}
	
	public void actionAddSupervisor(ActionEvent event) {
		currentAssignment.getSupervisorList().add(new Supervisor());
	}
	
	public void actionRemoveSupervisor(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		currentAssignment.getSupervisorList().remove(table.getRowData());		
	}
	
	public void actionUpdateCurrentAssignment(ActionEvent event) {
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
		currentAssignment.setDepartment(studentService.getCurrentStudent().getDepartment());
		currentAssignment.setStudyProgram(studentService.getCurrentStudent().getStudyProgram());
		currentAssignment.setFileUploadErrorMessage("");
		GregorianCalendar calendar = new GregorianCalendar();
		currentAssignment.setAddedDate(calendar);
		calendar.add(Calendar.MONTH, Assignment.ACTIVE_MONTHS);
		currentAssignment.setExpireDate(calendar);
		currentAssignment.setType(studentService.getCurrentStudent().getType());
		
		String numberOfStudentsInput = (String)parameterMap.get(clientId+"numberOfStudents");
		if (numberOfStudentsInput == null) numberOfStudentsInput = "1";
	}
	
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
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
	    currentAssignment.getAttachedFileList().remove(table.getRowData());		
	}

	public void dispose() throws Exception {
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
}
