package no.uis.abam.ws_abam;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Supervisor;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestWebservice {

  private static AbamWebService abamService;
  private static Properties testData;

  @BeforeClass
  public static void initSpring() {
    
    System.setProperty("catalina.base", "x:");
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/abam-ws/beans.xml");
    abamService = ctx.getBean("abamWS", AbamWebService.class);
    
    testData = ctx.getBean("config-props", Properties.class);
  }
  
//	@Test
	public void testEmployee() throws Exception {
	  String testUid = testData.getProperty("test.employee.1");
	  Employee employee = abamService.getEmployeeFromUisLoginName(testUid);
	  assertThat(employee, is(notNullValue()));
	}
	
//	@Test
	public void testStudent() throws Exception {
	  String studNo = testData.getProperty("test.student.1");
	  Student student = abamService.getStudentFromStudentNumber(studNo);
	  
	  assertThat(student, is(notNullValue()));
	  
	  String deptName = testData.getProperty("test.student.2");
	  assertThat(student.getDepartmentCode(), is(equalTo(deptName)));
	}
	
	//@Test
	public void persistAssignment() throws Exception {
	  Assignment assignment = new Assignment();
	  assignment.setAddedDate(Calendar.getInstance());
    String testUid = testData.getProperty("test.employee.1");
    Employee employee = abamService.getEmployeeFromUisLoginName(testUid);
	  assignment.setAuthor(employee);
	  assignment.setCustom(false);
	  assignment.setTitle("Test");
	  assignment.setDescription("Test description");
	  assignment.setDepartmentCode("217_8_2_0");
	  assignment.setStudyProgramCode("B-ELEKTRO");
	  assignment.setType(AssignmentType.BACHELOR);
	  Supervisor supervisor = new Supervisor();
	  supervisor.setCompanyName("Testing");
	  supervisor.setName("Test supervisor");
	  supervisor.setExternal(true);
	  supervisor.setEmail("test@test.de");
    assignment.getSupervisorList().add(supervisor);
	  abamService.saveAssignment(assignment);
	  
	  List<Assignment> allAssignments = abamService.getAllAssignments();
	  assertThat(allAssignments, hasItem(notNullValue(Assignment.class)));
	}
	
	@Test
	public void getActiveAssignments() throws Exception {
	  List<Assignment> assignments = abamService.getActiveAssignments();
	  
	  assertThat(assignments, is(notNullValue()));
	  assertThat(assignments, hasItem(notNullValue(Assignment.class)));
	}
}
