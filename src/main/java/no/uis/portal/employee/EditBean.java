package no.uis.portal.employee;

import java.util.LinkedList;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
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
		
		LinkedList<SelectItem> departmentList = employeeService.getDepartmentList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(departmentList.size()), "");
		newItem.setEditable(true);
		departmentList.add(newItem);
		
		LinkedList<LinkedList<SelectItem>> allStudyProgramsByDepartmentsList = employeeService.getAllStudyProgramsByDepartmentsList();
		allStudyProgramsByDepartmentsList.add(new LinkedList<SelectItem>());
		
	}
	
	public void actionRemoveDepartment(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		employeeService.getDepartmentList().remove(table.getRowData());	
	}
	
	public void actionAddNewStudyProgram(ActionEvent event){
		LinkedList<SelectItem> studyProgramList = employeeService.getStudyProgramList();
		EditableSelectItem newItem = new EditableSelectItem(new Integer(studyProgramList.size()), "");
		newItem.setEditable(true);
		studyProgramList.add(newItem);
	}
	
	public void actionRemoveStudyProgram(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		employeeService.getStudyProgramList().remove(table.getRowData());			
	}
	
	public void dispose() throws Exception {
		
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
