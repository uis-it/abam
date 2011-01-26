package no.uis.portal.employee;

import javax.faces.model.SelectItem;

public class EditableSelectItem extends SelectItem {

	private boolean editable;
	
	public EditableSelectItem(Integer value, String label) {
		super(value, label);
		editable = false;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
