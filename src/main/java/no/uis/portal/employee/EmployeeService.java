package no.uis.portal.employee;

import java.io.Serializable;
import java.util.*;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.RenderRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.*;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.PermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

import no.uis.abam.dom.*;
import no.uis.abam.ws_abam.AbamWebService;

public class EmployeeService {
	
	public static final String COLUMN_UIS_LOGIN_NAME = "UiS-login-name";
	private static final String LANGUAGE = "language";
	private static final String NORWEGIAN_LANGUAGE = "Norsk";
	private static final String SCIENTIFIC_EMPLOYEE_GROUP_NAME_FROM_LDAP = "alle-vit";

	private Logger log = Logger.getLogger(EmployeeService.class);
	
	private int selectedDepartmentNumber;
	private int selectedStudyProgramNumber;

	private String selectedDepartmentCode;

	private AbamWebService abamClient;

	private List<Department> departmentList;
	private List<StudyProgram> selectedStudyProgramList = new ArrayList<StudyProgram>();

	private List<SelectItem> departmentSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> studyProgramSelectItemList = new ArrayList<SelectItem>();
	
	private HtmlSelectOneMenu studyProgramMenu;
	
	private Set<Assignment> assignmentSet;
	private Set<Assignment> displayAssignmentSet;
	
	private FacesContext context;
	private Locale locale;
	private ResourceBundle res;
	
	private Employee loggedInEmployee;
	private ThemeDisplay themeDisplay;
	
	
	public EmployeeService() {	
		context  = FacesContext.getCurrentInstance();
		locale = context.getViewRoot().getLocale();
		res = ResourceBundle.getBundle("Language", locale);	
		initializeThemeDisplay();		
	}
	
	private void initializeThemeDisplay() {
		if (themeDisplay == null) {			
			RenderRequest renderRequest = (RenderRequest) (context
					.getExternalContext().getRequest());
			themeDisplay = (ThemeDisplay) renderRequest
			.getAttribute(WebKeys.THEME_DISPLAY);

		}
	}

	
	/**
	 * @param assignment that should be saved
	 */
	public void saveAssignment(Assignment assignment) {
		abamClient.saveAssignment(assignment);
	}

	
	/**
	 * ActionListener that prepares displayAssignments.jspx
	 * 
	 * @param event
	 */
	public void actionPrepareDisplayAssignments(ActionEvent event) {		
		setLoggedInEmployee(getEmployeeFromUisLoginName());
		getActiveAssignmentsSet();
		setSelectedStudyProgramNumber(0);
		setSelectedDepartmentNumber(0);
		getDepartmentListFromWebService();
		checkIfLoggedInUserIsAuthor();
		addRoleToEmployee();
	}
	
	private void checkIfLoggedInUserIsAuthor() {
		if(assignmentSet != null) {
			for (Assignment assignment : assignmentSet) {
				if (assignment.getAuthor().getName() !=  null && assignment.getAuthor().getName().equals(loggedInEmployee.getName())) {
					assignment.setLoggedInUserIsAuthor(true);
				} else {
					assignment.setLoggedInUserIsAuthor(false);
				}
			}
		}
	}

	/**
	 * ValueChangeListener that updates the StudyProgram List and Assignment Set based on the selected Department
	 * @param event
	 */
	public void actionUpdateStudyProgramList(ValueChangeEvent event) {
		setSelectedDepartmentAndStudyProgramFromValue(Integer.parseInt(event.getNewValue().toString()));
		if(studyProgramMenu != null) studyProgramMenu.setValue(getSelectedStudyProgramNumber());
		displayAssignmentSet.clear();
		if(assignmentSet != null) {
			for (Assignment assignment : assignmentSet) {
				if (assignment.getDepartmentCode().equals(selectedDepartmentCode)
					|| selectedDepartmentCode.equals("")) {
					displayAssignmentSet.add(assignment);
					String depName = getDepartmentNameFromCode(assignment.getDepartmentCode());
					assignment.setDepartmentName(depName);
				}
			}
		}
	}

	private void setSelectedDepartmentAndStudyProgramFromValue(int value) {
		setSelectedDepartmentNumber(value);
		Department selectedDepartment = getDepartmentFromIndex(selectedDepartmentNumber);
		setSelectedDepartmentCode(selectedDepartment.getOeKode());
		
		setSelectedStudyProgramNumber(0);
		setSelectedStudyProgramList(selectedDepartment.getStudyPrograms());		

	}
	
