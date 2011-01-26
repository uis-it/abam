package no.uis.portal.employee;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import com.icesoft.faces.context.DisposableBean;

public class EditBean implements DisposableBean {

	private ArrayList<SelectItem> instituteList;
	private ArrayList<SelectItem> studyProgramList;
	
	private ArrayList<SelectItem> selectedList;
 	
	private Logger log = Logger.getLogger(EditBean.class); 
	
	public EditBean(){
		
	}

	public void setEditableAction(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		
		EditableSelectItem editItem = (EditableSelectItem)table.getRowData();
		if (editItem.isEditable()){
			editItem.setEditable(false);
		} else {
			editItem.setEditable(true);
		}
	
	}
	
	public void dispose() throws Exception {
		
	}
	public ArrayList<SelectItem> getInstituteList() {
		return instituteList;
	}

	public void setInstituteList(ArrayList<SelectItem> instituteList) {
		this.instituteList = instituteList;
	}

	public ArrayList<SelectItem> getStudyProgramList() {
		return studyProgramList;
	}

	public void setStudyProgramList(ArrayList<SelectItem> studyProgramList) {
		this.studyProgramList = studyProgramList;
	}

	public ArrayList<SelectItem> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(ArrayList<SelectItem> selectedList) {
		this.selectedList = selectedList;
	}
}
