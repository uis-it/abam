package no.uis.abam.ws_abam;

import java.util.List;

import no.uis.abam.dom.Assignment;

public interface AbamDao {

  void saveAssignment(Assignment assignment);

  void removeAssignment(Assignment assignment);

  List<Assignment> getAllAssignments();

  List<Assignment> getAssignmentsFromDepartmentCode(String departmentCode);

  List<Assignment> getActiveAssignments();

  Assignment getAssignment(long id);

}
