package no.uis.portal.student.domain;

public class MasterStudent extends Student {

	private final String type = "Master"; 
	
	public MasterStudent() {
		super();
	}

	public String getType() {
		return type;
	}
	
}
