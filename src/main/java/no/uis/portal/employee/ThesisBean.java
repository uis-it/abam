package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisInformation;

public class ThesisBean {

	private Logger log = Logger.getLogger(ThesisBean.class);
	
	private List<Thesis> thesisList;
	private List<ThesisInformation> thesisInformationList;
	
	private EmployeeService employeeService;
	
	public ThesisBean() {
		
	}
	
	public void actionPrepareMyStudentTheses(ActionEvent event) {
		thesisList = new ArrayList<Thesis>();		
		List<Thesis> tempList = employeeService.getThesisList();
		if (tempList != null && !tempList.isEmpty()) {
			thesisList.addAll(tempList);
		}
		createThesisInformationFromThesis();
	}

	private void createThesisInformationFromThesis() {
		log.setLevel(Level.DEBUG);
		thesisInformationList = new ArrayList<ThesisInformation>();
		Employee employee = employeeService.getEmployeeFromUisLoginName();
		if (!thesisList.isEmpty()) {
			for (Thesis thesis : thesisList) {
				if(thesis.getFacultySupervisor().getName().equals(employee.getName())) {
					ThesisInformation ti = new ThesisInformation();
					
					ti.setAssignmentTitle(thesis.getAssignedAssignment().getTitle());
					//ti.setCoStudent1Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber2()).getName());
					//ti.setCoStudent2Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber3()).getName());
					ti.setCoStudent1Name("");
					ti.setCoStudent2Name("");
					ti.setEvaluationSubmissionDeadlineAsString(thesis.getDeadlineForSubmissionForEvalutationAsString());
					//ti.setExternalExaminerName(thesis.getExternalExaminer().getName());
					ti.setStudentName(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber1()).getName());
					thesisInformationList.add(ti);
				}
			}		
		}
	}
	
	public List<Thesis> getThesisList() {
		return thesisList;
	}

	public void setThesisList(List<Thesis> thesisList) {
		this.thesisList = thesisList;
	}

	public List<ThesisInformation> getThesisInformationList() {
		return thesisInformationList;
	}

	public void setThesisInformationList(
			List<ThesisInformation> thesisInformationList) {
		this.thesisInformationList = thesisInformationList;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
