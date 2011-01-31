package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;
import com.liferay.portal.service.ServiceContext;

public class AssignmentBean implements DisposableBean, Comparable {

	private ArrayList<SelectItem> instituteList = new ArrayList<SelectItem>();
	private ArrayList<SelectItem> studyProgramList = new ArrayList<SelectItem>();
	
	private static int lastId = 0; //todo: sync against database. 
		
	private int id;
	
	private boolean master;
	private boolean bachelor;
	
	private String numberOfStudents;
	private String title;
	private String facultySupervisor;
	private String description;
	private String studyProgram;
	private String institute;
	private String numberOfStudentsError;
	private String fileUploadErrorMessage;
	private String type;
	private String attachedFilePath;
	
	private int instituteNumber;
	private int studyProgramNumber;
	
	private ArrayList<Supervisor> supervisorList;
	private ArrayList<String> attachedFileList;
	
	private PortletRequest pr;
	private PortletSession ps;
	private Logger log = Logger.getLogger(AssignmentBean.class); 
	
	public AssignmentBean(){
		FacesContext context = FacesContext.getCurrentInstance();
		pr = (PortletRequest)context.getExternalContext().getRequest();
		ps = pr.getPortletSession();
		
		Controller controller = (Controller)ps.getAttribute("controller");
		if(controller == null){
			controller = new Controller();
			ps.setAttribute("controller", controller);
			controller.createTestData();
		}
		
		id = controller.getNextId();
		attachedFileList = new ArrayList<String>();
		supervisorList = new ArrayList<Supervisor>();		
		supervisorList.add(new Supervisor());
		
		instituteList.add(new EditableSelectItem(new Integer(0), "IDE"));
		instituteList.add(new EditableSelectItem(new Integer(1), "TEKNAT"));
		
		studyProgramList.add(new EditableSelectItem(new Integer(0), "B-DATA"));
		studyProgramList.add(new EditableSelectItem(new Integer(1), "B-ELEKTRO"));
		bachelor = true;
		type = "Bachelor";
	}
	
	public void actionClear(ActionEvent event) {		
		ps.setAttribute("assignmentBean", new AssignmentBean());
	}
	
	public void actionSetSelectedAssignment(ActionEvent event){
		System.out.println("actionSetSelectedAssignment");
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest pr = (PortletRequest)context.getExternalContext().getRequest();
		PortletSession ps = pr.getPortletSession();
		
		AssignmentBean selectedAssignment = (AssignmentBean)table.getRowData();
		
		ps.setAttribute("assignmentBean", selectedAssignment);
	}
		
	public void actionAddSupervisor(ActionEvent event) {
		supervisorList.add(new Supervisor());
	}
	
	public void actionRemoveSupervisor(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		supervisorList.remove(table.getRowData());		
	}
	
	public void listen(ActionEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		
		String clientId = event.getComponent().getClientId(context);
		clientId = clientId.replaceAll("CreateButton", "");
		
		Map<?,?> parameterMap = context.getExternalContext().getRequestParameterMap();
		
		log.setLevel(Level.ERROR);
		if (log.isDebugEnabled()) {
			log.debug("Title: "+parameterMap.get(clientId+"title"));
			log.debug("Des: "+parameterMap.get(clientId+"description"));
			log.debug("Supervisor: "+parameterMap.get(clientId+"supervisor"));
			log.debug("FacultySupervisor: "+parameterMap.get(clientId+"facultySupervisor"));
			log.debug("Institute: "+parameterMap.get(clientId+"institute"));
			log.debug("StudyProgram: "+parameterMap.get(clientId+"studyProgram"));
			log.debug("NumberOfStudents: "+parameterMap.get(clientId+"numberOfStudents"));
			log.debug("type: "+parameterMap.get(clientId+"type"));
		}
		title = (String)parameterMap.get(clientId+"title");
		description = (String)parameterMap.get(clientId+"description");
		facultySupervisor = (String)parameterMap.get(clientId+"facultySupervisor");
		institute = instituteList.get(instituteNumber).getLabel();
		studyProgram = studyProgramList.get(studyProgramNumber).getLabel();
		numberOfStudents = (String)parameterMap.get(clientId+"numberOfStudents");
		fileUploadErrorMessage = "";

		if(parameterMap.get(clientId+"type").equals("false")){
			if(!parameterMap.get(clientId+"numberOfStudents").equals("1")){
				if(!parameterMap.get(clientId+"numberOfStudents").equals(""))numberOfStudentsError = "Maximum number of students on a master assignment is 1.";
				numberOfStudents = "1";
			}
		}
	}
	
