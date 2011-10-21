package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.RenderRequest;

import no.uis.abam.commons.BaseTextUtil;
import no.uis.abam.dom.AbamGroup;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.abam.ws_abam.AbamWebService;
import no.uis.portal.util.LiferayUtil;
import no.uis.service.model.AffiliationType;
import no.uis.service.model.BaseText;
import no.uis.service.model.Organization;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Permission;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

public class EmployeeService implements InitializingBean {
	
	private static final UnknownEmployee UNKNOWN_EMPLOYEE = new UnknownEmployee() {};

  public static final String COLUMN_UIS_LOGIN_NAME = "UiS-login-name";
	private static final String LANGUAGE = "language";
	private static final String NORWEGIAN_LANGUAGE = "Norsk";
	private static final String ABAM_SCIENTIFIC_ROLE_NAME = "Abam Scientific Employee";
	private static final String ABAM_ADMINISTRATIVE_ROLE_NAME = "Abam Administrative Employee";

	private Logger log = Logger.getLogger(EmployeeService.class);
	
	private String selectedDepartmentCode;
	private String selectedStudyProgramCode;
	
	private AbamWebService abamClient;

	private List<Organization> departmentList;
	private List<no.uis.service.model.StudyProgram> selectedStudyProgramList = new ArrayList<no.uis.service.model.StudyProgram>();

	private List<SelectItem> departmentSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> studyProgramSelectItemList = new ArrayList<SelectItem>();
	
	private HtmlSelectOneMenu studyProgramMenu;
	
	private List<Assignment> assignmentSet;
	private Set<Assignment> displayAssignmentSet;
	
	private FacesContext context;
	private Locale locale;
	private ResourceBundle res;
	
	private Employee loggedInEmployee;
	private ThemeDisplay themeDisplay;
	
	
	public EmployeeService() {	
	}
	
