package no.uis.abam.ws_abam;

import static org.junit.Assert.*;

import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import no.uis.abam.dom.Department;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.service.idm.ws.IdmWebService;
import no.uis.service.idm.ws.impl.IdmWebServiceImpl;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
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
}
