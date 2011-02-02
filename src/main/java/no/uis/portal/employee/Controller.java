package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class Controller {

	private TreeSet<AssignmentBean> assignmentList = new TreeSet<AssignmentBean>(); 
	private AssignmentBean selectedAssignment;
	
	private PortletRequest portletRequest;
	private PortletSession portletSession;
	
	private ArrayList<SelectItem> instituteList;
	private ArrayList<SelectItem> studyProgramList = new ArrayList<SelectItem>();
	private ArrayList<ArrayList<SelectItem>> allStudyProgramsByInstitutesList;
	
	private int instituteNumber;
	private int studyProgramNumber;
	
	private String selectedInstitute;
	public Controller() {
		FacesContext context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
		setListsFromSession();
		
		if(instituteList == null) {
			initializeInsituteAndStudyProgramLists();
		}
	}
	
	private void initializeInsituteAndStudyProgramLists(){
		instituteList = new ArrayList<SelectItem>();
		allStudyProgramsByInstitutesList = new ArrayList<ArrayList<SelectItem>>();
		
		instituteList.add(new EditableSelectItem(new Integer(0), ""));
		instituteList.add(new EditableSelectItem(new Integer(1), "Institutt for industriell økonomi, risikostyring og planlegging"));
		instituteList.add(new EditableSelectItem(new Integer(2), "Petroleumsteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(3), "Data- og elektroteknikk"));
		instituteList.add(new EditableSelectItem(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(5), "Matematikk og naturvitskap"));
		
		ArrayList<SelectItem> listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Industriell økonomi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Boreteknologi"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Petroleumsgeologi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new ArrayList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Data"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Elektro"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Informasjonsteknologi"));
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
	
	private void setListsFromSession(){
		instituteList = (ArrayList<SelectItem>)portletSession.getAttribute("instituteList");
		allStudyProgramsByInstitutesList = (ArrayList<ArrayList<SelectItem>>)portletSession.getAttribute("allStudyProgramsByInstitutesList");
		studyProgramList = (ArrayList<SelectItem>)portletSession.getAttribute("studyProgramList");
	}
	
	public void createTestData(){
		AssignmentBean test1 = new AssignmentBean();
		test1.setTitle("Test1 webutviklings oppgave");
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setInstitute("Petroleumsteknologi");
		test1.setStudyProgram("Boreteknologi");
		test1.setAddedDate(new GregorianCalendar(10, 11, 10));
		GregorianCalendar dato = test1.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test1.setExpireDate(dato);
		
		AssignmentBean test2 = new AssignmentBean();
		test2.setTitle("Test2 webutviklings oppgave");
		test2.setBachelor(false);
		test2.setMaster(true);
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setInstitute("Data- og elektroteknikk");
		test2.setStudyProgram("Elektro");
		test2.setId(2);
		test2.setAddedDate(new GregorianCalendar(2010, 10, 10));
		dato = test2.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test2.setExpireDate(dato);
		assignmentList.add(test1);
		assignmentList.add(test2);
	}

	public int getNextId(){
		return assignmentList.size()+1;
	}
	
	public void actionSaveAssignment(ActionEvent event) {
		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		assignmentList.add(ab);
	}
	
	public void actionRemoveAssignment(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		AssignmentBean assignment = (AssignmentBean)table.getRowData();
		
		assignmentList.remove(assignment);
	}
	
	public TreeSet<AssignmentBean> getAssignmentList() {
		return assignmentList;
	}

	public AssignmentBean getSelectedAssignment() {
		return selectedAssignment;
	}

	public void setSelectedAssignment(AssignmentBean selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
	
	public void actionUpdateStudyProgramList(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
		selectedInstitute = (String) instituteList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		TreeSet<AssignmentBean> assignmentList = getAssignmentList();
		for (AssignmentBean assignmentBean : assignmentList) {
			if (assignmentBean.getInstitute().equals(selectedInstitute)
				|| selectedInstitute.equals("")) 
				assignmentBean.setDisplayAssignment(true);
			else assignmentBean.setDisplayAssignment(false);
		}
	}
	
	public void actionSetDisplayAssignment(ValueChangeEvent event){
		String selectedStudyProgram = (String) studyProgramList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		TreeSet<AssignmentBean> assignmentList = getAssignmentList();
		if (selectedInstitute == null) setSelectedInstitute("");
		for (AssignmentBean assignmentBean : assignmentList) {
			if (checkIfAssignmentShouldBeDisplayed(assignmentBean, selectedStudyProgram)) 
				assignmentBean.setDisplayAssignment(true);
			else assignmentBean.setDisplayAssignment(false);
		}
	}
	
	private boolean checkIfAssignmentShouldBeDisplayed(AssignmentBean abIn, String selectedStudyProgram) {
		return (selectedStudyProgram.equals("") && abIn.getInstitute().equals(selectedInstitute)) 
		|| abIn.getStudyProgram().equals(selectedStudyProgram);
	}
	
	public ArrayList<SelectItem> getInstituteList() {
		return instituteList;
	}

	public void setInstituteList(ArrayList<SelectItem> instituteList) {
		this.instituteList = instituteList;
	}
	public ArrayList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	public void setStudyProgramList(ArrayList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}
	
	public ArrayList<ArrayList<SelectItem>> getAllStudyProgramsByInstitutesList() {
		return allStudyProgramsByInstitutesList;
	}

	public void setAllStudyProgramsByInstitutesList(
			ArrayList<ArrayList<SelectItem>> allStudyProgramsByInstitutesListIn) {
		  allStudyProgramsByInstitutesList = allStudyProgramsByInstitutesListIn;
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
	
	public String getStudyProgram() {
		return studyProgramList.get(studyProgramNumber).getLabel();
	}
	public String getInstitute() {
		return  instituteList.get(instituteNumber).getLabel();
	}

	public String getSelectedInstitute() {
		return selectedInstitute;
	}

	public void setSelectedInstitute(String selectedInstitute) {
		this.selectedInstitute = selectedInstitute;
	}
}