  @Override
  public void afterPropertiesSet() throws Exception {
    context  = FacesContext.getCurrentInstance();
    locale = context.getViewRoot().getLocale();
    res = ResourceBundle.getBundle("Language", locale);
    themeDisplay = LiferayUtil.getThemeDisplay(context);

    // we get the loggedInEmployee and make sure it has the correct Liferay roles.
    if (!UNKNOWN_EMPLOYEE.equals(getLoggedInEmployee())) {
      addRoleToEmployee();
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
		getDepartmentListFromWebService();

		if (StringUtils.isBlank(loggedInEmployee.getEmployeeId())) {
	     throw new IllegalArgumentException("employeeId");
		} else  {
      Organization org = abamClient.getEmployeeDeptarment(loggedInEmployee.getEmployeeId());
      setSelectedDepartmentCode(org.getPlaceRef());
      getStudyProgramListFromSelectedDepartment();
      setSelectedStudyProgramCode(this.selectedStudyProgramList.get(0).getId());
  		
      getActiveAssignmentsSet();
  		checkIfLoggedInUserIsAuthor();
		}
	}
	
	public void setSelectedStudyProgramCode(String selectedStudyProgramCode) {
	  this.selectedStudyProgramCode = selectedStudyProgramCode;
  }

  public String getSelectedStudyProgramCode() {
    return selectedStudyProgramCode;
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
		String newDeptCode = event.getNewValue().toString();
		setSelectedDepartmentCode(newDeptCode);
		setSelectedStudyProgramListFromDepartmentCode(newDeptCode);
		if(studyProgramMenu != null) {
		  studyProgramMenu.setValue(getSelectedStudyProgramCode());
		}
		displayAssignmentSet.clear();
		if(assignmentSet != null) {
			for (Assignment assignment : assignmentSet) {
				if (assignment.getDepartmentCode().equals(selectedDepartmentCode)
					|| selectedDepartmentCode.equals("")) {
					displayAssignmentSet.add(assignment);
					assignment.setDepartmentCode(assignment.getDepartmentCode());
				}
			}
		}
	}

	public String getDepartmentNameFromCode(String code) {
	  if (code != null) {
  		for (Organization dep : departmentList) {
  		  if(dep.getPlaceRef().equals(code)) {
  		    return getText(dep.getName());
  		  }
  		}
	  }
		return "";
	}
	
	
	/**
	 * ValueChangeListener that updates the StudyProgram List from createAssignment.jspx
	 * @param event
	 */
	public void actionUpdateStudyProgramListFromCreateAssignment(ValueChangeEvent event) {
		selectedDepartmentCode = event.getNewValue().toString();
		getStudyProgramListFromSelectedDepartment();		

	}

	private void getStudyProgramListFromSelectedDepartment() {
	  List<no.uis.service.model.StudyProgram> progs = abamClient.getStudyProgramsFromDepartmentFSCode(this.selectedDepartmentCode);
		selectedStudyProgramList = progs;
		studyProgramSelectItemList.clear();
		updateStudyProgramSelectItemList();
	}

	
	/**
	 * ValueChangeListener that updates the Set with Assignments based on the selected StudyProgram
	 * 
	 * @param event
	 */
	public void actionSetDisplayAssignment(ValueChangeEvent event) {

		if (event.getNewValue() == null) {
			selectedStudyProgramCode = null;
		} else {
			selectedStudyProgramCode = event.getNewValue().toString();
		}
		setDisplayAssignments();
	}
	
	
	/**
	 * Updates the Set with DisplayAssignments based on selected StudyProgram 
	 */
	public void setDisplayAssignments() {
		String selectedStudyProgram = getStudyProgramNameFromCode(selectedStudyProgramCode);
		
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

	public String getStudyProgramNameFromCode(String programCode) {
    no.uis.service.model.StudyProgram studProg = abamClient.getStudyProgramFromCode(programCode);
    String progName = getText(studProg.getName());
	  return progName;
	}
	
	private boolean assignmentShouldBeDisplayed(Assignment assignmentIn, String selectedStudyProgram) {
	  
	  String assignmentStudProgName = getStudyProgramNameFromCode(assignmentIn.getStudyProgramCode());
	  
		return (selectedStudyProgram.equals("") && assignmentIn
				.getDepartmentCode().equals(selectedDepartmentCode))
				|| assignmentStudProgName.equals(
						selectedStudyProgram)
				|| getSelectedDepartmentCode().equals("");
	}

	public void addThesesFromList(List<Thesis> thesesToAdd) {
		abamClient.addThesesFromList(thesesToAdd);
	}

	public void updateThesis(Thesis thesisToUpdate) {
		abamClient.updateThesis(thesisToUpdate);
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

	public List<Application> getMasterApplicationList() {
		return abamClient.getMasterApplicationListFromDepartmentCode(selectedDepartmentCode);
	}

	public List<Application> getBachelorApplicationListFromSelectedDepartmentNumber() {
		return abamClient.getBachelorApplicationListFromDepartmentCode(selectedDepartmentCode);
	}

	public Assignment getAssignmentFromId(int id) {
		return abamClient.getAssignmentFromId(id);
	}

	
	/**
	 * Gets the Departments from the webservice, and sets the name based on selected language 
	 */
	public void getDepartmentListFromWebService() {
	  List<Organization> deps = abamClient.getDepartmentList();
		departmentSelectItemList.clear();
		for (Organization dep : deps) {
      String placeRef = dep.getPlaceRef();
      SelectItem item = null;
      if (placeRef == null || placeRef.length() == 0) {
        item = new SelectItem("", "");
      } else {
        item = new SelectItem(placeRef, getText(dep.getName()));
      }
      departmentSelectItemList.add(item);
    }
    departmentList = deps;
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

	public List<no.uis.service.model.StudyProgram> getSelectedStudyProgramList() {
		return selectedStudyProgramList;
	}

	public void setSelectedStudyProgramList(List<no.uis.service.model.StudyProgram> list) {
		this.selectedStudyProgramList = list;
		updateStudyProgramSelectItemList();
	}
	
	private void updateStudyProgramSelectItemList() {
		studyProgramSelectItemList.clear();
		for (no.uis.service.model.StudyProgram prog : selectedStudyProgramList) {
		  studyProgramSelectItemList.add(new SelectItem(prog.getId(), getText(prog.getName())));
    }
	}

	public List<Thesis> getThesisList() {
		return abamClient.getThesisList();
	}

	public List<Assignment> getAssignmentSet() {
		return assignmentSet;
	}

	public void setAssignmentSet(List<Assignment> assignmentSet) {
		this.assignmentSet = assignmentSet;
	}

	public List<Assignment> getAllAssignmentsSet() {
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
	public List<Assignment> getActiveAssignmentsSet() {
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
	
	AbamWebService getAbamClient() {
	  return this.abamClient;
	}
	
	/**
	 * @param permissionName 
	 * @return true if user has permission, false if not
	 */
	public boolean checkPermission(String permissionName) {
		User user = themeDisplay.getUser();
		// TODO remove hardcoded value
		if (user.getUuid().equals("830c81bf-1684-4114-8981-6be03daabb84")) {
		  return true;
		}
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
	
	/**
	 * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
	 */
	public boolean isShouldDisplayAssignAssignments() {
		return checkPermission("ASSIGN_ASSIGNMENTS");						
	}
	
  /**
   * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
   */
	public boolean isShouldDisplayCreateAssignments() {
		return checkPermission("CREATE_ASSIGNMENTS");						
	}
	
  /**
   * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
   */
	public boolean isShouldDisplayEditAssignments() {
		return checkPermission("EDIT_ASSIGNMENTS");						
	}
	
  /**
   * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
   */
	public boolean isShouldDisplayViewSupervisedThesis() {
		return checkPermission("VIEW_SUPERVISED_THESIS");						
	}
	
  /**
   * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
   */
	public boolean isShouldDisplayRenewAssignments() {
		return checkPermission("RENEW_ASSIGNMENTS");						
	}

  /**
   * the permission name is defined in classpath:(resource-actions/employee_permissions.properties
   */
	public boolean isShouldDisplayViewExternalExaminer() {
		return checkPermission("VIEW_EXTERNAL_EXAMINER");						
	}
	
	
	/**
   * Theses roles are defined in Liferay.
   * TODO when the roles are defines in LDAP, this code has to be removed.
	 * @return true if user is administrative employee, false if not
	 */
	public boolean isAdministrativeEmployee() {
		User user = themeDisplay.getUser();
		boolean isAdmin=false;
		for (Role role : user.getRoles()) {			
			if(role.getName().equalsIgnoreCase(ABAM_ADMINISTRATIVE_ROLE_NAME)) {
			  isAdmin = true;
				break;
			}
		}
		// TODO remove hard-coded value
		isAdmin = true;
		return isAdmin;
	}
	
	/**
	 * Theses roles are defined in Liferay.
	 * TODO when the roles are defines in LDAP, this code has to be removed.
	 */
	public void addRoleToEmployee() {
		if (loggedInEmployee != null && !loggedInEmployee.getGroups().isEmpty()) {
			if (loggedInEmployeeIsScientificEmployee()) {
				addRoleToLiferayUser(ABAM_SCIENTIFIC_ROLE_NAME);
			} else if (loggedInEmployeeIsAdministrativeEmployee()) {
				addRoleToLiferayUser(ABAM_ADMINISTRATIVE_ROLE_NAME);
			}
		}
	}

	private void addRoleToLiferayUser(String roleName) {
		long companyId = themeDisplay.getCompanyId();
		try {

			List<Role> roleList = RoleLocalServiceUtil.getRoles(companyId);
			for (Role role : roleList) {
				if (role.getName().equals(roleName)) {
					long[] roleId = {role.getRoleId()};							
					RoleLocalServiceUtil.addUserRoles(themeDisplay.getUserId(), roleId); 
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	private boolean loggedInEmployeeIsAdministrativeEmployee() {
	  List<AffiliationType> affiliations = abamClient.getAffiliation(loggedInEmployee.getEmployeeId());
	  return affiliations.contains(AffiliationType.EMPLOYEE) && !affiliations.contains(AffiliationType.FACULTY); 
	}

	private boolean loggedInEmployeeIsScientificEmployee() {
    List<AffiliationType> affiliations = abamClient.getAffiliation(loggedInEmployee.getEmployeeId());
    if (affiliations != null) {
      return affiliations.contains(AffiliationType.FACULTY);
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
	public synchronized Employee getLoggedInEmployee() {
		if (loggedInEmployee == null) {
			String loginName = null;
			User user = getThemeDisplay().getUser();
			try {			
        loginName = getUserCustomAttribute(user, COLUMN_UIS_LOGIN_NAME);
			} catch (Exception e) {
			  log.warn(user.getFullName(), e);
			}
			Employee employee = null;
			if (loginName != null) {
			  try {
			    employee = abamClient.getEmployeeFromUisLoginName(loginName);
			  } catch(Exception e) {
			    log.warn(loginName, e);
			  }
			}
			if(employee == null) {
				employee = UNKNOWN_EMPLOYEE;
			}
			loggedInEmployee = employee;
		}
		return loggedInEmployee;
	}		
	
	// TODO this is the same function as in StudentService, put common code in a library
	private static String getUserCustomAttribute(User user, String columnName) throws PortalException, SystemException {
    // we cannot use the user's expando bridge here because the permission checker is not initialized properly at this stage      
    String data = ExpandoValueLocalServiceUtil.getData(User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME,
        columnName, user.getUserId(), (String)null);
     return data;
	}

//	public void setLoggedInEmployee(Employee loggedInEmployee) {
//		this.loggedInEmployee = loggedInEmployee;
//	}
//	
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
		Employee employee = getLoggedInEmployee();		
		return abamClient.getArchivedThesisListFromUisLoginName(employee.getEmployeeId());		
	}

  public void setSelectedStudyProgramListFromDepartmentCode(String departmentCode) {
    List<no.uis.service.model.StudyProgram> progs = abamClient.getStudyProgramsFromDepartmentFSCode(departmentCode);
    this.selectedStudyProgramList = progs;
    updateStudyProgramSelectItemList();
  }
  
  // TODO move to common code
  private String getText(List<BaseText> txtList) {
    String lang = "en";
    if (res.getString(LANGUAGE).equals(NORWEGIAN_LANGUAGE)) {
      lang = "nb";
    }
    return BaseTextUtil.getText(txtList, lang);
  }
  
  private static class UnknownEmployee extends Employee {

    private static final Long LONG_ZERO = Long.valueOf(0);
    private static final String EMPTY_STRING = "";
    private static final long serialVersionUID = 1L;
    
    @Override
    public String getEmployeeId() {
      return EMPTY_STRING;
    }

    @Override
    public List<AbamGroup> getGroups() {
      return Collections.emptyList();
    }

    @Override
    public String getName() {
      return EMPTY_STRING;
    }

    @Override
    public String getEmail() {
      return EMPTY_STRING;
    }

    @Override
    public String getPhoneNumber() {
      return EMPTY_STRING;
    }

    @Override
    public Long getOid() {
      return LONG_ZERO;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (this == obj) {
        return true;
      }
      return false;
    }

    @Override
    public void setEmployeeId(String employeeId) {
    }

    @Override
    public void setGroups(List<AbamGroup> groups) {
    }

    @Override
    public void setEmail(String email) {
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void setOid(Long oid) {
    }
  }
}
