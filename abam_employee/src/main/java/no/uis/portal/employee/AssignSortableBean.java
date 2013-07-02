package no.uis.portal.employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisStatus;
import no.uis.abam.dom.ThesisStatusType;

import com.icesoft.faces.context.DisposableBean;

/**
 * @author Stig Rune Malterud
 * @author Bente Cecilie Andorsen
 *
 */
public class AssignSortableBean implements DisposableBean{

	private static final String assignmentTitleColumnName = "Assignment Title";
	private static final String studentColumnName = "Student";
	private static final String priorityNumberColumnName = "Priority";
	private static final String facultySupervisorColumnName = "Faculty Supervisor";
	private static final String studyProgramColumnName = "Study program";
	
	private static final Date FROM_DATE_MASTER_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.FEBRUARY, 1).getTime();
	private static final Date FROM_DATE_BACHELOR_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.JANUARY, 15).getTime();
	private static final Date TO_DATE_MASTER_DEFAULT = new GregorianCalendar(
			GregorianCalendar.getInstance().get(Calendar.YEAR)+1, Calendar.JUNE, 15).getTime();
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
	private String selectedPriority;
	private String departmentName;
	
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

	// TODO
	private void sort() {
		Comparator<Object> comparator = new Comparator<Object>(){
			public int compare(Object obj1, Object obj2) {
				ApplicationInformation app1 = (ApplicationInformation)obj1;
				ApplicationInformation app2 = (ApplicationInformation)obj2;
				if (sortColumnName == null) {
					return 0;
				}
				if(sortColumnName.equals(assignmentTitleColumnName)){
					String assignmentTitle1 = app1.getAssignmentTitle();
					String assignmentTitle2 = app2.getAssignmentTitle();
					return ascending ? 
							assignmentTitle1.compareTo(assignmentTitle2) :
							assignmentTitle2.compareTo(assignmentTitle1);
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
				} else if(sortColumnName.equals(studyProgramColumnName)){
					String studyProgram1 = app1.getStudyProgramName();
					String studyProgram2 = app2.getStudyProgramName();
					return ascending ? 
							studyProgram1.compareTo(studyProgram2) :
								studyProgram2.compareTo(studyProgram1);
				} else return 0;
			}
		};
		Object[] aiArray = getApplicationInformationAsArray();
    if(aiArray != null) {
			Arrays.sort(aiArray, comparator);
		}
	}

	
	/**
	 * @return an array of sorted ApplicationInformation objects
	 */
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
		for (int i = 0; i < applicationInformationArray.length; i++) {
			applicationInformationArray[i] = getApplicationInformationWithFieldsSetFromApplication(applicationList.get(i));			
		}
	}
	
	private ApplicationInformation getApplicationInformationWithFieldsSetFromApplication(
			Application application) {
		ApplicationInformation appInfo = new ApplicationInformation();
		
		appInfo.setApplication(application);
		appInfo.setAssignmentTitle(application.getAssignment().getTitle());
		appInfo.setCoStudent1Name(application.getCoStudentName1());
		appInfo.setCoStudent2Name(application.getCoStudentName2());
		appInfo.setFacultySupervisor(application.getAssignment()
				.getFacultySupervisor().getName());
		appInfo.setPriority(application.getPriority());
		
		Student student = employeeService.getStudentFromStudentNumber(
				application.getApplicantStudentNumber()); 
		appInfo.setStudentName(student.getName());
		appInfo.setStudyProgramName(student.getStudyProgramName());
		
		return appInfo;
	}

	
	/**
	 * ActionListener that prepares the array with ApplicationInformation objects
	 * 
	 * @param event
	 */
	public void actionPrepareApplicationInformationArray(ActionEvent event) {
		setSelectedPriority("all");
		setDepartmentName(employeeService.getDepartmentNameFromCode(employeeService.getSelectedDepartmentCode()));
		List<Application> applicationList;
		if(isBachelor()){ 			
			applicationList = employeeService.getBachelorApplicationListFromSelectedDepartmentNumber();			
		}
		else applicationList = employeeService.getMasterApplicationList();
		if(applicationList != null){							
			applicationInformationArray = new ApplicationInformation[applicationList.size()];
			fillApplicationInformationArray(applicationList);
		} else {
			applicationInformationArray = null;
		}
	}
	
	
	/**
	 * ValueChangeListener that updates the array with ApplicationInformation objects based on priority 
	 * 
	 * @param event
	 */
	public void actionPriorityChange(ValueChangeEvent event) {
		List<Application> applicationList;
		List<Application> applicationPriorityList = new ArrayList<Application>();
 		if(isBachelor()) applicationList = employeeService.getBachelorApplicationListFromSelectedDepartmentNumber();
		else applicationList = employeeService.getMasterApplicationList();
		int priority = convertSelectedPriority(event.getNewValue().toString());
		if(applicationList != null){
	 		if(priority != 0) {
				for (Application application : applicationList) {
					if(application.getPriority() == priority) applicationPriorityList.add(application);
				}
				applicationInformationArray = new ApplicationInformation[applicationPriorityList.size()];
				fillApplicationInformationArray(applicationPriorityList);
	 		}
	 		else {
	 			applicationInformationArray = new ApplicationInformation[applicationList.size()];
	 			fillApplicationInformationArray(applicationList);
	 		}
		} else {
			applicationInformationArray = null;
		}
	}
	
	
	/**
	 * ActionListener that makes sure Departments are gotten from webservice when entering setDates.jspxs
	 * 
	 * @param event
	 */
	public void actionPrepareSetDates(ActionEvent event) {
	}
	
	private int convertSelectedPriority(String priority) {
		if (priority.equals("one")) {
		  return 1;
		} else if(priority.equals("two")) {
		  return 2;
		} else if(priority.equals("three")) {
		  return 3;
		} else {
		  return 0;
		}
	}
	
	
	/**
	 * ActionListener that gets the selected ApplicationInformation objects and puts it into a List
	 * 
	 * @param event
	 */
	public void actionGetSelectedRows(ActionEvent event) {
		selectedApplicationInformationList.clear();
		if (applicationInformationArray != null) {
			for (int i = 0; i < applicationInformationArray.length; i++) {
				ApplicationInformation ai = applicationInformationArray[i];
				if(ai.isSelected()) {
				  selectedApplicationInformationList.add(ai);
				}
			}
		}
	}
	
	
	/**
	 * ActionListener that creates Thesis objects from the selected Applications and saves the Thesis objects to the webservice
	 * 
	 * @param event
	 */
	public void actionSaveAssignedApplications(ActionEvent event) {
	  Map<String, Thesis> thesisSave = new HashMap<String, Thesis>();
		for (ApplicationInformation appInfo : selectedApplicationInformationList) {
			Thesis thesis = thesisSave.get(appInfo.getAssignmentTitle());
			if (thesis == null) {
			  thesisSave.put(appInfo.getAssignmentTitle(), getThesisWithFieldsSetFromApplicationInformation(appInfo));
			} else {
			  thesis.addStudentNumber(appInfo.getApplication().getApplicantStudentNumber());
			}
		}
		List<Thesis> thesisList = new ArrayList<Thesis>(thesisSave.values().size());
		thesisList.addAll(thesisSave.values());
		employeeService.addThesesFromList(thesisList);
	}

	private Thesis getThesisWithFieldsSetFromApplicationInformation(ApplicationInformation appInfo) {

	  
	  Thesis thesis = new Thesis();

		thesis.setAssignment(appInfo.getApplication().getAssignment());
		Calendar from = Calendar.getInstance();
		Calendar to = (Calendar)from.clone();
		from.setTime(fromDate);
		to.setTime(toDate);
		thesis.setAcceptionDeadline(from);
		thesis.setSubmissionDeadline(to);
		thesis.addStudentNumber(appInfo.getApplication().getApplicantStudentNumber());
		thesis.setFacultySupervisor(employeeService.getEmployeeFromName(appInfo.getFacultySupervisor()));
		thesis.getStatusList().add(new ThesisStatus(ThesisStatusType.ASSIGNED_TO_STUDENT, employeeService.getLoggedInEmployee().getName()));
		return thesis;
	}
	
	/**
	 * ActionListener that toggles the calendar in the setDates.jspx
	 * 
	 * @param event
	 */
	public void actionToggleCalendar(ActionEvent event) {
		Object compId = event.getComponent().getId();
		if (compId.equals("fromDate")) {
			showFromDateCalendar = !showFromDateCalendar;
		} else if (compId.equals("toDate")) {
			showToDateCalendar = !showToDateCalendar;
		}
		
	}
	
	/**
	 * ValueChangeListener that updates the Date with the selected Date from a calendar 
	 * 
	 * @param event
	 */
	public void actionUpdateDate(ValueChangeEvent event) {
		String componentId = event.getComponent().getId();
		if(componentId.equals("fromDateInput")) {
			setFromDate((Date)event.getNewValue());
			setShowFromDateCalendar(false);
		} else if (componentId.equals("toDateInput")) {
			setToDate((Date)event.getNewValue());
			setShowToDateCalendar(false);
		}
	}
	
	
	/**
	 * ValueChangeEvent that updates the default Dates used in assignSetDates.jspx 
	 * 
	 * @param event
	 */
	public void actionUpdateDefaultDates(ValueChangeEvent event) {
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
	
	public String getStudyProgramColumnName() {
		return studyProgramColumnName;
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
	
	public String getSelectedPriority() {
		return selectedPriority;
	}

	public void setSelectedPriority(String selectedPriority) {
		this.selectedPriority = selectedPriority;
	}

	public String getTypeAsString() {
		if(bachelor) return "Bachelor";
		return "Master";
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public void dispose() throws Exception {
	
	}
	
}
