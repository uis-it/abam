package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
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
	
	private LinkedList<SelectItem> instituteList;
	private LinkedList<SelectItem> studyProgramList = new LinkedList<SelectItem>();
	private LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesList;
	
	private int selectedInstituteNumber;
	private int selectedStudyProgramNumber;
	
	public int getSelectedInstituteNumber() {
		return selectedInstituteNumber;
	}

	public void setSelectedInstituteNumber(int selectedInstituteNumber) {
		this.selectedInstituteNumber = selectedInstituteNumber;
	}

	public int getSelectedStudyProgramNumber() {
		return selectedStudyProgramNumber;
	}

	public void setSelectedStudyProgramNumber(int selectedStudyProgramNumber) {
		this.selectedStudyProgramNumber = selectedStudyProgramNumber;
	}

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
		instituteList = new LinkedList<SelectItem>();
		allStudyProgramsByInstitutesList = new LinkedList<LinkedList<SelectItem>>();
		
		instituteList.add(new EditableSelectItem(new Integer(0), ""));
		instituteList.add(new EditableSelectItem(new Integer(1), "Institutt for industriell økonomi, risikostyring og planlegging"));
		instituteList.add(new EditableSelectItem(new Integer(2), "Petroleumsteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(3), "Data- og elektroteknikk"));
		instituteList.add(new EditableSelectItem(new Integer(4), "Institutt for konstruksjonsteknikk og materialteknologi"));
		instituteList.add(new EditableSelectItem(new Integer(5), "Matematikk og naturvitskap"));
		
		LinkedList<SelectItem> listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "banan"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Industriell økonomi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Boreteknologi"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Petroleumsgeologi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		listToAdd.add(new EditableSelectItem(new Integer(1), "Data"));
		listToAdd.add(new EditableSelectItem(new Integer(2), "Elektro"));
		listToAdd.add(new EditableSelectItem(new Integer(3), "Informasjonsteknologi"));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		allStudyProgramsByInstitutesList.add(listToAdd);
		
		listToAdd = new LinkedList<SelectItem>();
		listToAdd.add(new EditableSelectItem(new Integer(0), ""));
		allStudyProgramsByInstitutesList.add(listToAdd);
		studyProgramList = allStudyProgramsByInstitutesList.get(0);
		portletSession.setAttribute("instituteList", instituteList);
		portletSession.setAttribute("allStudyProgramsByInstitutesList", allStudyProgramsByInstitutesList);
		portletSession.setAttribute("studyProgramList", studyProgramList);
	}
	
	private void setListsFromSession(){
		instituteList = (LinkedList<SelectItem>)portletSession.getAttribute("instituteList");
		allStudyProgramsByInstitutesList = (LinkedList<LinkedList<SelectItem>>)portletSession.getAttribute("allStudyProgramsByInstitutesList");
		studyProgramList = (LinkedList<SelectItem>)portletSession.getAttribute("studyProgramList");
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
	
	public void actionClearStudyProgramAndInstituteNumber(ActionEvent event){
		setSelectedStudyProgramNumber(0);
		setSelectedInstituteNumber(0);
	}
	
	public void actionUpdateStudyProgramList(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
		selectedInstitute = (String) instituteList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		selectedInstituteNumber = Integer.parseInt(event.getNewValue().toString());
		TreeSet<AssignmentBean> assignmentList = getAssignmentList();
		for (AssignmentBean assignmentBean : assignmentList) {
			if (assignmentBean.getInstitute().equals(selectedInstitute)
				|| selectedInstitute.equals("")) 
				assignmentBean.setDisplayAssignment(true);
			else assignmentBean.setDisplayAssignment(false);
		}
	}
	
	public void actionUpdateStudyProgramListFromCreateAssignment(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
		//selectedInstitute = (String) instituteList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		selectedInstituteNumber = Integer.parseInt(event.getNewValue().toString());
	}
	
	public void actionSetDisplayAssignment(ValueChangeEvent event){
		String selectedStudyProgram = (String) studyProgramList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		TreeSet<AssignmentBean> assignmentList = getAssignmentList();
		selectedStudyProgramNumber = Integer.parseInt(event.getNewValue().toString());
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
	
	public void actionSetSelectedAssignment(ActionEvent event){
		UIComponent uic = event.getComponent();

		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		AssignmentBean selectedAssignment = (AssignmentBean)table.getRowData();
		
		setStudyProgramList(getAllStudyProgramsByInstitutesList().
			get(selectedAssignment.getInstituteNumber()));
		setSelectedInstituteNumber(selectedAssignment.getInstituteNumber());
		setSelectedStudyProgramNumber(selectedAssignment.getStudyProgramNumber());
		portletSession.setAttribute("assignmentBean", selectedAssignment);
	}
	
	public LinkedList<SelectItem> getInstituteList() {
		return instituteList;
	}

	public void setInstituteList(LinkedList<SelectItem> instituteList) {
		this.instituteList = instituteList;
	}
	public LinkedList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	public void setStudyProgramList(LinkedList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}
	
	public LinkedList<LinkedList<SelectItem>> getAllStudyProgramsByInstitutesList() {
		return allStudyProgramsByInstitutesList;
	}

	public void setAllStudyProgramsByInstitutesList(
			LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesListIn) {
		  allStudyProgramsByInstitutesList = allStudyProgramsByInstitutesListIn;
	}

	public String getStudyProgram(int index) {
		return studyProgramList.get(index).getLabel();
	}
	public String getInstitute(int index) {
		return  instituteList.get(index).getLabel();
	}

	public String getSelectedInstitute() {
		return selectedInstitute;
	}

	public void setSelectedInstitute(String selectedInstitute) {
		this.selectedInstitute = selectedInstitute;
	}
}
