package no.uis.abam.dom;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class AbamPerson extends AbamType {
	
  private static final long serialVersionUID = 1L;

  private String name;
	private String email;
	private String phoneNumber;

	public AbamPerson(String name) {
	}
	
	public AbamPerson() {
	}
	
  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
	  return name;
	}

	public void setName(String name) {
	  this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
