package no.uis.abam.dao;

import java.util.List;

import no.uis.abam.dom.Department;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DepartmentDAOTest {

	private static DepartmentDAO dao;

	@BeforeClass
	public static void init() {
		// instantiate bean context
		BeanFactory context = new ClassPathXmlApplicationContext("classpath:test-beans.xml");
		dao = (DepartmentDAO) context.getBean("departmentDAO");
	}
	
	@Test
	public void assureGetDepartmentsReturnsSomething() throws Exception {
		List<Department> departments = dao.getDepartments();
		System.out.println(departments.size());
		for (Department department : departments) {
			System.out.println("index: " + department.getOe2() + " " + department.getOeNavn_Engelsk());
		}
		Assert.assertNotNull(departments);
		Assert.assertFalse(departments.isEmpty());
		
	}
}
