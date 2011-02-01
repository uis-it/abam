package no.uis.portal.employee;

import java.util.ArrayList;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import com.icesoft.faces.context.DisposableBean;

public class EditBean implements DisposableBean {

	private PortletRequest portletRequest;
	private PortletSession portletSession;
	
	public EditBean(){
		FacesContext context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
	}

	public void actionSetEditable(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		EditableSelectItem editItem = (EditableSelectItem)table.getRowData();
		if (editItem.isEditable()){
			editItem.setEditable(false);
		} else {
			editItem.setEditable(true);
		}
	
	}
	
	public void actionAddNewInstitute(ActionEvent event){
		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		ArrayList<SelectItem> instituteList = ab.getInstituteList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(instituteList.size()), "");
		newItem.setEditable(true);
		instituteList.add(newItem);
		
		ArrayList<ArrayList<SelectItem>> allStudyProgramsByInstitutesList = ab.getAllStudyProgramsByInstitutesList();
		allStudyProgramsByInstitutesList.add(new ArrayList<SelectItem>());
		
	}
	
	public void actionRemoveInstitute(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();

		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		ab.getInstituteList().remove(table.getRowData());	
	}
	
	public void actionAddNewStudyProgram(ActionEvent event){
		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		ArrayList<SelectItem> studyProgramList = ab.getStudyProgramList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(studyProgramList.size()), "");
		newItem.setEditable(true);
		studyProgramList.add(newItem);
	}
	
	public void actionRemoveStudyProgram(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		ab.getStudyProgramList().remove(table.getRowData());			
	}
	
	public void dispose() throws Exception {
		
	}

}
