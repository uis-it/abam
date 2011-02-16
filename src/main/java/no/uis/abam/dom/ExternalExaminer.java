package no.uis.abam.dom;

public class ExternalExaminer extends Person{
	
	private String title;
	private String streetAddress;
	private String postalCodeAndPlace;
	private String placeOfEmployment;
	
	public ExternalExaminer() {
	}

	public ExternalExaminer(String name) {
		super();
		super.setName(name);
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
