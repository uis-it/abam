package no.uis.abam.dom;

import java.util.Comparator;

public class AssigmentIdComparator implements Comparator<Assignment> {

	public int compare(Assignment arg0, Assignment arg1) {
		if (arg0.getId() > arg1.getId()) return 1;
		else if (arg0.getId() < arg1.getId()) return -1;
		else return 0; 
	}

}
