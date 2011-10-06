package no.uis.abam.ws_abam;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Calendar;
import java.util.Properties;

import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;

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
    abamService = ctx.getBean("abamService", AbamWebService.class);
    
    testData = ctx.getBean("config-props", Properties.class);
  }
  
	@Test
	public void testEmployee() throws Exception {
	  String testUid = testData.getProperty("test.employee.1");
	  Employee employee = abamService.getEmployeeFromUisLoginName(testUid);
	  assertThat(employee, is(notNullValue()));
	  String testOu = testData.getProperty("test.employee.2");
    assertThat(employee.getGroupMembership(), hasItem(testOu));
	}
	
	@Test
	public void testStudent() throws Exception {
	  String studNo = testData.getProperty("test.student.1");
	  Student student = abamService.getStudentFromStudentNumber(studNo);
	  
	  assertThat(student, is(notNullValue()));
	  
	  String deptName = testData.getProperty("test.student.2");
	  assertThat(student.getDepartmentCode(), is(equalTo(deptName)));
	}
	
	@Test
	public void persistAssignment() throws Exception {
	  Assignment assignment = new Assignment();
	  assignment.setAddedDate(Calendar.getInstance());
    String testUid = testData.getProperty("test.employee.1");
    Employee employee = abamService.getEmployeeFromUisLoginName(testUid);
	  assignment.setAuthor(employee);
//	  assignment.
	  abamService.saveAssignment(assignment);
	}
}
