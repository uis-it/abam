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
import com.liferay.portal.security.auth.PrincipalThreadLocal;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.StudyProgram;
import no.uis.abam.dom.Thesis;
import no.uis.abam.ws_abam.AbamWebService;

public class EmployeeService {

	private int selectedDepartmentNumber;
	private int selectedStudyProgramNumber;

	private String selectedDepartmentName;

	private AbamWebService abamClient;

	private List<Department> departmentList;
	private List<StudyProgram> selectedStudyProgramList = new ArrayList<StudyProgram>();

	private List<SelectItem> departmentSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> studyProgramSelectItemList = new ArrayList<SelectItem>();
	
	private HtmlSelectOneMenu studyProgramMenu;
	
	private Set<Assignment> assignmentSet;
	
	public EmployeeService() {		
		
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
			if (assignment.getDepartmentName().equals(selectedDepartmentName)
					|| selectedDepartmentName.equals(""))
				assignment.setDisplayAssignment(true);
			else
				assignment.setDisplayAssignment(false);
		}
		setAllEditExternalExaminerToFalse();
	}

	public void setSelectedDepartmentAndStudyProgramFromValue(int value) {
		setSelectedDepartmentNumber(value);
		Department selectedDepartment = getDepartmentFromValue(selectedDepartmentNumber);
		setSelectedDepartmentName(selectedDepartment.getName());
		setSelectedStudyProgramNumber(0);
		setSelectedStudyProgramList(selectedDepartment.getStudyPrograms());		

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
		setAllEditExternalExaminerToFalse();
	}
	
	public void setDisplayAssignments() {
		String selectedStudyProgram = getStudyProgramNameFromValue(selectedStudyProgramNumber);
		
		
		//TreeSet<Assignment> assignmentList = abamClient.getAllAssignments();
		
		if (selectedDepartmentName == null)
			setSelectedDepartmentName("");
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
				.getDepartmentName().equals(selectedDepartmentName))
				|| assignmentIn.getStudyProgramName().equals(
						selectedStudyProgram)
				|| getSelectedDepartmentName().equals("");
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
		return departmentList.get(index).getOeNavn_Engelsk();
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
		return abamClient.getMasterApplicationList();
	}

	public List<Application> getBachelorApplicationList() {
		return abamClient.getBachelorApplicationList();
	}

	public Assignment getAssignmentFromId(int id) {
		return abamClient.getAssignmentFromId(id);
	}

	public void getDepartmentListFromWebService() {
		departmentList = abamClient.getDepartmentList();
		departmentSelectItemList.clear();
		FacesContext context  = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
        ResourceBundle res = ResourceBundle.getBundle("Language", locale);
        System.out.println(res.getString("language"));
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

	public String getSelectedDepartmentName() {
		return selectedDepartmentName;
	}

	public void setSelectedDepartmentName(String selectedDepartment) {
		this.selectedDepartmentName = selectedDepartment;
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
			String principal = PrincipalThreadLocal.getName();
		
			//String userId = FacesContext.getCurrentInstance().getRemoteUser();
//			User user = UserLocalServiceUtil.getUser(Long.valueOf(userId));
//			List<Role> roles = user.getRoles();			
		} catch (NumberFormatException e) {
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
