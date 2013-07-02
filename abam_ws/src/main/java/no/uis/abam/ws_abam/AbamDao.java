package no.uis.abam.ws_abam;

import java.util.Calendar;
import java.util.List;

import no.uis.abam.dom.AbamGroup;
import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;
import no.uis.abam.dom.Employee;
import no.uis.abam.dom.Student;
import no.uis.abam.dom.Thesis;

public interface AbamDao {

  void saveAssignment(Assignment assignment);

  void removeAssignment(Assignment assignment);

  List<Assignment> getAssignments();

  List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode);

  List<Assignment> getActiveAssignments();

  Assignment getAssignment(long id);

  List<Application> getApplications();

  List<Application> getApplicationsByDepartmentCode(String departmentCode, AssignmentType master);

  Application saveApplication(Application application);

  void removeApplication(Application application);

  Thesis saveThesis(Thesis thesis);

  Student saveStudent(Student student);

  List<Thesis> getThesisesAfterEvaluationDeadline(Calendar cal, String departmentCode, String employeeId);

  List<Thesis> getThesisesBeforeEvaluationDeadline(Calendar cal, String departmentCode);

  AbamGroup findOrCreateGroup(String placeRef);

  Employee findOrCreateEmployee(String userId, String fullName, String email, String phone);

  Student findOrCreateStudent(String userId, String firstName, String email);
}
