package no.uis.portal.student.domain;

public class BachelorStudent extends Student {

	private final String type = "Bachelor"; 
	
	
	public BachelorStudent() {
		super();
	}


	public String getType() {
		return type;
	}
}