	private String getDepartmentNameFromCode(String code) {
		String language = res.getString(LANGUAGE);
		for (Department dep : departmentList) {
			if (dep.getOeKode() != null && dep.getOeKode().equals(code)) {
				if (language.equals(NORWEGIAN_LANGUAGE)) {
					return dep.getOeNavn_Bokmaal();
				} else {
					return dep.getOeNavn_Engelsk();
				}
			}
		}
		return "";
	}
	
	
	/**
	 * ValueChangeListener that updates the StudyProgram List from createAssignment.jspx
	 * @param event
	 */
	public void actionUpdateStudyProgramListFromCreateAssignment(
			ValueChangeEvent event) {
		selectedDepartmentNumber = Integer.parseInt(event.getNewValue()
				.toString());
		getStudyProgramListFromSelectedDepartment();		

	}

	private void getStudyProgramListFromSelectedDepartment() {
		selectedStudyProgramList = getDepartmentFromIndex(
				selectedDepartmentNumber).getStudyPrograms();
		studyProgramSelectItemList.clear();
		for (int i = 0; i < selectedStudyProgramList.size(); i++) {
			studyProgramSelectItemList.add(new SelectItem(i,selectedStudyProgramList.get(i).getName()));
		}
		
	}

	
	/**
	 * ValueChangeListener that updates the Set with Assignments based on the selected StudyProgram
	 * 
	 * @param event
	 */
	public void actionSetDisplayAssignment(ValueChangeEvent event) {

		if (event.getNewValue() == null) {
			selectedStudyProgramNumber = 0;
		} else {
			selectedStudyProgramNumber = Integer.parseInt(event.getNewValue()
					.toString());
		}
		setDisplayAssignments();
	}
	
	
	/**
	 * Updates the Set with DisplayAssignments based on selected StudyProgram 
	 */
	public void setDisplayAssignments() {
		String selectedStudyProgram = getStudyProgramNameFromIndex(selectedStudyProgramNumber);
		
		displayAssignmentSet.clear();
		
		if (selectedDepartmentCode == null)
			
			setSelectedDepartmentCode("");
		if (selectedStudyProgram == null)
			selectedStudyProgram = "";
		if (assignmentSet != null) {
			for (Assignment assignment : assignmentSet) {
				if (assignmentShouldBeDisplayed(assignment,
						selectedStudyProgram)) {
					displayAssignmentSet.add(assignment);
				}
			}
		}		
	}
		
	private boolean assignmentShouldBeDisplayed(Assignment assignmentIn,
			String selectedStudyProgram) {
		return (selectedStudyProgram.equals("") && assignmentIn
				.getDepartmentCode().equals(selectedDepartmentCode))
				|| assignmentIn.getStudyProgramName().equals(
						selectedStudyProgram)
				|| getSelectedDepartmentCode().equals("");
	}

	public void addThesesFromList(List<Thesis> thesesToAdd) {
		abamClient.addThesesFromList(thesesToAdd);
	}

	public void updateThesis(Thesis thesisToUpdate) {
		abamClient.updateThesis(thesisToUpdate);
	}

	
	/**
	 * @param index of Department to get the name for
	 * @return name of the Department
	 */
	public String getDepartmentNameFromIndex(int index) {
		if(res.getString(LANGUAGE).equals(NORWEGIAN_LANGUAGE)) {
			return departmentList.get(index).getOeNavn_Bokmaal();
		}
		return departmentList.get(index).getOeNavn_Engelsk();
	}
	
	/**
	 * @param index of Department to get the code for
	 * @return code of the Department
	 */
	public String getDepartmentCodeFromIndex(int index) {		
		return departmentList.get(index).getOeKode();
	}

	
	/**
	 * @param index of Department to get
	 * @return Department object, or null if not found
	 */
	public Department getDepartmentFromIndex(int index) {
		getDepartmentListFromWebService();
		for (Department department : departmentList) {
			if (departmentList.indexOf(department) == index) {
				return department;
			}
		}
		return null;
	}

	/**
	 * @param index of StudyProgram to get
	 * @return name of StudyProgram, or null if not found
	 */
	public String getStudyProgramNameFromIndex(int index) {
		for (StudyProgram studyProgram : selectedStudyProgramList) {
			if (selectedStudyProgramList.indexOf(studyProgram) == index) {
				return studyProgram.getName();
			}
		}
		return null;
	}

	public Student getStudentFromStudentNumber(String studentNumber) {
		return abamClient.getStudentFromStudentNumber(studentNumber);
	}

	public void removeAssignment(Assignment assignment) {
		abamClient.removeAssignment(assignment);
		getActiveAssignmentsSet();
	}

	public void removeApplication(Application application) {
		abamClient.removeApplication(application);
	}

	public String getSelectedStudyProgramNameFromIndex(int index) {
		return selectedStudyProgramList.get(index).getName();
	}

	public void setSelectedStudyProgramListFromDepartmentIndex(
			int departmentIndex) {
		setSelectedStudyProgramList(getDepartmentFromIndex(departmentIndex)
				.getStudyPrograms());
	}

