package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.ExternalExaminer;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisInformation;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.context.DisposableBean;

public class ExternalExaminerBean implements DisposableBean{

	private boolean showSavedConfirmation;
	
	private List<ThesisInformation> thesisInformationList = new ArrayList<ThesisInformation>();
	
	private EmployeeService employeeService;
	
	private ExternalExaminer externalExaminer;
	
	public ExternalExaminerBean() {
		
	}
	
	public void actionPrepareAddExternalExaminer(ActionEvent event) {
		employeeService.getDepartmentListFromWebService();
		thesisInformationList.clear();
		setShowSavedConfirmation(false);
		setExternalExaminer(new ExternalExaminer());
		List<Thesis> thesisList = employeeService.getThesisList();
		createThesisInformationFromThesis(thesisList);
	}
	
	private void createThesisInformationFromThesis(List<Thesis> thesisList) {
		ThesisInformation thesisInformation;
		if(thesisList != null) {
			for (Thesis thesis : thesisList) {
				thesisInformation = new ThesisInformation();
				Assignment assignment = thesis.getAssignedAssignment();
				if (assignment == null) {
					Student student = employeeService.getStudentFromStudentNumber(thesis.getStudentNumber1());
					thesisInformation.setAssignmentTitle(student.getCustomAssignment().getTitle());
				} else {
					thesisInformation.setAssignmentTitle(assignment.getTitle());
				}
				if (thesis.getStudentNumber2() != null && !thesis.getStudentNumber2().isEmpty())
					thesisInformation.setCoStudent1Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber2()).getName());
				if (thesis.getStudentNumber3() != null && !thesis.getStudentNumber3().isEmpty())
					thesisInformation.setCoStudent2Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber3()).getName());
				
				ExternalExaminer examiner = thesis.getExternalExaminer();
				if (examiner == null) {
					thesisInformation.setExternalExaminerName("");
				} else {
					thesisInformation.setExternalExaminerName(thesis.getExternalExaminer().getName());
				}
				thesisInformation.setStudentName(employeeService
						.getStudentFromStudentNumber(thesis.getStudentNumber1())
						.getName());
				thesisInformation.setThesis(thesis);	
				thesisInformationList.add(thesisInformation);
			}
		}
	}
	
	public void actionSaveExaminerToSelectedRows(ActionEvent event) {
		for (ThesisInformation thesisInformation : thesisInformationList) {
			if (thesisInformation.isSelected()) {
				thesisInformation.getThesis().setExternalExaminer(externalExaminer);
				thesisInformation.setExternalExaminerName(externalExaminer.getName());
				employeeService.updateThesis(thesisInformation.getThesis());
			}
		}
		setShowSavedConfirmation(true);
	}
	
	public void actionClearFields(ActionEvent event) {
		setExternalExaminer(new ExternalExaminer());
	}
	
	public void actionDisplayDepartmentTheses(ValueChangeEvent event) {
		List<Thesis> thesisList = employeeService.getThesisList();
		
		thesisList = employeeService.getThesisListFromDepartmentCode(
				employeeService.getDepartmentCodeFromIndex(
						Integer.parseInt(event.getNewValue().toString())
						));
		if(thesisList != null) createThesisInformationFromThesis(thesisList);
		else thesisInformationList.clear();
		actionClearFields(null);
	}
	
	
	public void actionSetFields(ActionEvent event) {
		ThesisInformation ti = (ThesisInformation) getRowFromEvent(event);
		setExternalExaminer(ti.getThesis().getExternalExaminer());
	}
	
	private Object getRowFromEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		return table.getRowData();
	}

	public boolean isShowSavedConfirmation() {
		return showSavedConfirmation;
	}

	public void setShowSavedConfirmation(boolean showSavedConfirmation) {
		this.showSavedConfirmation = showSavedConfirmation;
	}

	public List<ThesisInformation> getThesisInformationList() {
		return thesisInformationList;
	}

	public void setThesisInformationList(
			List<ThesisInformation> thesisInformationList) {
		this.thesisInformationList = thesisInformationList;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public ExternalExaminer getExternalExaminer() {
		return externalExaminer;
	}

	public void setExternalExaminer(ExternalExaminer externalExaminer) {
		this.externalExaminer = externalExaminer;
	}

	@Override
	public void dispose() throws Exception {
	}

}
