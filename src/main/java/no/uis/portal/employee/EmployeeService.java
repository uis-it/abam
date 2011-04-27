package no.uis.portal.employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
import com.liferay.portal.model.Permission;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.PermissionLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.StudyProgram;
import no.uis.abam.dom.Thesis;
import no.uis.abam.util.NumberValidator;
import no.uis.abam.ws_abam.AbamWebService;

public class EmployeeService {
	
	public static final String COLUMN_UIS_LOGIN_NAME = "UiS-login-name";
	private static final String LANGUAGE = "language";
	private static final String NORWEGIAN_LANGUAGE = "Norsk";

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

	public void saveAssignment(Assignment assignment) {
		abamClient.saveAssignment(assignment);
	}

	public void actionPrepareDisplayAssignments(ActionEvent event) {		
		setLoggedInEmployee(getEmployeeFromUisLoginName());
		getActiveAssignmentsSet();
		setSelectedStudyProgramNumber(0);
		setSelectedDepartmentNumber(0);
		getDepartmentListFromWebService();
		checkIfLoggedInUserIsAuthor();
		
	}
	
	private void checkIfLoggedInUserIsAuthor() {
		if(assignmentSet != null) {
			for (Assignment assignment : assignmentSet) {
				if (assignment.getAuthor().getName().equals(loggedInEmployee.getName())) {
					assignment.setLoggedInUserIsAuthor(true);
				} else {
					assignment.setLoggedInUserIsAuthor(false);
				}
			}
		}
	}

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

	public void setSelectedDepartmentAndStudyProgramFromValue(int value) {
		setSelectedDepartmentNumber(value);
		Department selectedDepartment = getDepartmentFromValue(selectedDepartmentNumber);
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
	
	public void actionUpdateStudyProgramListFromCreateAssignment(
			ValueChangeEvent event) {
		selectedDepartmentNumber = Integer.parseInt(event.getNewValue()
				.toString());
		getStudyProgramListFromSelectedDepartment();		

	}

	private void getStudyProgramListFromSelectedDepartment() {
		selectedStudyProgramList = getDepartmentFromValue(
				selectedDepartmentNumber).getStudyPrograms();
		studyProgramSelectItemList.clear();
		for (int i = 0; i < selectedStudyProgramList.size(); i++) {
			studyProgramSelectItemList.add(new SelectItem(i,selectedStudyProgramList.get(i).getName()));
		}
		
	}

	public void actionSetDisplayAssignment(ValueChangeEvent event) {

		if (event.getNewValue() == null) {
			selectedStudyProgramNumber = 0;
		} else {
			selectedStudyProgramNumber = Integer.parseInt(event.getNewValue()
					.toString());
		}
		setDisplayAssignments();
	}
	
	public void setDisplayAssignments() {
		String selectedStudyProgram = getStudyProgramNameFromValue(selectedStudyProgramNumber);
		
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

	public void setAllEditExternalExaminerToFalse() {
		// TODO Only get logged in teacher's students theses
		for (Thesis thesis : abamClient.getThesisList()) {
			thesis.setEditExternalExaminer(false);
		}
	}

	public void addThesesFromList(List<Thesis> thesesToAdd) {
		abamClient.addThesesFromList(thesesToAdd);
	}

	public void updateThesis(Thesis thesisToUpdate) {
		abamClient.updateThesis(thesisToUpdate);
	}

	public void removeDepartment(Department department) {
		departmentList.remove(department);
	}

	public void saveDepartmentListToWebService() {
		abamClient.setDepartmentList(departmentList);
	}

	public String getDepartmentNameFromIndex(int index) {
		if(res.getString(LANGUAGE).equals(NORWEGIAN_LANGUAGE)) {
			return departmentList.get(index).getOeNavn_Bokmaal();
		}
		return departmentList.get(index).getOeNavn_Engelsk();
	}
	
	public String getDepartmentCodeFromIndex(int index) {		
		return departmentList.get(index).getOeKode();

	}

	//TODO Må kanskje endre litt på denne metoden.
	public Department getDepartmentFromValue(int value) {
		getDepartmentListFromWebService();
		for (Department department : departmentList) {
			if (departmentList.indexOf(department) == value) {
				return department;
			}
		}
		return null;
	}

	public String getStudyProgramNameFromValue(int value) {
		for (StudyProgram studyProgram : selectedStudyProgramList) {
			if (selectedStudyProgramList.indexOf(studyProgram) == value) {
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

	public void setSelectedStudyProgramListFromDepartmentNumber(
			int departmentNumber) {
		setSelectedStudyProgramList(getDepartmentFromValue(departmentNumber)
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

	public void setSelectedStudyProgramList(List<StudyProgram> list) {
		this.selectedStudyProgramList = list;
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
	
	public PermissionChecker getPermissionChecker() {
		return themeDisplay.getPermissionChecker(); 
	}

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
	
	public boolean isAdministrativeEmployee() {
		User user = themeDisplay.getUser();
		for (Role role : user.getRoles()) {
			if(role.getName().equalsIgnoreCase("Abam Administrative Employee")) {
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
	
	public String getUserCustomAttribute(User user, String columnName) throws PortalException, SystemException {
		//workaround for Liferay bug LPS-2568, by James Falkner.
		int excount = ExpandoValueLocalServiceUtil.getExpandoValuesCount();
		List<ExpandoValue> vals = ExpandoValueLocalServiceUtil.getExpandoValues(0, excount-1);
		Map <String, Serializable> attrs = user.getExpandoBridge().getAttributes();
		String data = (String)attrs.get(columnName);
		return data;
	}

//	public Employee getLoggedInEmployee() {
//		return loggedInEmployee;
//	}

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
	
	public List<Thesis> getArchivedThesisListFromUisLoginName() {
		String uisLoginName;
		try {
			uisLoginName = getUserCustomAttribute(getThemeDisplay().getUser(), COLUMN_UIS_LOGIN_NAME);
			return abamClient.getArchivedThesisListFromUisLoginName(uisLoginName);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return null;
	}
}
