package no.uis.portal.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.portlet.RenderRequest;

import no.uis.abam.commons.BaseTextUtil;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.abam.ws_abam.AbamWebService;
import no.uis.service.model.BaseText;
import no.uis.service.model.Organization;
import no.uis.service.model.StudyProgram;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.apache.myfaces.shared_impl.util.MessageUtils;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.util.bridges.jsf.common.LanguageManagedBean;

// TODO improve protection of resources in concurrent thread environment, it is a mess. 
public class StudentService {

  private static final int MAX_BACHELOR_APPLICATIONS = 3;
  
	public static final String COLUMN_UIS_LOGIN_NAME = "UiS-login-name";
	
	private static final String LANGUAGE = "language";
	private static final String NORWEGIAN_LANGUAGE = "Norsk";
	
	private static Logger log = Logger.getLogger(StudentService.class);
	
	private List<Assignment> assignmentList; 
	private Assignment selectedAssignment;
	
	private Student currentStudent;

	private AbamWebService abamStudentClient;
	
	private List<Application> tempApplicationPriorityArray = new ArrayList<Application>();
	private List<Application> applicationsToRemove = new ArrayList<Application>();
	private List<SelectItem> departmentSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> studyProgramSelectItemList = new ArrayList<SelectItem>();
	private List<Organization> departmentList;
	
	private String selectedStudyProgramCode;
	
	private ThemeDisplay themeDisplay;
	
	private FacesContext context;
	private Locale locale;
  private ResourceBundle res;
	
