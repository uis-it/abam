package no.uis.portal.employee;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlDataTable;

import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Supervisor;
import no.uis.abam.dom.Thesis;
import no.uis.abam.dom.ThesisInformation;
import no.uis.abam.dom.ThesisStatus;

public class ThesisBean {

	private Logger log = Logger.getLogger(ThesisBean.class);
	
	private List<Thesis> thesisList;
	private List<ThesisInformation> thesisInformationList;
	private List<ThesisStatus> thesisStatusList; 
	
	private EmployeeService employeeService;
	
	public ThesisBean() {
		
	}
	
	public void actionPrepareAllStudentTheses(ActionEvent event) {
		thesisList = new ArrayList<Thesis>();		
		List<Thesis> tempList = employeeService.getThesisList();
		if (tempList != null && !tempList.isEmpty()) {
			thesisList.addAll(tempList);
		}
		createThesisInformationFromThesis(true);
	}	
	
	public void actionPrepareMyStudentTheses(ActionEvent event) {
		thesisList = new ArrayList<Thesis>();		
		List<Thesis> tempList = employeeService.getThesisList();
		if (tempList != null && !tempList.isEmpty()) {
			thesisList.addAll(tempList);
		}
		createThesisInformationFromThesis(false);
	}
	
	public void actionPrepareThesisStatusList(ActionEvent event) {
		ThesisInformation selectedThesis = (ThesisInformation) getRowFromEvent(event);
		setThesisStatusList(selectedThesis.getThesis().getStatusList());
	}
	
	private Object getRowFromEvent(ActionEvent event) {
		UIComponent uic = event.getComponent();		
		HtmlDataTable table = (HtmlDataTable)uic.getParent().getParent();
		return table.getRowData();
	}

	private void createThesisInformationFromThesis(boolean isAdministrative) {
		log.setLevel(Level.DEBUG);
		thesisInformationList = new ArrayList<ThesisInformation>();
		Employee employee = employeeService.getEmployeeFromUisLoginName();
		if (!thesisList.isEmpty()) {
			for (Thesis thesis : thesisList) {
				if(isAdministrative || thesis.getFacultySupervisor().getName().equals(employee.getName())
						|| loggedInUserIsSupervisor(thesis.getAssignedAssignment().getSupervisorList()) ) {
					ThesisInformation ti = new ThesisInformation();
					
					ti.setAssignmentTitle(thesis.getAssignedAssignment().getTitle());
					if (thesis.getStudentNumber2() != null)
						ti.setCoStudent1Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber2()).getName());
					if (thesis.getStudentNumber3() != null)
						ti.setCoStudent2Name(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber3()).getName());
					ti.setEvaluationSubmissionDeadlineAsString(thesis.getDeadlineForSubmissionForEvalutationAsString());
					ti.setStudentName(employeeService.getStudentFromStudentNumber(thesis.getStudentNumber1()).getName());
					ti.setThesis(thesis);
					thesisInformationList.add(ti);
				}
			}		
		}
	}
	
	private boolean loggedInUserIsSupervisor(
			ArrayList<Supervisor> supervisorList) {
		
		Employee employee = employeeService.getEmployeeFromUisLoginName();
		
		for (Supervisor supervisor : supervisorList) {
			if(!supervisor.isExternal() && supervisor.getName().equalsIgnoreCase(employee.getName())) {
				return true;
			}
		}
		return false;
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

	public List<ThesisStatus> getThesisStatusList() {
		return thesisStatusList;
	}

	public void setThesisStatusList(List<ThesisStatus> thesisStatusList) {
		this.thesisStatusList = thesisStatusList;
	}
	
}