	public void fileUploadListen(ActionEvent event){
		InputFile inputFile =(InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        //file has been saved
        if (fileInfo.isSaved()) {
            fileUploadErrorMessage = "";
            attachedFileList.add(fileInfo.getFileName());
            attachedFilePath = fileInfo.getPhysicalPath();
            attachedFilePath.replace(fileInfo.getFileName(), "");
        }
        //upload failed, generate custom messages
        if (fileInfo.isFailed()) {
            if(fileInfo.getStatus() == FileInfo.INVALID){
                fileUploadErrorMessage = "The attachment could not be uploaded.";
            }
            if(fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED){
                fileUploadErrorMessage = "The attachment exceeded the size limit.";
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_CONTENT_TYPE){
                fileUploadErrorMessage = "The attachment could not be uploaded.";
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_NAME_PATTERN){
                fileUploadErrorMessage = "The attachment can only be a pdf, zip, gif, png, jpeg, jpg, doc or docx file.";
            }
        }
	}
	
	public void radioListener(ValueChangeEvent event){
		if (event.getNewValue().equals(false)){
			master = true;
			bachelor = false;
			type = "Master";
		} else {
			master = false;
			bachelor = true;
			type = "Bachelor";
			numberOfStudentsError = "";
		}
	}

	public void dispose() throws Exception {
	}

	public boolean equals (AssignmentBean ab) {
		return ab.getId() == this.getId();
	}
	
	public String getType() {
		return type;
	}
		
	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(String numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public ArrayList<Supervisor> getSupervisorList() {
		return supervisorList;
	}

	public void setSupervisorList(ArrayList<Supervisor> supervisorList) {
		this.supervisorList = supervisorList;
	}

	public String getFacultySupervisor() {
		return facultySupervisor;
	}

	public void setFacultySupervisor(String facultySupervisor) {
		this.facultySupervisor = facultySupervisor;
	}

	public String getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(String studyProgram) {
		this.studyProgram = studyProgram;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public boolean isBachelor() {
		return bachelor;
	}

	public void setBachelor(boolean bachelor) {
		this.bachelor = bachelor;
	}

	public String getFileUploadErrorMessage() {
		return fileUploadErrorMessage;
	}

	public void setFileUploadErrorMessage(String fileUploadErrorMessage) {
		this.fileUploadErrorMessage = fileUploadErrorMessage;
	}
	
	public String getNumberOfStudentsError() {
		return numberOfStudentsError;
	}

	public void setNumberOfStudentsError(String numberOfStudentsError) {
		this.numberOfStudentsError = numberOfStudentsError;
	}

	public ArrayList<String> getAttachedFilePathList() {
		return attachedFileList;
	}

	public void setAttachedFilePath(ArrayList<String> attachedFilePathList) {
		this.attachedFileList = attachedFilePathList;
	}

	public ArrayList<SelectItem> getInstituteList() {
		return instituteList;
	}

	public void setInstituteList(ArrayList<SelectItem> instituteList) {
		this.instituteList = instituteList;
	}

	public int getInstituteNumber() {
		return instituteNumber;
	}

	public void setInstituteNumber(int instituteNumber) {
		this.instituteNumber = instituteNumber;
	}

	public ArrayList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	public void setStudyProgramList(ArrayList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}

	public int getStudyProgramNumber() {
		return studyProgramNumber;
	}

	public void setStudyProgramNumber(int studyProgramNumber) {
		this.studyProgramNumber = studyProgramNumber;
	}

	public static int getLastId() {
		return lastId;
	}

	public static void setLastId(int lastId) {
		AssignmentBean.lastId = lastId;
	}

	@Override
	public int compareTo(Object arg0) {
		AssignmentBean inputAssignment = (AssignmentBean)arg0;
		if (inputAssignment.getId() > this.getId()) return -1;
		else if (inputAssignment.getId() < this.getId()) return 1;
		else return 0;
	}

	public String getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(String attachedFilePath) {
		this.attachedFilePath = attachedFilePath;
	}

	public ArrayList<String> getAttachedFileList() {
		return attachedFileList;
	}

	public void setAttachedFileList(ArrayList<String> attachedFileList) {
		this.attachedFileList = attachedFileList;
	}
}
