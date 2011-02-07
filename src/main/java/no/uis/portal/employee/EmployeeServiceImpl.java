package no.uis.portal.employee;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import no.uis.portal.employee.domain.AssigmentIdComparator;
import no.uis.portal.employee.domain.Assignment;

public class EmployeeServiceImpl implements EmployeeService {

	private TreeSet<Assignment> assignmentList = new TreeSet<Assignment>(new AssigmentIdComparator()); 
	private EmployeeService selectedAssignment;
	
	private PortletRequest portletRequest;
	private PortletSession portletSession;
	
	private LinkedList<SelectItem> instituteList;
	private LinkedList<SelectItem> studyProgramList = new LinkedList<SelectItem>();
	private LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesList;
	
	private int selectedInstituteNumber;
	private int selectedStudyProgramNumber;
	
	public EmployeeServiceImpl() {
		FacesContext context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
		setListsFromSession();
		
		if(instituteList == null) {
			initializeInsituteAndStudyProgramLists();
		}
	}
	
	@Override
	public int getSelectedInstituteNumber() {
		return selectedInstituteNumber;
	}

	@Override
	public void setSelectedInstituteNumber(int selectedInstituteNumber) {
		this.selectedInstituteNumber = selectedInstituteNumber;
	}

	@Override
	public int getSelectedStudyProgramNumber() {
		return selectedStudyProgramNumber;
	}

	@Override
	public void setSelectedStudyProgramNumber(int selectedStudyProgramNumber) {
		this.selectedStudyProgramNumber = selectedStudyProgramNumber;
	}

	private String selectedInstitute;
	
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
	
	@Override
	public void createTestData(){
		Assignment test1 = new Assignment();
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
		
		Assignment test2 = new Assignment();
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

	@Override
	public int getNextId(){
		return assignmentList.size()+1;
	}
	
	@Override
	public void saveAssignment(Assignment assignment) {
		assignmentList.add(assignment);
	}
	
	@Override
	public TreeSet<Assignment> getAssignmentList() {
		return assignmentList;
	}

	@Override
	public EmployeeService getSelectedAssignment() {
		return selectedAssignment;
	}

	@Override
	public void setSelectedAssignment(EmployeeService selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
	
	@Override
	public void actionClearStudyProgramAndInstituteNumber(ActionEvent event){
		setSelectedStudyProgramNumber(0);
		setSelectedInstituteNumber(0);
	}
	
	@Override
	public void actionUpdateStudyProgramList(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
		selectedInstitute = (String) instituteList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		selectedInstituteNumber = Integer.parseInt(event.getNewValue().toString());
		TreeSet<Assignment> assignmentList = getAssignmentList();
		for (Assignment assignment : assignmentList) {
			if (assignment.getInstitute().equals(selectedInstitute)
				|| selectedInstitute.equals("")) 
				assignment.setDisplayAssignment(true);
			else assignment.setDisplayAssignment(false);
		}
	}
	
	@Override
	public void actionUpdateStudyProgramListFromCreateAssignment(ValueChangeEvent event){
		studyProgramList = allStudyProgramsByInstitutesList.get(Integer.parseInt(event.getNewValue().toString()));
		//selectedInstitute = (String) instituteList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		selectedInstituteNumber = Integer.parseInt(event.getNewValue().toString());
	}
	
	@Override
	public void actionSetDisplayAssignment(ValueChangeEvent event){
		String selectedStudyProgram = (String) studyProgramList.get(Integer.parseInt(event.getNewValue().toString())).getLabel();
		TreeSet<Assignment> assignmentList = getAssignmentList();
		selectedStudyProgramNumber = Integer.parseInt(event.getNewValue().toString());
		if (selectedInstitute == null) setSelectedInstitute("");
		for (Assignment assignment : assignmentList) {
			if (checkIfAssignmentShouldBeDisplayed(assignment, selectedStudyProgram)) 
				assignment.setDisplayAssignment(true);
			else assignment.setDisplayAssignment(false);
		}
	}
	
	private boolean checkIfAssignmentShouldBeDisplayed(Assignment abIn, String selectedStudyProgram) {
		return (selectedStudyProgram.equals("") && abIn.getInstitute().equals(selectedInstitute)) 
		|| abIn.getStudyProgram().equals(selectedStudyProgram);
	}
	
	@Override
	public LinkedList<SelectItem> getInstituteList() {
		return instituteList;
	}

	@Override
	public void setInstituteList(LinkedList<SelectItem> instituteList) {
		this.instituteList = instituteList;
	}
	@Override
	public LinkedList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	@Override
	public void setStudyProgramList(LinkedList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}
	
	@Override
	public LinkedList<LinkedList<SelectItem>> getAllStudyProgramsByInstitutesList() {
		return allStudyProgramsByInstitutesList;
	}

	@Override
	public void setAllStudyProgramsByInstitutesList(
			LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesListIn) {
		  allStudyProgramsByInstitutesList = allStudyProgramsByInstitutesListIn;
	}

	@Override
	public String getStudyProgram(int index) {
		return studyProgramList.get(index).getLabel();
	}
	@Override
	public String getInstitute(int index) {
		return  instituteList.get(index).getLabel();
	}

	@Override
	public String getSelectedInstitute() {
		return selectedInstitute;
	}

	@Override
	public void setSelectedInstitute(String selectedInstitute) {
		this.selectedInstitute = selectedInstitute;
	}

	@Override
	public void removeAssignment(Assignment assignment) {
		assignmentList.remove(assignment);
	}
}
