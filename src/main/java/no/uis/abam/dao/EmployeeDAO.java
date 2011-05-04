package no.uis.abam.dao;

import java.util.List;

import no.uis.abam.dom.Employee;

public interface EmployeeDAO {
	public List<Employee> getAllTNEmployeesFromLdap();
	public Employee findEmployeeByEmployeeNumber(String employeeNumber);
	public Employee findEmployeeByEmployeeFullName(String employeeFullName);
}
