package no.uis.abam.dom;

import java.util.List;

public class Employee extends Person{

	private String employeeId;
	private List<Group> groupMembership;
	
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

	public List<Group> getGroupMembership() {
		return groupMembership;
	}

	public void setGroupMembership(List<Group> groupMembership) {
		this.groupMembership = groupMembership;
	}
	
}
