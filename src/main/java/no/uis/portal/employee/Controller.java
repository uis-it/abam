package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class Controller {

	private ArrayList<AssignmentBean> assignmentList = new ArrayList<AssignmentBean>(); 
	private AssignmentBean selectedAssignment;
	
	
	public Controller() {
		AssignmentBean test1 = new AssignmentBean();
		test1.setTitle("Test1 webutviklings oppgave");
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(300);
		
		AssignmentBean test2 = new AssignmentBean();
		test2.setTitle("Test2 webutviklings oppgave");
		test2.setBachelor(false);
		test2.setMaster(true);
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setId(301);
		
		assignmentList.add(test1);
		assignmentList.add(test2);
	}

	public void actionSaveAssignment(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest pr = (PortletRequest)context.getExternalContext().getRequest();
		PortletSession ps = pr.getPortletSession();
		AssignmentBean ab = (AssignmentBean)ps.getAttribute("assignmentBean");
		assignmentList.add(ab);
	}
	
	public ArrayList<AssignmentBean> getAssignmentList() {
		return assignmentList;
	}

	public AssignmentBean getSelectedAssignment() {
		return selectedAssignment;
	}

	public void setSelectedAssignment(AssignmentBean selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
}
