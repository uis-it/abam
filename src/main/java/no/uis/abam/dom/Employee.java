package no.uis.abam.dom;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity(name="Employee")
@Inheritance(strategy=InheritanceType.JOINED)
public class Employee extends AbamPerson {

  private static final long serialVersionUID = 1L;

	private String employeeId;

	@ElementCollection(fetch=FetchType.LAZY)
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
