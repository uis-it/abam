package no.uis.abam.dom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity(name="Employee")
@Inheritance(strategy=InheritanceType.JOINED)
public class Employee extends AbamPerson {

  private static final long serialVersionUID = 1L;

	private String employeeId;

	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<AbamGroup> groups;
	
	public Employee() {
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

  public List<AbamGroup> getGroups() {
    if (groups == null) {
      groups = new ArrayList<AbamGroup>();
    }
    return groups;
  }

  public void setGroups(List<AbamGroup> groups) {
    this.groups = groups;
  }
}
