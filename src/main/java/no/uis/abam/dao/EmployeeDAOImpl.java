package no.uis.abam.dao;

import java.util.ArrayList;
import java.util.List;

import no.uis.abam.dom.Employee;
import no.uis.service.idm.ws.IdmWebService;
import no.uis.service.model.Organization;
import no.uis.service.model.Person;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

public class EmployeeDAOImpl implements EmployeeDAO {
	
  private static Logger log = Logger.getLogger(EmployeeDAOImpl.class);
  
  private IdmWebService idmService;
	
	public EmployeeDAOImpl() {
//		BeanFactory factory = new ClassPathXmlApplicationContext("employee-beans.xml");
//	    uas = (UserAccountService)factory.getBean("userAccountService");
	}

	public void setIdmService(IdmWebService idmService) {
	  this.idmService = idmService;
	}
	
	public Employee findEmployeeByEmployeeFullName(String employeeFullName) {
	  try {
	    Person p = idmService.findPersonByFullName(employeeFullName);
	    return convertPersonToEmployee(p);
	  } catch (Exception e) {
	    log.warn(employeeFullName, e);
	  }
	  return null;
	}
	
	public Employee findEmployeeByEmployeeNumber(String employeeNumber) {
	  Person person = idmService.getPersonByEmployeeNumber(employeeNumber);
	  if (person != null) {
			return convertPersonToEmployee(person);
		}
		return null;
	}
	
	
	public List<Employee> getAllTNEmployeesFromLdap() {
	  throw new NotImplementedException(getClass());
//		Person person = new Person();
//		List<Employee> employeeList = new ArrayList<Employee>();
//		person.setFullName("*");
//		List<Person> personList = uas.findPersonByExample(person, true);
//		for (Person person2 : personList) {
//			if(personWorksAtTekNat(person2)) {
//				Employee employee = convertPersonToEmployee(person2);
//				employeeList.add(employee);				
//			}
//		}
//		return employeeList;
	}
	
	private Employee convertPersonToEmployee(Person person) {
		Employee employee = new Employee();
		employee.setName(person.getFullName());
		employee.setEmployeeId(person.getUserId());

		List<Organization> gl = new ArrayList<Organization>();
		List<String> orgUnits = person.getOrgUnits();
		for (String orgUnit : orgUnits) {
		  Organization org = idmService.getOrganizationByDN(orgUnit);
		  gl.add(org);
    }
		// TODO either save the DN, the ID or the Organization object
		employee.setGroupMembership(orgUnits);
		return employee;
	}

}
