package no.uis.abam.dom;

import java.util.List;
import javax.persistence.*;


@Entity(name = "Department")
@Table(name = "OrgEnhet")
public class Department {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "oeSAPnr")
	private int oeSAPnr; 
	
	@Column(name = "oe1")
	private short oe1; //Should be 8 because we only need TN-faculty for now.
	@Column(name = "oe2")
	private short oe2;
	@Column(name = "oe3")
	private short oe3;
	@Column(name = "oeInstKode")
	private short oeInstKode;
	@Column(name = "oeNivaa")
	private short oeNivaa;
	@Column(name = "oeInstKodeParent", columnDefinition="SMALLINT")
	private short oeInstKodeParent;
	@Column(name = "oe1Parent")
	private short oe1Parent;
	@Column(name = "oe2Parent")
	private short oe2Parent;
	@Column(name = "oe3Parent")
	private short oe3Parent;
	@Column(name = "oeSortering")
	private short oeSortering;
	
	@Column(name = "oeNSD_kode")
	private String oeNSD_kode;
	@Column(name = "oeNavn_Bokmaal")
	private String oeNavn_Bokmaal;
	@Column(name = "oeNavn_Nynorsk")
	private String oeNavn_Nynorsk;
	@Column(name = "oeNavn_Engelsk")
	private String oeNavn_Engelsk;
	@Column(name = "oeKode")
	private String oeKode;
	@Column(name = "oeURL")
	private String oeURL;
	@Column(name = "oeURL_Engelsk")
	private String oeURL_Engelsk;
	@Column(name = "oeMerknad")
	private String oeMerknad;
	
	@Transient
	private String name;
	
	@Transient
	private List<StudyProgram> studyPrograms;
	
	public Department(){}
		
	public Department(Integer value, String name){
		oeSAPnr = value;
		oeNavn_Engelsk = name;
		oeNavn_Bokmaal = name;
		this.name = name;
	}

	public int getOeSAPnr() {
		return oeSAPnr;
	}

	public void setOeSAPnr(int oeSAPnr) {
		this.oeSAPnr = oeSAPnr;
	}

	public short getOe1() {
		return oe1;
	}

	public void setOe1(short oe1) {
		this.oe1 = oe1;
	}

	public short getOe2() {
		return oe2;
	}

	public void setOe2(short oe2) {
		this.oe2 = oe2;
	}

	public short getOe3() {
		return oe3;
	}

	public void setOe3(short oe3) {
		this.oe3 = oe3;
	}

	public short getOeInstKode() {
		return oeInstKode;
	}

	public void setOeInstKode(short oeInstKode) {
		this.oeInstKode = oeInstKode;
	}

	public short getOeNivaa() {
		return oeNivaa;
	}

	public void setOeNivaa(short oeNivaa) {
		this.oeNivaa = oeNivaa;
	}

	public short getOeInstKodeParent() {
		return oeInstKodeParent;
	}

	public void setOeInstKodeParent(short oeInstKodeParent) {
		this.oeInstKodeParent = oeInstKodeParent;
	}

	public short getOe1Parent() {
		return oe1Parent;
	}

	public void setOe1Parent(short oe1Parent) {
		this.oe1Parent = oe1Parent;
	}

	public short getOe2Parent() {
		return oe2Parent;
	}

	public void setOe2Parent(short oe2Parent) {
		this.oe2Parent = oe2Parent;
	}

	public short getOe3Parent() {
		return oe3Parent;
	}

	public void setOe3Parent(short oe3Parent) {
		this.oe3Parent = oe3Parent;
	}

	public short getOeSortering() {
		return oeSortering;
	}

	public void setOeSortering(short oeSortering) {
		this.oeSortering = oeSortering;
	}

	public String getOeNSD_kode() {
		return oeNSD_kode;
	}

	public void setOeNSD_kode(String oeNSD_kode) {
		this.oeNSD_kode = oeNSD_kode;
	}

	public String getOeNavn_Bokmaal() {
		return oeNavn_Bokmaal;
	}

	public void setOeNavn_Bokmaal(String oeNavn_Bokmaal) {
		this.oeNavn_Bokmaal = oeNavn_Bokmaal;
	}

	public String getOeNavn_Nynorsk() {
		return oeNavn_Nynorsk;
	}

	public void setOeNavn_Nynorsk(String oeNavn_Nynorsk) {
		this.oeNavn_Nynorsk = oeNavn_Nynorsk;
	}

	public String getOeNavn_Engelsk() {
		return oeNavn_Engelsk;
	}

	public void setOeNavn_Engelsk(String oeNavn_Engelsk) {
		this.oeNavn_Engelsk = oeNavn_Engelsk;
	}

	public String getOeKode() {
		return oeKode;
	}

	public void setOeKode(String oeKode) {
		this.oeKode = oeKode;
	}

	public String getOeURL() {
		return oeURL;
	}

	public void setOeURL(String oeURL) {
		this.oeURL = oeURL;
	}

	public String getOeURL_Engelsk() {
		return oeURL_Engelsk;
	}

	public void setOeURL_Engelsk(String oeURL_Engelsk) {
		this.oeURL_Engelsk = oeURL_Engelsk;
	}

	public String getOeMerknad() {
		return oeMerknad;
	}

	public void setOeMerknad(String oeMerknad) {
		this.oeMerknad = oeMerknad;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StudyProgram> getStudyPrograms() {
		return studyPrograms;
	}

	public void setStudyPrograms(List<StudyProgram> studyPrograms) {
		this.studyPrograms = studyPrograms;
	}

}
