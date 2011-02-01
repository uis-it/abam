package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Logger;
import com.icesoft.faces.context.DisposableBean;

public class EditBean implements DisposableBean {

	private Logger log = Logger.getLogger(EditBean.class); 
	
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
		UIComponent uic = event.getComponent();
		HtmlForm form = (HtmlForm)uic.getParent();
		
		List<UIComponent> children = form.getChildren();
		HtmlDataTable table = new HtmlDataTable();
		
		for (UIComponent child : children) {
			if(child.getId().equals("instituteTable")){
				table = (HtmlDataTable)child;
				ArrayList<EditableSelectItem> list = (ArrayList<EditableSelectItem>)table.getValue();
				EditableSelectItem newItem = new EditableSelectItem(new Integer(list.size()), "");
				newItem.setEditable(true);
				list.add(newItem);
			}
		}		
	}
	
	public void actionRemoveInstitute(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		ArrayList<EditableSelectItem> list = (ArrayList<EditableSelectItem>)table.getValue();
		
		list.remove(table.getRowData());			
	}
	
	public void actionAddNewStudyProgram(ActionEvent event){
		AssignmentBean ab = (AssignmentBean)portletSession.getAttribute("assignmentBean");
		ArrayList<SelectItem> studyProgramList = ab.getStudyProgramList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(studyProgramList.size()), "");
		newItem.setEditable(true);
		System.out.println("Before add: "+studyProgramList.size());
		System.out.println("Before add ab: "+ab.getStudyProgramList().size());
		studyProgramList.add(newItem);
		System.out.println("after add: "+studyProgramList.size());
	/*	UIComponent uic = event.getComponent();
		HtmlForm form = (HtmlForm)uic.getParent();
		
		List<UIComponent> children = form.getChildren();
		HtmlDataTable table = new HtmlDataTable();
		
		for (UIComponent child : children) {
			if(child.getId().equals("studyProgramTable")){
				table = (HtmlDataTable)child;
				ArrayList<EditableSelectItem> list = (ArrayList<EditableSelectItem>)table.getValue();
				EditableSelectItem newItem = new EditableSelectItem(new Integer(list.size()), "");
				newItem.setEditable(true);
				list.add(newItem);
				
			}
		}		*/
	}
	
	public void actionRemoveStudyProgram(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		ArrayList<EditableSelectItem> list = (ArrayList<EditableSelectItem>)table.getValue();
		
		list.remove(table.getRowData());			
	}
	
	public void dispose() throws Exception {
		
	}

}
