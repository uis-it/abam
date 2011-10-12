package no.uis.abam.ws_abam;

import java.util.List;

import no.uis.abam.dom.Application;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.AssignmentType;

public interface AbamDao {

  void saveAssignment(Assignment assignment);

  void removeAssignment(Assignment assignment);

  List<Assignment> getAssignments();

  List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode);

  List<Assignment> getActiveAssignments();

  Assignment getAssignment(long id);

  List<Application> getApplications();

  List<Application> getApplicationsByDepartmentCode(String departmentCode, AssignmentType master);

  void saveApplication(Application application);

  void removeApplication(Application application);

}
