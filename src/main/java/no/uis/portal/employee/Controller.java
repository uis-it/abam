package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
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
	
	private PortletRequest pr;
	private PortletSession ps;
	
	public Controller() {
		FacesContext context = FacesContext.getCurrentInstance();
		pr = (PortletRequest)context.getExternalContext().getRequest();
		ps = pr.getPortletSession();
	}
	
	public void createTestData(){
		AssignmentBean test1 = new AssignmentBean();
		test1.setTitle("Test1 webutviklings oppgave");
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		
		AssignmentBean test2 = new AssignmentBean();
		test2.setTitle("Test2 webutviklings oppgave");
		test2.setBachelor(false);
		test2.setMaster(true);
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setId(2);
		
		assignmentList.add(test1);
		assignmentList.add(test2);
	}

	public int getNextId(){
		return assignmentList.size()+1;
	}
	
	public void actionSaveAssignment(ActionEvent event) {
		AssignmentBean ab = (AssignmentBean)ps.getAttribute("assignmentBean");
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
