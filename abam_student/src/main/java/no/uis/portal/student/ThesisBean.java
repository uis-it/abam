package no.uis.portal.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.Resource;

import no.uis.abam.commons.AttachmentResource;
import no.uis.abam.commons.ThesisInformation;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Attachment;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisStatus;
import no.uis.abam.dom.ThesisStatusType;

public class ThesisBean {

  private StudentService studentService;
	
	private ThesisInformation currentThesisInformation;
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
		
		//studentService.updateCurrentStudentFromWebService();
		Student currentStudent = studentService.getCurrentStudent();
		currentStudentsThesis = studentService.getCurrentStudent().getAssignedThesis();
		
		
		if (currentStudentsThesis != null) {
			if (currentStudentsThesis.getAssignment().isCustom()) {
				currentAssignment = studentService.getCurrentStudent().getCustomAssignment();
			} else {
				currentAssignment = currentStudentsThesis.getAssignment();
			}
			currentAssignment.setDepartmentCode(currentStudent.getDepartmentCode());
			// TODO the name can be fetched when needed
//			currentAssignment.setDepartmentName(
//					studentService.getDepartmentNameFromIndex(
//							studentService.findDepartmentOe2ForCurrentStudent()));
			if (currentStudent.isAcceptedThesis()) {
				readRules1 = true;
				readRules2 = true;
				readRules3 = true;
			}
			currentThesisInformation = createThesisInformation();
			renderNotAssigned = false;
		} else {
			renderNotAssigned = true;
		}		
	}
	
	private ThesisInformation createThesisInformation() {
		ThesisInformation ti = new ThesisInformation();
		ti.setAssignmentTitle(currentAssignment.getTitle());
		
		Student student = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber1());
		if (student != null) 
			ti.setStudentName(student.getName());
		
		student = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber2());
		if (student != null)
			ti.setCoStudent1Name(student.getName());
		
		student = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber3());
		if (student != null)
			ti.setCoStudent2Name(student.getName());
		
		ti.setThesis(currentStudentsThesis);
		return ti;
	}

	private void setRenderAcceptButtonFalse() {
		renderAcceptButton = false;
	}
	
	public void actionSaveThesis(ActionEvent event) {
		currentStudentsThesis.addThesisStatus(new ThesisStatus(ThesisStatusType.SUBMITTED, studentService.getCurrentStudent().getName()));
		currentStudentsThesis.setSubmitted(true);
		currentStudentsThesis.setSubmissionDate(Calendar.getInstance());
		studentService.updateThesis(currentStudentsThesis);
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
		if(readRules1 && readRules2 && readRules3) {
		  renderAcceptButton = true;
		} else {
		  renderAcceptButton = false;
		}
	}
	
	public void actionSetThesisIsAccepted(ActionEvent event) {
		studentService.getCurrentStudent().setSubmissionDate(Calendar.getInstance());
		studentService.getCurrentStudent().setAcceptedThesis(true);
		studentService.updateStudentInWebServiceFromCurrentStudent();
		ThesisStatusType statusType;
		if (allStudentsHaveAccepted()) {
			statusType = ThesisStatusType.ACCEPTED;
		} else {
		  statusType = ThesisStatusType.PARTIALLY_ACCEPTED;
		}		
		currentStudentsThesis.addThesisStatus(new ThesisStatus(statusType, studentService.getCurrentStudent().getName()));
		updateThesisForInvolvedStudents();
		studentService.updateThesis(currentStudentsThesis);
		actionInitMyThesisPage(event);
	}
	
	private void updateThesisForInvolvedStudents() {		
		studentService.getCurrentStudent().setAssignedThesis(currentStudentsThesis);
		Student std = null;
		if (currentStudentsThesis.getStudentNumber1() != null && !currentStudentsThesis.getStudentNumber1().isEmpty()) {
			std = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber1());
			std.setAssignedThesis(currentStudentsThesis);
			studentService.updateStudent(std);
		}
		if (currentStudentsThesis.getStudentNumber2() != null && !currentStudentsThesis.getStudentNumber2().isEmpty()) {
			std = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber2());
			std.setAssignedThesis(currentStudentsThesis);
			studentService.updateStudent(std);
		}
		if (currentStudentsThesis.getStudentNumber3() != null && !currentStudentsThesis.getStudentNumber3().isEmpty()) {
			std = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber3());
			std.setAssignedThesis(currentStudentsThesis);
			studentService.updateStudent(std);
		}
	}
	
	private boolean allStudentsHaveAccepted() {
		boolean allHaveAccepted = false;
		
		if(StringUtils.isBlank(currentStudentsThesis.getStudentNumber2())) {
			allHaveAccepted = true;
		} else if (StringUtils.isBlank(currentStudentsThesis.getStudentNumber3())) {
			Student std1 = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber1());
			Student std2 = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber2());
			if (std1.isAcceptedThesis() && std2.isAcceptedThesis()) {
				allHaveAccepted = true;
			}
		} else {
			Student std1 = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber1());
			Student std2 = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber2());
			Student std3 = studentService.getStudentFromStudentNumber(currentStudentsThesis.getStudentNumber3());
			if (std1.isAcceptedThesis() && std2.isAcceptedThesis() && std3.isAcceptedThesis()) {
				allHaveAccepted = true;
			}
		}
		return allHaveAccepted;
	}
	
	public void actionInitMyThesisPage(ActionEvent event) {
		actionGetInformationForStudent(null);
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
      	currentStudentsThesis.setFileUploadErrorMessage("The attachment can only be a pdf, zip, doc or docx file.");
      }
    }
	}
	
	public void actionRemoveAttachment(ActionEvent event){
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent().getParent().getParent();
		
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

  public List<Resource> getAssignmentAttachmentResources() {
    List<Attachment> attachments = currentAssignment.getAttachments();
    List<Resource> resources = new ArrayList<Resource>(attachments.size());
    for (Attachment attachment : attachments) {
      Resource res = new AttachmentResource(attachment.getData(), attachment.getFileName(), attachment.getContentType());
      resources.add(res);
    }
    return resources;
  }
  
  public List<Resource> getThesisAttachmentResources() {
    List<Attachment> attachments = this.currentStudentsThesis.getAttachments();
    List<Resource> resources = new ArrayList<Resource>(attachments.size());
    for (Attachment attachment : attachments) {
      Resource res = new AttachmentResource(attachment.getData(), attachment.getFileName(), attachment.getContentType());
      resources.add(res);
    }
    return resources;
  }
  
	
	public String getCurrentDepartmentName() {
	  // TODO show name instead
	  return currentAssignment.getDepartmentCode();
	}
	
	public String getCurrentStudyProgram() {
    // TODO show name instead
	  return currentAssignment.getStudyProgramCode();
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

	public ThesisInformation getCurrentThesisInformation() {
		return currentThesisInformation;
	}

	public void setCurrentThesisInformation(
			ThesisInformation currentThesisInformation) {
		this.currentThesisInformation = currentThesisInformation;
	}
	
	public boolean isDeadlineForSubmissionOfTopicReached() {
		if(currentStudentsThesis != null) {
			return currentStudentsThesis.getAcceptionDeadline().before(Calendar.getInstance());
		}
		return false;
	}
	
	public boolean isDeadlineForSubmissionForEvalutationReached() {
		if(currentStudentsThesis != null) {
			return currentStudentsThesis.getSubmissionDeadline().before(Calendar.getInstance());
		}
		return false;
	}
}
