package no.uis.abam.dom;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="Supervisor")
@Inheritance(strategy=InheritanceType.JOINED)
public class Supervisor extends AbamPerson {

  private static final long serialVersionUID = 1L;

  private boolean external;
	
	private String companyName;
	
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