	public List<Application> getMasterApplicationList() {
		return abamClient.getMasterApplicationListFromDepartmentCode(getDepartmentCodeFromIndex(selectedDepartmentNumber));
	}

	public List<Application> getBachelorApplicationListFromSelectedDepartmentNumber() {
		return abamClient.getBachelorApplicationListFromDepartmentCode(getDepartmentCodeFromIndex(selectedDepartmentNumber));
	}

	public Assignment getAssignmentFromId(int id) {
		return abamClient.getAssignmentFromId(id);
	}

	
	/**
	 * Gets the Departments from the webservice, and sets the name based on selected language 
	 */
	public void getDepartmentListFromWebService() {
		departmentList = abamClient.getDepartmentList();
		departmentSelectItemList.clear();
  		for (int i = 0; i < departmentList.size(); i++) {
			if(res.getString("language").equals("Norsk")) {
				departmentSelectItemList.add(new SelectItem(i,departmentList.get(i).getOeNavn_Bokmaal()));
			} else {
				departmentSelectItemList.add(new SelectItem(i,departmentList.get(i).getOeNavn_Engelsk()));
			}
		}
	}

	public int getSelectedDepartmentNumber() {
		return selectedDepartmentNumber;
	}

	public void setSelectedDepartmentNumber(int selectedDepartmentNumber) {
		this.selectedDepartmentNumber = selectedDepartmentNumber;
	}

	public int getSelectedStudyProgramNumber() {
		return selectedStudyProgramNumber;
	}

	public void setSelectedStudyProgramNumber(int selectedStudyProgramNumber) {
		this.selectedStudyProgramNumber = selectedStudyProgramNumber;
	}

	public int getNextId() {
		return abamClient.getNextId();
	}

	public String getSelectedDepartmentCode() {
		return selectedDepartmentCode;
	}

	public void setSelectedDepartmentCode(String selectedDepartmentCode) {
		this.selectedDepartmentCode = selectedDepartmentCode;
	}

	public List<StudyProgram> getSelectedStudyProgramList() {
		return selectedStudyProgramList;
	}

	
	/**
	 * @param list of StudyPrograms to set
	 */
	public void setSelectedStudyProgramList(List<StudyProgram> list) {
		this.selectedStudyProgramList = list;
		updateStudyProgramSelectItemList();
	}
	
	private void updateStudyProgramSelectItemList() {
		studyProgramSelectItemList.clear();
		for (int i = 0; i < selectedStudyProgramList.size(); i++) {
			studyProgramSelectItemList.add(new SelectItem(i,selectedStudyProgramList.get(i).getName()));
		}
	}

