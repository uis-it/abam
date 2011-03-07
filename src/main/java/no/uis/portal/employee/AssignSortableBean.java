package no.uis.portal.employee;

import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.*;

public class AssignSortableBean {

	private static final String assignmentTitleColumnName = "Assignment Title";
	private static final String studentColumnName = "Student";
	private static final String priorityNumberColumnName = "Priority";
	private static final String facultySupervisorColumnName = "Faculty Supervisor";
	private static final Date FROM_DATE_MASTER_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.FEBRUARY, 1).getTime();
	private static final Date FROM_DATE_BACHELOR_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.JANUARY, 15).getTime();
	private static final Date TO_DATE_MASTER_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.JULY, 1).getTime();
	private static final Date TO_DATE_BACHELOR_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.MAY, 15).getTime();
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private String sortColumnName;
	private boolean ascending;

	private String oldSort;
	private boolean oldAscending;

	private boolean bachelor;
	private boolean showFromDateCalendar;
	private boolean showToDateCalendar;
	
	private Date fromDate;
	private Date toDate;
	
	private String fromDateString;
	private String toDateString;
	
	private EmployeeService employeeService;

	private ApplicationInformation[] applicationInformationArray;
	private List<ApplicationInformation> selectedApplicationInformationList = new ArrayList<ApplicationInformation>();
	
	public AssignSortableBean() {
		sortColumnName = assignmentTitleColumnName;
		ascending = true;

		oldSort = sortColumnName;
		oldAscending = !ascending;
		bachelor = true;
		
		setFromDate(FROM_DATE_BACHELOR_DEFAULT);
		setToDate(TO_DATE_BACHELOR_DEFAULT);
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
		List<Application> applicationList;
		if(isBachelor()) applicationList = employeeService.getBachelorApplicationList();
		else applicationList = employeeService.getMasterApplicationList();
		if(applicationList != null){							
			applicationInformationArray = new ApplicationInformation[applicationList.size()];
			fillApplicationInformationArray(applicationList);
		}
	}
	
	public void actionGetSelectedRows(ActionEvent event) {
		selectedApplicationInformationList.clear();
		for (int i = 0; i < applicationInformationArray.length; i++) {
			ApplicationInformation ai = applicationInformationArray[i];
			if(ai.isSelected()) selectedApplicationInformationList.add(ai);
		}
	}
	
	public void actionSaveAssignedApplications(ActionEvent event) {
		
	}
	
	public void actionToggleCalendar(ActionEvent event) {
		Object compId = event.getComponent().getId();
		if (compId.equals("fromDate")) {
			showFromDateCalendar = !showFromDateCalendar;
		} else if (compId.equals("toDate")) {
			showToDateCalendar = !showToDateCalendar;
		}
		
	}
	
	public void actionUpdateDate(ValueChangeEvent event) {
		if(event.getComponent().getId().equals("fromDateInput")) {
			setFromDate((Date)event.getNewValue());
		} else if (event.getComponent().getId().equals("toDateInput")) {
			setToDate((Date)event.getNewValue());
		}
	}
	
	public void radioListener(ValueChangeEvent event) {
		bachelor = (Boolean) event.getNewValue();
		if(bachelor) {
			setToDate(TO_DATE_BACHELOR_DEFAULT);
			setFromDate(FROM_DATE_BACHELOR_DEFAULT);
		} else {
			setToDate(TO_DATE_MASTER_DEFAULT);
			setFromDate(FROM_DATE_MASTER_DEFAULT);
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

	public List<ApplicationInformation> getSelectedApplicationInformationList() {
		return selectedApplicationInformationList;
	}

	public void setSelectedApplicationInformationList(
			List<ApplicationInformation> selectedApplicationInformationList) {
		this.selectedApplicationInformationList = selectedApplicationInformationList;
	}

	public boolean isBachelor() {
		return bachelor;
	}

	public void setBachelor(boolean bachelor) {
		this.bachelor = bachelor;
	}

	public boolean isShowFromDateCalendar() {
		return showFromDateCalendar;
	}

	public void setShowFromDateCalendar(boolean showFromDateCalendar) {
		this.showFromDateCalendar = showFromDateCalendar;
	}

	public boolean isShowToDateCalendar() {
		return showToDateCalendar;
	}

	public void setShowToDateCalendar(boolean showToDateCalendar) {
		this.showToDateCalendar = showToDateCalendar;
	}

	public Date getFromDate() {
		return fromDate;
	}
	
	public String getFromDateAsString() {
		return fromDate.toString();
	}

	public void setFromDate(Date fromDate) {
		fromDateString = simpleDateFormatter.format(fromDate);
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}
	
	public String getToDateAsString() {
		return toDate.toString();
	}

	public void setToDate(Date toDate) {
		toDateString = simpleDateFormatter.format(toDate);
		this.toDate = toDate;
	}

	public String getFromDateString() {
		return fromDateString;
	}

	public void setFromDateString(String fromDateString) {
		this.fromDateString = fromDateString;
	}

	public String getToDateString() {
		return toDateString;
	}

	public void setToDateString(String toDateString) {
		this.toDateString = toDateString;
	}
	
	public String getTypeAsString() {
		if(bachelor) return "Bachelor";
		return "Master";
	}
	
}
