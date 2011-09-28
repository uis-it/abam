package no.uis.abam.dom;

import java.util.List;

public class Employee extends AbamPerson{

	private String employeeId;
	private List<String> groupMembership;
	
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

	public List<String> getGroupMembership() {
		return groupMembership;
	}

	public void setGroupMembership(List<String> groupMembership) {
		this.groupMembership = groupMembership;
	}
	
}
