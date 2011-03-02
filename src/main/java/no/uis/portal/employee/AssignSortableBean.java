package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.*;

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

	private ApplicationInformation[] applicationInformationArray;

	public AssignSortableBean() {
		sortColumnName = assignmentTitleColumnName;
		ascending = true;

		oldSort = sortColumnName;
		oldAscending = !ascending;
	}

	public void sort() {
		Comparator comparator = new Comparator(){
			public int compare(Object obj1, Object obj2) {
				ApplicationInformation app1 = (ApplicationInformation)obj1;
				ApplicationInformation app2 = (ApplicationInformation)obj2;
				if (sortColumnName == null) {
					return 0;
				}
				if(sortColumnName.equals(assignmentTitleColumnName)){
					return ascending ? 
							app1.getAssignmentTitle().compareTo(app2.getAssignmentTitle()) :
								app2.getAssignmentTitle().compareTo(app1.getAssignmentTitle());
				} else if(sortColumnName.equals(studentColumnName)){
					String studentName1 = app1.getStudentName();
					String studentName2 = app2.getStudentName();
					return ascending ? 
							studentName1.compareTo(studentName2) :
							studentName2.compareTo(studentName1);
				} else if(sortColumnName.equals(priorityNumberColumnName)){
					Integer priority1 = app1.getPriority();
					Integer priority2 = app2.getPriority();
					return ascending ? 
							priority1.compareTo(priority2) :
							priority2.compareTo(priority1);
				} else if(sortColumnName.equals(facultySupervisorColumnName)){
					String facultySuperVisor1 = app1.getFacultySupervisor();
					String facultySuperVisor2 = app2.getFacultySupervisor();
					return ascending ? 
							facultySuperVisor1.compareTo(facultySuperVisor2) :
								facultySuperVisor2.compareTo(facultySuperVisor1);
				} else return 0;
			}
		};
		
		Arrays.sort(getApplicationInformationAsArray(), comparator);
	}

	public Object[] getApplicationInformationAsArray() {
		if (!oldSort.equals(sortColumnName) ||
				oldAscending != ascending){
			oldSort = sortColumnName;
			oldAscending = ascending;
			sort();
		}

		return applicationInformationArray;
	}
	
	private void fillApplicationInformationArray(List<Application> applicationList) {
		ApplicationInformation applicationInformation = null;
		Application application = null;
		for (int i = 0; i < applicationInformationArray.length; i++) {
			application = applicationList.get(i);
			applicationInformation = new ApplicationInformation();
			applicationInformation.setApplication(application);
			applicationInformation.setAssigned(application.isAssigned());
			applicationInformation.setAssignmentTitle(application.getAssignment().getTitle());
			applicationInformation.setCoStudent1Name(application.getCoStudentName1());
			applicationInformation.setCoStudent2Name(application.getCoStudentName2());
			applicationInformation.setFacultySupervisor(application.getAssignment().getFacultySupervisor());
			applicationInformation.setPriority(application.getPriority());
			applicationInformation.setStudentName(
					employeeService.getStudentFromStudentNumber(
							application.getApplicantStudentNumber()).getName());
			applicationInformationArray[i] = applicationInformation;
		}
	}

	public void actionRefreshApplicationInformationArray(ActionEvent event) {
		List<Application> applicationList = employeeService.getApplicationList();
		if(applicationList != null){							
			applicationInformationArray = new ApplicationInformation[applicationList.size()];
			fillApplicationInformationArray(applicationList);
		}
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
		oldAscending = this.ascending;
		this.ascending = ascending;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
