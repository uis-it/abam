package no.uis.abam.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import no.uis.abam.dom.Department;
import no.uis.abam.dom.StudyProgram;

@Entity(name = "OrgEnhet")
public class DepartmentDAOImpl implements DepartmentDAO {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	public DepartmentDAOImpl(){
		
	}

	public List<Department> getDepartments() {
		Query query = entityManager.createQuery("from ");
		return query.getResultList();
	}
	
	public List<StudyProgram> getStudyPrograms(int departmentId) {
		//TODO Fix the query. 
		Query query = entityManager.createNamedQuery(" ");
		return query.getResultList();
	}

	
}
