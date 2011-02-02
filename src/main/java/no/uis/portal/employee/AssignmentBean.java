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
import javax.faces.model.SelectItem;
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
	
	private ArrayList<SelectItem> instituteList;
	private ArrayList<SelectItem> studyProgramList = new ArrayList<SelectItem>();
	private ArrayList<ArrayList<SelectItem>> allStudyProgramsByInstitutesList;
	
	private boolean master;
	private boolean bachelor;
	
	private int instituteNumber;
	private int studyProgramNumber;
	private int id;
		
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
	private Logger log = Logger.getLogger(AssignmentBean.class); 
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public AssignmentBean(){
		context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
		
		setListsFromSession();
		
		if(instituteList == null) {
			initializeInsituteAndStudyProgramLists();
			System.out.println("Kaller initialize-metoden");
		}
		
		Controller controller = (Controller)portletSession.getAttribute("controller");
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
	
	private void setListsFromSession(){
			instituteList = (ArrayList<SelectItem>)portletSession.getAttribute("instituteList");
			allStudyProgramsByInstitutesList = (ArrayList<ArrayList<SelectItem>>)portletSession.getAttribute("allStudyProgramsByInstitutesList");
			studyProgramList = (ArrayList<SelectItem>)portletSession.getAttribute("studyProgramList");
	}
	
	private void initializeInsituteAndStudyProgramLists(){
		instituteList = new ArrayList<SelectItem>();
		allStudyProgramsByInstitutesList = new ArrayList<ArrayList<SelectItem>>();
		
		instituteList.add(new EditableSelectItem(new Integer(0), "Institutt for industriell økonomi, risikostyring og planlegging"));
		instituteList.add(new EditableSelectItem(new Integer(1), "Petroleumsteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(2), "Data- og elektroteknikk"));
		instituteList.add(new EditableSelectItem(new Integer(3), "Institutt for konstruksjonsteknikk og materialteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(4), "Matematikk og naturvitskap"));
		
		ArrayList<SelectItem> listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), "Industriell økonomi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), "Boreteknologi"));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Petroleumsgeologi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), "Data"));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Elektro"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Informasjonsteknologi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		allStudyProgramsByInstitutesList.add(listToAdd);
		studyProgramList = allStudyProgramsByInstitutesList.get(0);
		portletSession.setAttribute("instituteList", instituteList);
		portletSession.setAttribute("allStudyProgramsByInstitutesList", allStudyProgramsByInstitutesList);
		portletSession.setAttribute("studyProgramList", studyProgramList);
	}
	
	public void actionClear(ActionEvent event) {		
		portletSession.setAttribute("assignmentBean", new AssignmentBean());
	}
	
	public void actionUpdateStudyProgramList(ValueChangeEvent event){
		System.out.println(event.getNewValue());
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
	}
	
	public void actionSetSelectedAssignment(ActionEvent event){
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		AssignmentBean selectedAssignment = (AssignmentBean)table.getRowData();
		
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
		
		setInstitute(instituteList.get(instituteNumber).getLabel());
		setStudyProgram(studyProgramList.get(studyProgramNumber).getLabel());
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

	public ArrayList<ArrayList<SelectItem>> getAllStudyProgramsByInstitutesList() {
		return allStudyProgramsByInstitutesList;
	}

	public void setAllStudyProgramsByInstitutesList(
			ArrayList<ArrayList<SelectItem>> allStudyProgramsByInstitutesListIn) {
		  allStudyProgramsByInstitutesList = allStudyProgramsByInstitutesListIn;
	}

}
