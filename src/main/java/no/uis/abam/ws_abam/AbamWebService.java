package no.uis.abam.ws_abam;

import java.util.Set;

import javax.jws.WebService;
import no.uis.abam.dom.Assignment;

@WebService
public interface AbamWebService {
	public Set<Assignment> getAllAssignments();
	public void saveAssignment(Assignment assignment);
	
}
