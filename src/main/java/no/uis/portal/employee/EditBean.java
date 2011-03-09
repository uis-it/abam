package no.uis.portal.employee;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

import no.uis.abam.dom.EditableSelectItem;

import com.icesoft.faces.context.DisposableBean;

public class EditBean implements DisposableBean {

	private EmployeeService employeeService;
	
	public EditBean(){
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
	
	public void actionAddNewDepartment(ActionEvent event){
		employeeService.addNewDepartment();
			
	}
	
	public void actionRemoveDepartment(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		employeeService.removeDepartment((EditableSelectItem) table.getRowData());	
	}
	
	public void actionAddNewStudyProgram(ActionEvent event){
		List<EditableSelectItem> studyProgramList = employeeService.getSelectedStudyProgramList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(studyProgramList.size()), "");
		newItem.setEditable(true);
		studyProgramList.add(newItem);
	}
	
	public void actionSaveDepartmentListToWebService(ActionEvent event) {
		employeeService.saveDepartmentListToWebService();
	}
	
	public void actionGetListsFromWebService(ActionEvent event) {
		employeeService.getDepartmentListFromWebService();
		//employeeService.getAllStudyProgramsByDepartmentListFromWebService();
	}
	
	public void actionRemoveStudyProgram(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		employeeService.getSelectedStudyProgramList().remove(table.getRowData());			
	}
	
	public void dispose() throws Exception {
		
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
