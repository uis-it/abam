package no.uis.abam.ws_abam;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import no.uis.abam.dom.Assignment;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
@Repository
public class AbamDaoImpl implements AbamDao {

  private EntityManager em;
  
  @PersistenceContext(name="abamEmf", unitName="AbamAssignment")
  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  @Override
  public void saveAssignment(Assignment assignment) {
    em.persist(assignment);
  }

  @Override
  public void removeAssignment(Assignment assignment) {
    em.remove(assignment);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getAllAssignments() {
    Query query = em.createQuery("FROM Assignment");
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode) {
    Query query = em.createQuery("FROM Assignment c WHERE c.departmentCode = :departmentCode");
    query.setParameter("departmentCode", departmentCode);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getActiveAssignments() {
    Query query = em.createQuery("FROM Assignment c WHERE c.expireDate < :now");
    Calendar now = Calendar.getInstance();
    query.setParameter("now", now);
    return query.getResultList();
  }

  @Override
  public Assignment getAssignment(long id) {
    return em.find(Assignment.class, Long.valueOf(id));
  }
}
