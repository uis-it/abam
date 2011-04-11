package no.uis.abam.dom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThesisStatus {

	private String status; 
	private String responsible;
	
	private Date date;
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public ThesisStatus(String status, String responsible) {
		this.status = status;
		this.responsible = responsible;
		this.date = createDate();
	}
	
	private Date createDate() {
		return Calendar.getInstance().getTime();
	}

	public String getStatus() {
		return status;
	}

	public String getResponsible() {
		return responsible;
	}

	public Date getDate() {
		return date;
	}
	
	public String getDateAsString() {
		return simpleDateFormatter.format(date);
	}
}
