package no.uis.abam.dom;

@Deprecated
public class StudyProgram {

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	public StudyProgram(){}

	public StudyProgram(Integer value, String name){
		id = value;
		this.name = name;		
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
