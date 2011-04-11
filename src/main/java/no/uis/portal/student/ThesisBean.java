package no.uis.portal.student;

import java.util.GregorianCalendar;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class ThesisBean implements DisposableBean {

	private StudentService studentService;
	
	private Thesis currentStudentsThesis;
	private Assignment currentAssignment;
	
	private boolean renderAcceptButton = false;
	private boolean renderNotAssigned;
	private boolean renderMyThesis;
	private boolean readRules1;
	private boolean readRules2;
	private boolean readRules3;
	
	
	public ThesisBean() {
	}
	
	public void actionGetInformationForStudent(ActionEvent event) {
		setRenderAcceptButtonFalse();
		
		studentService.updateCurrentStudentFromWebService();
		Student currentStudent = studentService.getCurrentStudent();
		currentStudentsThesis = studentService.getCurrentStudent().getAssignedThesis();
		if (currentStudentsThesis != null) {
			if (currentStudentsThesis.getAssignedAssignment().getId() == 0) {
				currentAssignment = studentService.getCurrentStudent()
						.getCustomAssignment();
			} else {
				currentAssignment = currentStudentsThesis.getAssignedAssignment();
			}
			currentAssignment.setDepartmentName(
					studentService.getDepartmentNameFromIndex(
							studentService.findDepartmentOe2ForCurrentStudent()));
			if (currentStudent.isAcceptedThesis()) {
				readRules1 = true;
				readRules2 = true;
				readRules3 = true;
			}
			renderNotAssigned = false;
		} else {
			renderNotAssigned = true;
		}
	}
	
	private void setRenderAcceptButtonFalse() {
		renderAcceptButton = false;
	}
	
	public void actionCheckBoxChanges(ValueChangeEvent event) {
		String id = event.getComponent().getId();
		if (id.equals("readRules1")) {
			readRules1 = (Boolean)event.getNewValue();
		} else if (id.equals("readRules2")) {
			readRules2 = (Boolean)event.getNewValue();
		} else if (id.equals("readRules3")) {
			readRules3 = (Boolean)event.getNewValue();
		}
		if(readRules1 && readRules2 && readRules3) renderAcceptButton = true;
		else renderAcceptButton = false;
	}
	
	public void actionSetThesisIsAccepted(ActionEvent event) {
		studentService.getCurrentStudent().setActualSubmissionOfTopic(GregorianCalendar.getInstance().getTime());
		studentService.getCurrentStudent().setAssignedThesis(currentStudentsThesis);
		studentService.getCurrentStudent().setAcceptedThesis(true);
		studentService.updateStudentInWebServiceFromCurrentStudent();
		actionInitMyThesisPage(event);
	}
	
	public void actionInitMyThesisPage(ActionEvent event) {
		if (currentStudentsThesis == null) {
			renderMyThesis = false;
		} else {
			renderMyThesis = true;
		}
	}
	
	public void actionFileUpload(ActionEvent event){
		InputFile inputFile =(InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        //file has been saved
        if (fileInfo.isSaved()) {
        	currentStudentsThesis.setFileUploadErrorMessage("The size limit of an attachment is 40MB");
        	currentStudentsThesis.getAttachedFileList().add(fileInfo.getFileName());
        	currentStudentsThesis.setAttachedFilePath(fileInfo.getPhysicalPath());
        	currentStudentsThesis.getAttachedFilePath().replace(fileInfo.getFileName(), "");
        }
        //upload failed, generate custom messages
        if (fileInfo.isFailed()) {
            if(fileInfo.getStatus() == FileInfo.INVALID){
            	currentStudentsThesis.setFileUploadErrorMessage("The attachment could not be uploaded.");
            }
            if(fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED){
            	currentStudentsThesis.setFileUploadErrorMessage("The attachment exceeded the size limit of 40MB.");
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_CONTENT_TYPE){
            	currentStudentsThesis.setFileUploadErrorMessage("The attachment could not be uploaded.");
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_NAME_PATTERN){
            	currentStudentsThesis.setFileUploadErrorMessage("The attachment can only be a pdf, zip, gif, png, jpeg, jpg, doc or docx file.");
            }
        }
	}
	
	public void actionRemoveAttachment(ActionEvent event){
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		currentStudentsThesis.getAttachedFileList().remove(table.getRowData());		
	}
	
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
	
	public Thesis getCurrentStudentsThesis() {
		return currentStudentsThesis;
	}

	public void setCurrentStudentsThesis(Thesis currentStudentsThesis) {
		this.currentStudentsThesis = currentStudentsThesis;
	}

	public Assignment getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Assignment currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

	public boolean isRenderAcceptButton() {
		return renderAcceptButton;
	}

	public void setRenderAcceptButton(boolean enableAcceptButton) {
		this.renderAcceptButton = enableAcceptButton;
	}

	
	
	public boolean isReadRules1() {
		return readRules1;
	}

	public void setReadRules1(boolean readRules1) {
		this.readRules1 = readRules1;
	}

	public boolean isReadRules2() {
		return readRules2;
	}

	public void setReadRules2(boolean readRules2) {
		this.readRules2 = readRules2;
	}

	public boolean isReadRules3() {
		return readRules3;
	}

	public void setReadRules3(boolean readRules3) {
		this.readRules3 = readRules3;
	}

	public boolean isRenderNotAssigned() {
		return renderNotAssigned;
	}

	public void setRenderNotAssigned(boolean renderNotAssigned) {
		this.renderNotAssigned = renderNotAssigned;
	}

	public boolean isRenderMyThesis() {
		return renderMyThesis;
	}

	public void setRenderMyThesis(boolean renderMyThesis) {
		this.renderMyThesis = renderMyThesis;
	}

	public void dispose() throws Exception {}

}