	public List<Thesis> getThesisList() {
		return abamClient.getThesisList();
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public Set<Assignment> getAssignmentSet() {
		return assignmentSet;
	}

	public void setAssignmentSet(Set<Assignment> assignmentSet) {
		this.assignmentSet = assignmentSet;
	}

	public Set<Assignment> getAllAssignmentsSet() {
		assignmentSet = abamClient.getAllAssignments();
		return assignmentSet;
	}
	
	public HtmlSelectOneMenu getStudyProgramMenu() {
		return studyProgramMenu;
	}

	public void setStudyProgramMenu(HtmlSelectOneMenu studyProgramMenu) {
		this.studyProgramMenu = studyProgramMenu;
	}

	
	/**
	 * @return a Set containing all active Assignments
	 */
	public Set<Assignment> getActiveAssignmentsSet() {
		assignmentSet = abamClient.getActiveAssignments();
		displayAssignmentSet = new TreeSet<Assignment>();
		if(assignmentSet != null) {
			displayAssignmentSet.addAll(assignmentSet);
		}
		return assignmentSet;
	}

	public void setAbamClient(AbamWebService abamClient) {
		this.abamClient = abamClient;
	}
	
	
	/**
	 * @param permissionName 
	 * @return true if user has permission, false if not
	 */
	public boolean checkPermission(String permissionName) {
		User user = themeDisplay.getUser();
		List<Permission> permList = new LinkedList<Permission>();
		
		for (Role role : user.getRoles()) {
			try {
				permList.addAll(PermissionLocalServiceUtil.getRolePermissions(role.getRoleId()));
			} catch (SystemException e) {				
				log.debug(e.getStackTrace());
			}			
		}

		for (Permission permission : permList) {
			if (permission.getActionId().equals(permissionName)) {
				return true;
			}			
		}
		
		return false;				
	}
	
//	public PermissionChecker getPermissionChecker() {
//		return themeDisplay.getPermissionChecker(); 
//	}

	public boolean isShouldDisplayAssignAssignments() {
		return checkPermission("ASSIGN_ASSIGNMENTS");						
	}
	
	public boolean isShouldDisplayCreateAssignments() {
		return checkPermission("CREATE_ASSIGNMENTS");						
	}
	
	public boolean isShouldDisplayEditAssignments() {
		return checkPermission("EDIT_ASSIGNMENTS");						
	}
	
	public boolean isShouldDisplayViewSupervisedThesis() {
		return checkPermission("VIEW_SUPERVISED_THESIS");						
	}
	
	public boolean isShouldDisplayRenewAssignments() {
		return checkPermission("RENEW_ASSIGNMENTS");						
	}

	public boolean isShouldDisplayViewExternalExaminer() {
		return checkPermission("VIEW_EXTERNAL_EXAMINER");						
	}
	
	
	/**
	 * @return true if user is administrative employee, false if not
	 */
	public boolean isAdministrativeEmployee() {
		User user = themeDisplay.getUser();
		for (Role role : user.getRoles()) {
			if(role.getName().equalsIgnoreCase("Abam Administrative Employee")) {
				return true;
			}
		}
		return false;
	}
	
	public void addRoleToEmployee() {
		if (loggedInEmployee != null) {

			if (loggedInEmployeeIsScientificEmployee()) {
				long companyId = themeDisplay.getCompanyId();
				try {

					List<Role> roleList = RoleLocalServiceUtil.getRoles(companyId);
					for (Role role : roleList) {
						if (role.getName().equals("Abam Scientific Employee")) {
							long[] roleId = {role.getRoleId()};							
							RoleLocalServiceUtil.addUserRoles(themeDisplay.getUserId(), roleId); 
						}
					}
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean loggedInEmployeeIsScientificEmployee() {
		for (String groupName : loggedInEmployee.getGroupMembership()) {
			if (groupName.contains(SCIENTIFIC_EMPLOYEE_GROUP_NAME_FROM_LDAP)) {
				return true;
			}
		}
		return false;
	}

	public List<SelectItem> getDepartmentSelectItemList() {
		return departmentSelectItemList;
	}

	public void setDepartmentSelectItemList(
			List<SelectItem> departmentSelectItemList) {
		this.departmentSelectItemList = departmentSelectItemList;
	}

	public List<SelectItem> getStudyProgramSelectItemList() {
		return studyProgramSelectItemList;
	}

	public void setStudyProgramSelectItemList(
			List<SelectItem> studyProgramSelectItemList) {
		this.studyProgramSelectItemList = studyProgramSelectItemList;
	}

	public ThemeDisplay getThemeDisplay() {
		return themeDisplay;
	}
	
	/**
	 * Finds the logged in Employee based on employee id 
	 * @return Employee object if found, null if not found
	 */
	public Employee getEmployeeFromUisLoginName() {
		if (loggedInEmployee != null && !loggedInEmployee.getEmployeeId().isEmpty()) return loggedInEmployee;
		else {
			log.setLevel(Level.ERROR);
			
			String loginName = "";
			try {			
				loginName = getUserCustomAttribute(getThemeDisplay().getUser(), COLUMN_UIS_LOGIN_NAME);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			Employee employee = abamClient.getEmployeeFromUisLoginName(loginName);
			if(employee == null) {
				employee = new Employee();
				employee.setEmployeeId("");
			}
			setLoggedInEmployee(employee);
			return employee;
		}
	}		
	
	private String getUserCustomAttribute(User user, String columnName) throws PortalException, SystemException {
		//workaround for Liferay bug LPS-2568, by James Falkner.
		int excount = ExpandoValueLocalServiceUtil.getExpandoValuesCount();
		List<ExpandoValue> vals = ExpandoValueLocalServiceUtil.getExpandoValues(0, excount-1);
		Map <String, Serializable> attrs = user.getExpandoBridge().getAttributes();
		String data = (String)attrs.get(columnName);
		return data;
	}

	public void setLoggedInEmployee(Employee loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}
	
	public Set<Assignment> getDisplayAssignmentSet() {
		return displayAssignmentSet;
	}

	public Employee getEmployeeFromName(String facultySupervisorName) {		
		return abamClient.getEmployeeFromFullName(facultySupervisorName);
	}
	
	public List<Thesis> getThesisListFromDepartmentCode(String depCode) {
		return abamClient.getThesisListFromDepartmentCode(depCode);
	}
	
	public List<Thesis> getArchivedThesisListFromDepartmentCode(String depCode) {
		return abamClient.getArchivedThesisListFromDepartmentCode(depCode);
	}
		
	/**
	 * @return a List containing the archived Theses for logged in Employee
	 */
	public List<Thesis> getArchivedThesisListFromUisLoginName() {
		Employee employee = getEmployeeFromUisLoginName();		
		return abamClient.getArchivedThesisListFromUisLoginName(employee.getEmployeeId());		
	}
}
