package no.uis.abam.dom;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="ExternalExaminer")
@Inheritance(strategy=InheritanceType.JOINED)
public class ExternalExaminer extends AbamPerson {
	
  private static final long serialVersionUID = 1L;
  
  private String title;
	private String streetAddress;
	private String postalCodeAndPlace;
	private String placeOfEmployment;
	
	public ExternalExaminer() {}

	public ExternalExaminer(String name) {
		super(name);		
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCodeAndPlace() {
		return postalCodeAndPlace;
	}

	public void setPostalCodeAndPlace(String postalCodeAndPlace) {
		this.postalCodeAndPlace = postalCodeAndPlace;
	}

	public String getPlaceOfEmployment() {
		return placeOfEmployment;
	}

	public void setPlaceOfEmployment(String placeOfEmployment) {
		this.placeOfEmployment = placeOfEmployment;
	}
}
