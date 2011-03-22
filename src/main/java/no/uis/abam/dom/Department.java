package no.uis.abam.dom;

import java.util.List;

import javax.persistence.Id;


public class Department {
	
	private static final long serialVersionUID = 1L;

	private short oe1 = 8; //Set to 8 because we only need TN-faculty for now.
	private short oe2;
	
	@Id
	private int oeSAPnr; 

	private String oeNSD_kode;
	private String oeNavn_Bokmaal;
	private String oeNavn_Engelsk;
	
	private String name;
	
	private List<StudyProgram> studyPrograms;
	
	public Department(){}
		
	public Department(Integer value, String name){
		oeSAPnr = value;
		oeNavn_Engelsk = name;
		oeNavn_Bokmaal = name;
		this.name = name;
	}
	
	public List<StudyProgram> getStudyPrograms() {
		return studyPrograms;
	}
	
	public void setStudyPrograms(List<StudyProgram> studyPrograms) {
		this.studyPrograms = studyPrograms;
	}

	public short getOe1() {
		return oe1;
	}

	public short getOe2() {
		return oe2;
	}

	public String getOeNSD_kode() {
		return oeNSD_kode;
	}

	public String getOeNavn_Bokmaal() {
		return oeNavn_Bokmaal;
	}

	public String getOeNavn_Engelsk() {
		return oeNavn_Engelsk;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getOeSAPnr() {
		return oeSAPnr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
