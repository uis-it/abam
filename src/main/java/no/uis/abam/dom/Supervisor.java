package no.uis.abam.dom;

public class Supervisor extends Person{
	private String companyName;
	private boolean external;
	
	public Supervisor() {
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}	
}
