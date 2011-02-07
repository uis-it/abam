package no.uis.portal.employee;

import java.util.LinkedList;

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
	private EmployeeService controller;
	public EditBean(){
		FacesContext context = FacesContext.getCurrentInstance();
		portletRequest = (PortletRequest)context.getExternalContext().getRequest();
		portletSession = portletRequest.getPortletSession();
		controller = (EmployeeService)portletSession.getAttribute("controller");
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
		
		LinkedList<SelectItem> instituteList = controller.getInstituteList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(instituteList.size()), "");
		newItem.setEditable(true);
		instituteList.add(newItem);
		
		LinkedList<LinkedList<SelectItem>> allStudyProgramsByInstitutesList = controller.getAllStudyProgramsByInstitutesList();
		allStudyProgramsByInstitutesList.add(new LinkedList<SelectItem>());
		
	}
	
	public void actionRemoveInstitute(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		controller.getInstituteList().remove(table.getRowData());	
	}
	
	public void actionAddNewStudyProgram(ActionEvent event){
		LinkedList<SelectItem> studyProgramList = controller.getStudyProgramList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(studyProgramList.size()), "");
		newItem.setEditable(true);
		studyProgramList.add(newItem);
	}
	
	public void actionRemoveStudyProgram(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		controller.getStudyProgramList().remove(table.getRowData());			
	}
	
	public void dispose() throws Exception {
		
	}

}
