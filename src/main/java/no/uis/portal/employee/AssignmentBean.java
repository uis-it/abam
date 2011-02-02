package no.uis.portal.employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.DisposableBean;

public class AssignmentBean implements DisposableBean, Comparable {

	private final int ACTIVE_MONTHS = 6;
	
	private boolean master;
	private boolean bachelor;
	private boolean displayAssignment = true;
	
	private int id;
	private int instituteNumber;
	private int studyProgramNumber;
		
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
	
	private GregorianCalendar addedDate;
	private GregorianCalendar expireDate;
	
	private ArrayList<Supervisor> supervisorList;
	private ArrayList<String> attachedFileList;
	
	private FacesContext context;
	private PortletRequest portletRequest;
	private PortletSession portletSession;
	private Controller controller;
	private Logger log = Logger.getLogger(AssignmentBean.class); 
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public AssignmentBean(){
		context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
		
		controller = (Controller)portletSession.getAttribute("controller");
		if(controller == null){
			controller = new Controller();
			portletSession.setAttribute("controller", controller);
			controller.createTestData();
		}
		
		id = controller.getNextId();
		attachedFileList = new ArrayList<String>();
		supervisorList = new ArrayList<Supervisor>();		
		supervisorList.add(new Supervisor());
		
		bachelor = true;
		type = "Bachelor";
	}
	
	public void actionClear(ActionEvent event) {		
		portletSession.setAttribute("assignmentBean", new AssignmentBean());
	}
	
	public void actionSetSelectedAssignment(ActionEvent event){
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		AssignmentBean selectedAssignment = (AssignmentBean)table.getRowData();
		System.out.println("Title: "+ selectedAssignment.getTitle() + " studyProg: "+selectedAssignment.getStudyProgram());
		System.out.println("studyProgNo: "+ selectedAssignment.getStudyProgramNumber());
		System.out.println("instNo: " + selectedAssignment.getInstituteNumber());
		controller.setStudyProgramList(
				controller.getAllStudyProgramsByInstitutesList().
					get(selectedAssignment.getInstituteNumber())
				);
		portletSession.setAttribute("assignmentBean", selectedAssignment);
	}
		
	public void actionAddSupervisor(ActionEvent event) {
		supervisorList.add(new Supervisor());
	}
	
	public void actionRemoveSupervisor(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		supervisorList.remove(table.getRowData());		
	}
	
	public void actionCreateAssignment(ActionEvent event) {
		String clientId = event.getComponent().getClientId(context);
		clientId = clientId.replaceAll("CreateButton", "");
		
		Map<?,?> parameterMap = context.getExternalContext().getRequestParameterMap();
		
		log.setLevel(Level.DEBUG);
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
		System.out.println("controller.getInstitute(instituteNumber): "+ controller.getInstitute(instituteNumber));
		System.out.println("instituteNumber: "+instituteNumber);
		System.out.println("controller.getStudyProgram(studyProgramNumber): "+ controller.getStudyProgram(studyProgramNumber));
		System.out.println("studyProgramNumber: "+studyProgramNumber);
		setInstitute(controller.getInstitute(instituteNumber));
		setInstituteNumber(controller.getSelectedInstituteNumber());
		setStudyProgram(controller.getStudyProgram(studyProgramNumber));
		//setStudyProgramNumber(controller.getSelectedStudyProgramNumber());
		setFileUploadErrorMessage("");
		setAddedDate(new GregorianCalendar());
		setExpireDate(new GregorianCalendar());
		expireDate.add(Calendar.MONTH, ACTIVE_MONTHS);
		setType("Bachelor");
		
		String numberOfStudentsInput = (String)parameterMap.get(clientId+"numberOfStudents");
		if (numberOfStudentsInput == null) numberOfStudentsInput = "1";
		String typeAssignment = (String)parameterMap.get(clientId+"type");
		if(typeAssignment != null && typeAssignment.equals("false")){
			if(!numberOfStudentsInput.equals("1")){
				numberOfStudents = "1";
				setType("Master");
				if(!numberOfStudentsInput.equals(""))
					numberOfStudentsError = "Maximum number of students on a master assignment is 1.";				
			}
		}
	}
	
	public String getAddedDateAsString() {		
		return simpleDateFormatter.format(addedDate.getTime());
	}
	
	public String getExpireDateAsString() {		
		return simpleDateFormatter.format(expireDate.getTime());
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
	
	public void actionRemoveAttachment(ActionEvent event){
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
	    attachedFileList.remove(table.getRowData());		
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

	public GregorianCalendar getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(GregorianCalendar addedDate) {
		this.addedDate = addedDate;
	}

	public GregorianCalendar getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(GregorianCalendar expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isDisplayAssignment() {
		return displayAssignment;
	}

	public void setDisplayAssignment(boolean displayAssignment) {
		this.displayAssignment = displayAssignment;
	}

	public int getInstituteNumber() {
		return instituteNumber;
	}

	public void setInstituteNumber(int instituteNumber) {
		this.instituteNumber = instituteNumber;
	}

	public int getStudyProgramNumber() {
		return studyProgramNumber;
	}

	public void setStudyProgramNumber(int studyProgramNumber) {
		this.studyProgramNumber = studyProgramNumber;
	}

}
