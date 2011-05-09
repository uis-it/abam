package no.uis.abam.dao;

import java.util.List;
import javax.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;
import no.uis.abam.dom.Department;
import no.uis.abam.dom.StudyProgram;

//@Entity(name = "OrgEnhet")
@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)

public class DepartmentDAOImpl implements DepartmentDAO {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	public DepartmentDAOImpl(){
		
	}

	@SuppressWarnings("unchecked")
	public List<Department> getDepartments() {
		Query query = entityManager.createQuery(
				"FROM no.uis.abam.dom.Department " +
				"WHERE oe1=8 AND oeNivaa=2 " +
				"AND oeNavn_Engelsk LIKE 'Department%'"
		);
		return query.getResultList();
	}
	
	public List<StudyProgram> getStudyPrograms(int departmentId) {
		//TODO Fix the query and move to a new class
		Query query = entityManager.createNamedQuery(" ");
		return query.getResultList();
	}
	
	@PersistenceContext(name="departmentEmf", unitName="no.uis.abam.dom")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
