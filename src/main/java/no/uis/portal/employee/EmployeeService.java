package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.bridges.jsf.common.JSFPortletUtil;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.StudyProgram;
import no.uis.abam.dom.Thesis;
import no.uis.abam.ws_abam.AbamWebService;

public class EmployeeService {
	
	private static final String LANGUAGE = "language";
	private static final String NORWEGIAN_LANGUAGE = "Norsk";

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
	
	private FacesContext context;
	private Locale locale;
	private ResourceBundle res;
	
	private User loggedInUser;
	
	public EmployeeService() {	
		context  = FacesContext.getCurrentInstance();
		locale = context.getViewRoot().getLocale();
		res = ResourceBundle.getBundle("Language", locale);
	}

	public void saveAssignment(Assignment assignment) {
		abamClient.saveAssignment(assignment);
	}

	public void actionPrepareDisplayAssignments(ActionEvent event) {		
		getActiveAssignmentsSet();
		setSelectedStudyProgramNumber(0);
		setSelectedDepartmentNumber(0);
		getDepartmentListFromWebService();
	}

	public void actionUpdateStudyProgramList(ValueChangeEvent event) {
		setSelectedDepartmentAndStudyProgramFromValue(Integer.parseInt(event.getNewValue().toString()));
		if(studyProgramMenu != null) studyProgramMenu.setValue(getSelectedStudyProgramNumber());
		for (Assignment assignment : assignmentSet) {
				if (assignment.getDepartmentCode().equals(selectedDepartmentCode)
					|| selectedDepartmentCode.equals("")) {
				assignment.setDisplayAssignment(true);
				String depName = getDepartmentNameFromCode(assignment.getDepartmentCode());
				assignment.setDepartmentName(depName);
			} else
				assignment.setDisplayAssignment(false);
		}
		//setAllEditExternalExaminerToFalse();
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
		methodForFindingUserInfoToBeUsed();

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
		//setAllEditExternalExaminerToFalse();
	}
	
	public void setDisplayAssignments() {
		String selectedStudyProgram = getStudyProgramNameFromValue(selectedStudyProgramNumber);
		
		
		//TreeSet<Assignment> assignmentList = abamClient.getAllAssignments();
		
		if (selectedDepartmentCode == null)
			
			setSelectedDepartmentCode("");
		if (selectedStudyProgram == null)
			selectedStudyProgram = "";
		for (Assignment assignment : assignmentSet) {
			if (assignmentShouldBeDisplayed(assignment,
					selectedStudyProgram))
				assignment.setDisplayAssignment(true);
			else
				assignment.setDisplayAssignment(false);
		}
		//abamClient.setAssignmentList(assignmentSet);
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

//	public void addNewDepartment() {
//		Department newDepartment = new Department(new Integer(
//				departmentList.size()), "");
//		newDepartment.setEditable(true);
//		departmentList.add(newDepartment);
//	}

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

	public Student getStudentFromStudentNumber(long studentNumber) {
		return abamClient.getStudentFromStudentNumber(studentNumber);
	}

	public void removeAssignment(Assignment assignment) {
		abamClient.removeAssignment(assignment);
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
		return assignmentSet;
	}

	public void setAbamClient(AbamWebService abamClient) {
		this.abamClient = abamClient;
	}

	private void methodForFindingUserInfoToBeUsed() {
		try {
			//String principal = PrincipalThreadLocal.getName();			
			//System.out.println("principal: " + principal);
			//String userId = FacesContext.getCurrentInstance().getRemoteUser();
			String temp = JSFPortletUtil.getPortletRequest(FacesContext.getCurrentInstance()).getRemoteUser();
			System.out.println(temp);
			User user = UserLocalServiceUtil.getUser(Long.valueOf(temp));
			List<Role> roles = user.getRoles();
			boolean isSuperUser = false;
			for (Role role : roles) {
				if(role.getName().equals("AbamSuperUser")) {
					isSuperUser = true;
				}
			}
			System.out.println("User is superUser: " + isSuperUser);
//			List<Role> roles = user.getRoles();			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
}