	public StudentService() {
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
	
	private Student getStudentFromLogin() {
		String loginName = null;
		try {			
			loginName = getUserCustomAttribute(getThemeDisplay().getUser(), COLUMN_UIS_LOGIN_NAME);
		} catch (Exception e) {
		  log.warn(loginName, e);
		}
		Student student = null;
		if (loginName != null) {
		  student = abamStudentClient.getStudentFromStudentNumber(loginName);
		}
		return student;
	}

	public ThemeDisplay getThemeDisplay() {
		return themeDisplay;
	}
	
	 // TODO this is the same function as in EmployeeService, put common code in a library
	private static String getUserCustomAttribute(User user, String columnName) throws PortalException, SystemException {
	  // we cannot use the user's expando bridge here because the permission checker is not initialized properly at this stage	    
		String data = ExpandoValueLocalServiceUtil.getData(User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME,
	      columnName, user.getUserId(), (String)null);
	   return data;
	}
	
	public int getNextId(){
		return abamStudentClient.getNextId();
	}
	
	public void saveAssignment(Assignment assignment) {
		getCurrentStudent().setCustomAssignment(assignment);
	}
	
	public void setApplicationToStudent(Application application) {
	  Student stud = getCurrentStudent();
	  if (applicationIsLegitimate(stud, application)) {
	    stud.getApplications().add(application);
	  }
		abamStudentClient.updateStudent(stud);
	}
	
  private static boolean applicationIsLegitimate(Student student, Application application) {
    List<Application> applications = student.getApplications();
    if (applications.size() >= MAX_BACHELOR_APPLICATIONS) {
      MessageUtils.addMessage(FacesMessage.SEVERITY_WARN, "maximum_applications_reached", new Object[] {MAX_BACHELOR_APPLICATIONS});
      return false;
    }
    if (applications.contains(application)) {
      MessageUtils.addMessage(FacesMessage.SEVERITY_WARN, "applications_already_applied_for", null);
      return false;
    }
    return true;
  }
  
	public List<Assignment> getAssignmentList() {
		if(assignmentList == null) 
			assignmentList = abamStudentClient.getAssignmentsFromDepartmentCode(getCurrentStudent().getDepartmentCode());
		return assignmentList;		
	}

	
	public Assignment getSelectedAssignment() {
		return selectedAssignment;
	}

	
	public void setSelectedAssignment(Assignment selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
	
	public void actionRemoveApplication(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		applicationsToRemove.add(application);
		removeApplication(application);
	}
	
	public void actionSetApplicationPriorityHigher(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		moveApplicationHigher(application);
	}
	
	public void actionSetApplicationPriorityLower(ActionEvent event) {
		Application application = getApplicationFromEvent(event);
		moveApplicationLower(application);
	}
	
	private Application getApplicationFromEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent().getParent();
		return (Application)table.getRowData();
	}
	 
	public void actionGetApplicationFromStudent(ActionEvent event) {
		tempApplicationPriorityArray.clear(); 
		tempApplicationPriorityArray.addAll(getCurrentStudent().getApplications());
	}
	
	public void actionClearStudyProgramAndDepartmentNumber(ActionEvent event){
		setSelectedStudyProgramCode(null);
	}
	
	private void updateStudyProgramList(String departmentCode){
		List<Assignment> assignmentList = getAssignmentList();
		//Student stud = getCurrentStudent();
		if (assignmentList != null) {
			for (Assignment assignment : assignmentList) {
				if (assignment.getDepartmentCode().equals(departmentCode)) { 
					if(currentStudentIsEligibleForAssignment(assignment)) {
						assignment.setDisplayAssignment(true);
						//String depName = getDepartmentNameFromCode(assignment.getDepartmentCode());
					} else {
					  assignment.setDisplayAssignment(false);
					}
				} else {
				  assignment.setDisplayAssignment(false);
				}
			}
		}
	}
	
	private boolean currentStudentIsEligibleForAssignment(Assignment assignment) {
	  AssignmentType studentType = getCurrentStudent().getType();
		return assignment.getType().equals(studentType);
	}
	
	public void actionSetDisplayAssignment(ValueChangeEvent event){
	  
		selectedStudyProgramCode = event.getNewValue().toString();
    String selectedStudyProgram = getStudyProgramNameFromCode(selectedStudyProgramCode);
		assignmentList = getAssignmentList();
		if (assignmentList != null) {
			for (Assignment assignment : assignmentList) {
				if (checkIfAssignmentShouldBeDisplayed(assignment,
						selectedStudyProgram)) {
					if (currentStudentIsEligibleForAssignment(assignment))
						assignment.setDisplayAssignment(true);
				} else
					assignment.setDisplayAssignment(false);
			}
		}		
	}
	
	private boolean checkIfAssignmentShouldBeDisplayed(Assignment abIn, String selectedStudyProgram) {
	  Student stud = getCurrentStudent();
	  String assignStudProgName = getStudyProgramNameFromCode(abIn.getStudyProgramCode());
	  
  return (selectedStudyProgram.equals("") && abIn.getDepartmentCode().equals(stud.getDepartmentCode())) 
  || assignStudProgName.equals(selectedStudyProgram);
	  
	}
	
	private String getStudyProgramNameFromCode(String progCode) {
	  no.uis.service.model.StudyProgram prog = abamStudentClient.getStudyProgramFromCode(progCode);
	  return getText(prog.getName());
	}
	
  public void updateSelectedAssignmentInformation(Assignment selectedAssignment){
		setSelectedAssignment(selectedAssignment);
		//setStudyProgramListFromDepartmentNumber(selectedAssignment.getDepartmentNumber());
		
		setSelectedStudyProgramCode(selectedAssignment.getStudyProgramCode());
	}
	
	
	public void actionPrepareAvailableAssignments(ActionEvent event) {		
		assignmentList = abamStudentClient.getAssignmentsFromDepartmentCode(getCurrentStudent().getDepartmentCode());			
		updateStudyProgramList(findDepartmentCodeForCurrentStudent());		
		getStudyProgramList();
	}
	
	public String findDepartmentCodeForCurrentStudent() {
		return getCurrentStudent().getDepartmentCode();
	}
	
	public void actionSaveApplications(ActionEvent event) {
		removeDeletedApplications();
		getCurrentStudent().setApplications(tempApplicationPriorityArray);
		abamStudentClient.updateApplications(tempApplicationPriorityArray);		
		abamStudentClient.updateStudent(getCurrentStudent());
	}
	
	private void removeDeletedApplications() {
		for (Application application : applicationsToRemove) {
			abamStudentClient.removeApplication(application);
		}
	}
	
	public void actionClearDeletedElements(ActionEvent event){
		applicationsToRemove.clear();
	}
	
	public synchronized List<Organization> getDepartmentList() {
	  Student stud = getCurrentStudent();
		if(departmentList == null && stud != null) {
		  List<Organization> depts = abamStudentClient.getDepartmentList();
		  int i = 0;
			for (Organization dept : depts) {
        if (dept.getPlaceRef().equals(stud.getDepartmentCode())) {
          String name = getText(dept.getName());
          departmentSelectItemList.add(new SelectItem(dept.getPlaceRef(),name));
        }
      }
			departmentList = depts;
		}
		return departmentList;
	}

  private String getText(List<BaseText> txtList) {
    // TODO should be determined by current language, not a property in the resource bundle
    String lang = "en";
    if (res.getString(LANGUAGE).equals(NORWEGIAN_LANGUAGE)) {
      lang = "nb";
    }
    
    return BaseTextUtil.getText(txtList, lang);
  }

  // TODO move to common code
  private List<StudyProgram> getStudyProgramList() {
		List<StudyProgram> studyProgramList = null; 
		
		Student stud = getCurrentStudent();
		if (stud != null) {
		  studyProgramList = abamStudentClient.getStudyProgramsFromDepartmentFSCode(stud.getDepartmentCode());
		}
		studyProgramSelectItemList.clear();
		if (studyProgramList == null) {
		  return Collections.emptyList();
		}
		for (no.uis.service.model.StudyProgram program : studyProgramList) {
		  studyProgramSelectItemList.add(new SelectItem(program.getId(), getText(program.getName())));
    }
		return studyProgramList;
	}
	
	public void removeAssignment(Assignment assignment) {
		abamStudentClient.removeAssignment(assignment);
	}

	public String getSelectedStudyProgramCode() {
		return selectedStudyProgramCode;
	}
	
	public void setSelectedStudyProgramCode(String selectedStudyProgramCode) {
		this.selectedStudyProgramCode = selectedStudyProgramCode;
	}

	public synchronized Student getCurrentStudent() {
		if (currentStudent == null) {
			Student stud = getStudentFromLogin();
			if (stud == null) {
			  stud = new Student();
			  stud.setName("unknown student");
			  stud.setType(AssignmentType.BACHELOR);
			}
			currentStudent = stud;
		}
		return currentStudent;
	}

  public String getCurrentDepartmentName() {
      // TODO show name instead
    return currentStudent.getDepartmentCode();
  }
  
	public void updateStudentInWebServiceFromCurrentStudent() {
	  Student stud = getCurrentStudent();
		abamStudentClient.updateStudent(stud);
	}
	
	public List<Application> getApplicationList() {
		return abamStudentClient.getApplicationList();
	}

	public void saveApplication(Application application) {
		abamStudentClient.saveApplication(application);
	}

	public void setAbamStudentClient(AbamWebService abamStudentClient) {
		this.abamStudentClient = abamStudentClient;
	}
	
	public void removeApplication(Application application) {
	  tempApplicationPriorityArray.remove(application);
	}

	// TODO solve with sorting
	private void moveApplicationHigher(Application selectedApplication) {
//		int selectedApplicationIndex = findApplicationIndex(selectedApplication);
//		if(selectedApplicationIndex > 0) {
//			int higherApplicationIndex = selectedApplicationIndex - 1;
//			Application higherApplication = tempApplicationPriorityArray[higherApplicationIndex];
//			if(selectedApplication != null){
//				selectedApplication.setPriority(higherApplicationIndex + 1);
//				if(higherApplication != null){
//					higherApplication.setPriority(selectedApplicationIndex + 1);
//				}
//				tempApplicationPriorityArray[higherApplicationIndex] = selectedApplication;
//				tempApplicationPriorityArray[selectedApplicationIndex] = higherApplication;								
//					
//			}
//		}
	}
	
	// TODO solve with sorting
	private void moveApplicationLower(Application selectedApplication) {
//		int selectedApplicationIndex = findApplicationIndex(selectedApplication);
//		if(selectedApplicationIndex != 2) {
//			int lowerApplicationIndex = selectedApplicationIndex + 1;
//			Application lowerApplication = tempApplicationPriorityArray[lowerApplicationIndex];
//			if(lowerApplication != null) {
//				lowerApplication.setPriority(selectedApplicationIndex + 1);
//				tempApplicationPriorityArray[lowerApplicationIndex] = selectedApplication;
//				tempApplicationPriorityArray[selectedApplicationIndex] = lowerApplication;
//				if (selectedApplication != null) {
//					selectedApplication.setPriority(lowerApplicationIndex + 1);
//				}
//			}
//		}
	}

	public List<Application> getTempApplicationPriorityArray() {
		return tempApplicationPriorityArray;
	}

	public Assignment getAssignmentFromId(long assignedAssignmentId) {
		return abamStudentClient.getAssignmentFromId(assignedAssignmentId);
	}

	public List<SelectItem> getDepartmentSelectItemList() {
		return departmentSelectItemList;
	}

	public String getStudentInstitute() {
	  Student stud = getCurrentStudent();
	  if (stud != null) {
	    return stud.getDepartmentName();
	  }
	  return "";
	}
	
	public void setDepartmentSelectItemList(
			ArrayList<SelectItem> departmentSelectItemList) {
		this.departmentSelectItemList = departmentSelectItemList;
	}

	public List<SelectItem> getStudyProgramSelectItemList() {
		return studyProgramSelectItemList;
	}

	public void setStudyProgramSelectItemList(
			List<SelectItem> studyProgramSelectItemList) {
		this.studyProgramSelectItemList = studyProgramSelectItemList;
	}

	public Employee getEmployeeFromFullName(String name) {
		return abamStudentClient.getEmployeeFromFullName(name);
	}

	public Student getStudentFromStudentNumber(String studentNumber) {
		return abamStudentClient.getStudentFromStudentNumber(studentNumber);
	}

	public void updateThesis(Thesis thesis) {
		abamStudentClient.updateThesis(thesis);
		
	}
	
	public void updateStudent(Student std) {
		abamStudentClient.updateStudent(std);
	}
	
	public Assignment getCustomAssignmentFromStudentNumber(String studentNumber) {
		return abamStudentClient.getCustomAssignmentFromStudentNumber(studentNumber);
	}
	
}


