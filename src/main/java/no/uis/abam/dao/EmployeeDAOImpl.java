package no.uis.abam.dao;

import java.util.ArrayList;
import java.util.List;

import no.uis.abam.dom.Employee;
import no.uis.service.model.AffiliationData;
import no.uis.service.model.EmployeeData;
import no.uis.service.model.GroupData;
import no.uis.service.model.Person;
import no.uis.service.useraccount.UserAccountService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EmployeeDAOImpl implements EmployeeDAO {
	
	private UserAccountService uas;
	
	public EmployeeDAOImpl() {
		BeanFactory factory = new ClassPathXmlApplicationContext("employee-beans.xml");
	    uas = (UserAccountService)factory.getBean("userAccountService");
	}
	
	public Employee findEmployeeByEmployeeFullName(String employeeFullName) {
		Person person = new Person();
		person.setFullName(employeeFullName);
		List<Person> list = uas.findPersonByExample(person, false);
		if (list.size() > 0) {
			person = list.get(0);
			return convertPersonToEmployee(person);
		}
		return null;
	}
	
	public Employee findEmployeeByEmployeeNumber(String employeeNumber) {
		Person person = new Person();
		person.setUserId(employeeNumber);
		List<Person> list = uas.findPersonByExample(person, false);
		if (list.size() > 0) {
			person = list.get(0);
			return convertPersonToEmployee(person);
		}
		return null;
	}
	
	public List<Employee> getAllTNEmployeesFromLdap() {
		Person person = new Person();
		List<Employee> employeeList = new ArrayList<Employee>();
		person.setFullName("*");
		List<Person> personList = uas.findPersonByExample(person, true);
		System.out.println("personList: "+personList.size());
		for (Person person2 : personList) {
			if(personWorksAtTekNat(person2)) {
				Employee employee = convertPersonToEmployee(person2);
				employeeList.add(employee);
				System.out.println(employee.getName() + " " + employee.getEmployeeId() + " " + employee.getGroupMembership().size());
			}
		}
		return employeeList;
	}
	
	private boolean personWorksAtTekNat(Person person){
		List<AffiliationData> affData = person.getAffiliationData();
		for (AffiliationData affiliationData : affData) {
			if(affiliationData instanceof EmployeeData) {
				EmployeeData ed = (EmployeeData) affiliationData;
				for (GroupData gd : ed.getGroup()) {
					if (gd.getDescription().contains("o-tn")) return true;
				}
			}
		}
		return false;
	}
	
	private Employee convertPersonToEmployee(Person person2) {
		Employee employee = new Employee();
		employee.setName(person2.getFullName());
		employee.setEmployeeId(person2.getUserId());
		
		List<String> gl = new ArrayList<String>();
		List<AffiliationData> affData = person2.getAffiliationData();
		for (AffiliationData affiliationData : affData) {
			if(affiliationData instanceof EmployeeData) {
				EmployeeData ed = (EmployeeData) affiliationData;
				for (GroupData gd : ed.getGroup()) {
					gl.add(gd.getDescription());
				}
			}
		}
		employee.setGroupMembership(gl);
		return employee;
	}

}
