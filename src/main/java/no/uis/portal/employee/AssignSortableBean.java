package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;

import no.uis.abam.dom.Application;

public class AssignSortableBean {

	private static final String assignmentTitleColumnName = "Assignment Title";
	private static final String studentColumnName = "Student";
	private static final String priorityNumberColumnName = "Priority";
	private static final String facultySupervisorColumnName = "Faculty Supervisor";

	private String sortColumnName;
	private boolean ascending;

	private String oldSort;
	private boolean oldAscending;

	private EmployeeService employeeService;

	private Application[] applicationArray;

	public AssignSortableBean() {
		sortColumnName = assignmentTitleColumnName;
		ascending = true;

		oldSort = sortColumnName;
		oldAscending = !ascending;
	}

	public void sort() {
		System.out.println("Sort");
		Comparator comparator = new Comparator(){
			public int compare(Object obj1, Object obj2) {
				Application app1 = (Application)obj1;
				Application app2 = (Application)obj2;
				if (sortColumnName == null) {
					return 0;
				}
				if(sortColumnName.equals(assignmentTitleColumnName)){
					System.out.println("Ascending: " + ascending);
					System.out.println(app1.getAssignment().getTitle() + " vs. " + app2.getAssignment().getTitle());
					System.out.println(app1.getAssignment().getTitle().compareTo(app2.getAssignment().getTitle()));
					return ascending ? 
							app1.getAssignment().getTitle().compareTo(app2.getAssignment().getTitle()) :
								app2.getAssignment().getTitle().compareTo(app1.getAssignment().getTitle());
				} else return 0;
			}
		};
		Arrays.sort(getApplicationArray(), comparator);

		for (int i = 0; i < applicationArray.length; i++) {
			System.out.println(i+": "+applicationArray[i].getAssignment().getTitle());
		}
	}

	public Object[] getApplicationArray() {
		if (!oldSort.equals(sortColumnName) ||
				oldAscending != ascending){
			oldSort = sortColumnName;
			oldAscending = ascending;
			sort();
		}
		if(applicationArray == null){
			List<Application> applicationList = employeeService.getApplicationList();
			if(applicationList != null){							
				applicationArray = new Application[applicationList.size()];
				applicationArray = applicationList.toArray(applicationArray);
			}
		}

		return applicationArray;
	}

	public String getAssignmentTitleColumnName() {
		return assignmentTitleColumnName;
	}

	public String getStudentColumnName() {
		return studentColumnName;
	}

	public String getPriorityNumberColumnName() {
		return priorityNumberColumnName;
	}

	public String getFacultySupervisorColumnName() {
		return facultySupervisorColumnName;
	}

	public String getSortColumnName() {
		return sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		oldSort = this.sortColumnName;
		this.sortColumnName = sortColumnName;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		System.out.println("ascending = " + ascending);
		oldAscending = this.ascending;
		this.ascending = ascending;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
