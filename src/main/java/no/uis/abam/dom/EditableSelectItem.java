package no.uis.abam.dom;

import javax.faces.model.SelectItem;

public class EditableSelectItem extends SelectItem {

	private static final long serialVersionUID = 1L;

	private boolean editable;

	public EditableSelectItem(){}

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

	public boolean equals(Object inObject) {
		return this.getLabel().equalsIgnoreCase(
				((EditableSelectItem) inObject).getLabel());
	}
}
