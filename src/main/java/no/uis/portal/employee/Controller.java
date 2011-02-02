package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class Controller {

	private TreeSet<AssignmentBean> assignmentList = new TreeSet<AssignmentBean>(); 
	private AssignmentBean selectedAssignment;
	
	private PortletRequest portletRequest;
	private PortletSession portletSession;
	
	public Controller() {
		FacesContext context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
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
		
}
