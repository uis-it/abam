package no.uis.abam.dom;


public class AbamPerson {
	
  private String name;
	private String email;
	private String phoneNumber;

	public AbamPerson(String name) {
	}
	
	public AbamPerson() {
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
