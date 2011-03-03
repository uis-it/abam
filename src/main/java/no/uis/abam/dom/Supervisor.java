package no.uis.abam.dom;

public class Supervisor extends Person{
	
	private boolean external;
	
	private String companyName;
	
	public Supervisor() {
	}
	
	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
