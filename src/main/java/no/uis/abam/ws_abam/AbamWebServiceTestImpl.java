package no.uis.abam.ws_abam;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import javax.jws.WebService;

import no.uis.abam.dom.AssigmentIdComparator;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.ExternalExaminer;

@WebService(endpointInterface = "no.uis.abam.ws_abam.AbamWebService")
public class AbamWebServiceTestImpl implements AbamWebService {

	private Set<Assignment> assignmentList = new TreeSet<Assignment>(new AssigmentIdComparator());
	
	public AbamWebServiceTestImpl(){
		createAssignmentListContent();
	}
	
	public Set<Assignment> getAllAssignments() {
		return assignmentList;
	}
	
	public void saveAssignment(Assignment assignment){
		assignmentList.add(assignment);
	}

	private void createAssignmentListContent(){
		Assignment test1 = new Assignment();
		test1.setTitle("Pet Bor oppgave");
		test1.setBachelor(true);
		test1.setDescription("Beskrivelse av test1");
		test1.setNumberOfStudents("2-3");
		test1.setId(1);
		test1.setDepartment("Petroleumsteknologi");
		test1.setDepartmentNumber(2);
		test1.setStudyProgram("Boreteknologi");
		test1.setStudyProgramNumber(1);
		test1.setFacultySupervisor("Louis Lane");
		test1.getSupervisorList().get(0).setName("Superman");
		test1.setExternalExaminer(new ExternalExaminer("tester"));
		test1.setAddedDate(new GregorianCalendar(10, 11, 10));
		GregorianCalendar dato = test1.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test1.setExpireDate(dato);
		
		Assignment test2 = new Assignment();
		test2.setTitle("IDE El oppgave");
		test2.setBachelor(false);
		test2.setMaster(true);
		test2.setDescription("Beskrivelse av test2");
		test2.setNumberOfStudents("1");
		test2.setDepartment("Data- og elektroteknikk");
		test2.setDepartmentNumber(3);
		test2.setStudyProgram("Elektro");
		test2.setStudyProgramNumber(2);
		test2.setId(2);
		test2.setFacultySupervisor("Robin");
		test2.getSupervisorList().get(0).setName("Batman");
		test2.setAddedDate(new GregorianCalendar(2010, 10, 10));
		dato = test2.getAddedDate();
		dato.add(Calendar.MONTH, 6);
		test2.setExpireDate(dato);
		assignmentList.add(test1);
		assignmentList.add(test2);
	}
	
}
