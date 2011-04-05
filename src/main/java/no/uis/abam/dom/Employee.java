package no.uis.abam.dom;

public class Employee extends Person{

	private String employeeId;
	
	public Employee() {
		
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public boolean equals(Employee employee) {
		return this.getEmployeeId().equals(employee.getEmployeeId());
	}
}
