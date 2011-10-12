package no.uis.abam.ws_abam;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.sojo.common.ObjectGraphWalker;
import net.sf.sojo.common.WalkerInterceptor;
import net.sf.sojo.core.AbstractConversion;
import net.sf.sojo.core.Conversion;
import net.sf.sojo.core.Converter;
import net.sf.sojo.core.conversion.Iterateable2IterateableConversion;
import net.sf.sojo.interchange.Serializer;
import net.sf.sojo.interchange.json.JsonSerializer;
import no.uis.abam.dom.AbamPerson;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Attachment;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Supervisor;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
@Repository
public class AbamDaoImpl extends JpaDaoSupport implements AbamDao {

  private static Logger log = Logger.getLogger(AbamDaoImpl.class);
  
  @Override
  public void saveAssignment(Assignment assignment) {
    JpaTemplate jpa = getJpaTemplate();
    
    // author
    AbamPerson author = assignment.getAuthor();
    if (author.getOid() == null) {
      // not previously persisted
    
      if (Employee.class.isAssignableFrom(author.getClass())) {
        Employee authorEmployee = findEmpoyeeByEmpoyeeNumber(((Employee)author).getEmployeeId());
        if (authorEmployee == null) {
          jpa.persist(author);
        } else {
          assignment.setAuthor(authorEmployee);
        }
      } else {
        throw new NotImplementedException(getClass());
      }
    } else {
      jpa.merge(author);
    }
    
    // faculty supervisor
    Employee supervisor = assignment.getFacultySupervisor();
    if (supervisor.getOid() == null) {
      Employee sv = findEmpoyeeByEmpoyeeNumber(supervisor.getEmployeeId());
      if (sv == null) {
        jpa.persist(supervisor);
      } else {
        assignment.setFacultySupervisor(sv);
      }
    } else {
      jpa.merge(supervisor);
    }
    
    // supervisor list
    Iterator<Supervisor> svIter = assignment.getSupervisorList().iterator();
    List<Supervisor> svAdded = new LinkedList<Supervisor>();
    while (svIter.hasNext()) {
      Supervisor sv = svIter.next();
      if (sv.getOid() == null) {
        @SuppressWarnings("unchecked")
        List<Supervisor> svfind = jpa.find("select s from Supervisor s where s.name=?", sv.getName());
        if (svfind.size() == 1) {
          svIter.remove();
          svAdded.add(svfind.get(0));
        } else if (svfind.size() > 1) {
          throw new NotImplementedException(getClass());
        }
      }
    }
    assignment.getSupervisorList().addAll(svAdded);
    
    if (assignment.getOid() != null) {
      assignment = jpa.merge(assignment);
    }
    
    jpa.persist(assignment);
    
    jpa.flush();
  }

  @SuppressWarnings("unchecked")
  private Employee findEmpoyeeByEmpoyeeNumber(String employeeId) {
    List<Employee> employees = getJpaTemplate().find("select e from Employee e where e.employeeId = ?", employeeId);
    if (employees == null || employees.isEmpty()) {
      return null;
    }
    if (employees.size() != 1) {
      throw new NotImplementedException(getClass());
    }
    return employees.get(0);
  }

  @Override
  public void removeAssignment(Assignment assignment) {
    getJpaTemplate().remove(assignment);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getAssignments() {
    
    List<Assignment> assignments = getJpaTemplate().find("FROM Assignment");
    return assignments;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode) {
    List<Assignment> assignments = getJpaTemplate().find("FROM Assignment c fetch all properties WHERE c.departmentCode = ?", departmentCode);
    loadEntity(assignments);
    return assignments;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Assignment> getActiveAssignments() {
    Calendar now = Calendar.getInstance();
    List<Assignment> resultList = getJpaTemplate().find("select a FROM Assignment a fetch all properties WHERE a.expireDate > ?", now);
    loadEntity(resultList);
    return resultList;
  }

  @Override
  public Assignment getAssignment(long id) {
    Assignment assignment = getJpaTemplate().find(Assignment.class, Long.valueOf(id));
    return assignment;
  }

  
  @SuppressWarnings("unchecked")
  @Override
  public List<Application> getApplications() {
    List<Application> applications = getJpaTemplate().find("SELECT a FROM Application a fetch all properties");
    loadEntity(applications);
    return applications;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Application> getApplicationsByDepartmentCode(String departmentCode, AssignmentType assignmentType) {
    List<Application> applications = getJpaTemplate().find("SELECT a from Application a fetch all properties WHERE a.assignment.departmentCode=? AND a.assignment.type=?", departmentCode, assignmentType);
    loadEntity(applications);
    return applications;
  }

  @Override
  public void saveApplication(Application application) {
    AbamPerson author = application.getAssignment().getAuthor();
    if (author != null && author.getOid()!= null) {
      // there is an error when fetching abampersons that are actually Employees
      try {
        AbamPerson authorDb = getJpaTemplate().find(AbamPerson.class, author.getOid());
        if (!authorDb.getClass().isAssignableFrom(AbamPerson.class)) {
          application.getAssignment().setAuthor(authorDb);
        }
      } catch (Exception e) {
        log.error("", e);
      }
    }
    if (application.getApplicationDate() == null) {
      application.setApplicationDate(Calendar.getInstance());
    }
    Application appl = getJpaTemplate().merge(application);
    getJpaTemplate().persist(appl);
    getJpaTemplate().flush();
  }

  @Override
  public void removeApplication(Application application) {
    getJpaTemplate().merge(application);
    getJpaTemplate().remove(application);
  }

  private void loadEntity(Object entity) {
    try {
      Converter converter = new Converter();
      converter.addConversion(new LazyLoadConversion());
      converter.addConversion(new Iterateable2IterateableConversion());
      converter.convert(entity);
    } catch(Exception e) {
      log.error("entity " + entity, e);
    }
  }
}
