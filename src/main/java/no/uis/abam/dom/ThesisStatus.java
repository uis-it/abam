package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="ThesisStatus")
@Inheritance(strategy=InheritanceType.JOINED)
public class ThesisStatus extends AbamType {

  private static final long serialVersionUID = 1L;
	
  @Enumerated(EnumType.STRING)
	private ThesisStatusType status;
  
	private String responsible;
	
	private Calendar date;
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	public ThesisStatus() {
	}
	
	public ThesisStatus(ThesisStatusType status, String responsible) {
		this.status = status;
		this.responsible = responsible;
		this.date = Calendar.getInstance();
	}
	
	public ThesisStatusType getStatus() {
		return status;
	}

	public String getResponsible() {
		return responsible;
	}

	public Calendar getDate() {
		return date;
	}
	
	/**
	 * @deprecated move to commons.
	 * @return
	 */
	@Deprecated
	public String getDateAsString() {
		return simpleDateFormatter.format(date);
	}

	public void setStatus(ThesisStatusType status) {
		this.status = status;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
