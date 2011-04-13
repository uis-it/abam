package no.uis.abam.ws_abam;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import no.uis.service.affiliation.AffiliationDataType;
import no.uis.service.person.PersonType;
import no.uis.service.person.ws.PersonService;
import no.uis.service.person.ws.PersonWebService;
import no.uis.service.student.AcademicAffiliationType;
import no.uis.service.student.StudentDataType;
import no.uis.service.student.TeachingLinkType;

import org.junit.Test;

public class TestWebservice {

	@Test
	public void runPersonWS() throws Exception {
		PersonWebService personWS = getWS();
		// PersonService ws = new PersonService();
		// PersonWebService personWS = ws.getPersonServicePort();
		PersonType person = personWS.getPersonByStudentNumber("202551");
		System.out.println(person.getAffiliationData());
		List<AffiliationDataType> test = person.getAffiliationData();
		for (AffiliationDataType affiliationDataType : test) {
			if(affiliationDataType instanceof StudentDataType) {
				StudentDataType sdt = (StudentDataType)affiliationDataType;
				List<AcademicAffiliationType> test2 = sdt.getAcademicAffiliation();
				for (AcademicAffiliationType academicAffiliationType : test2) {
					List<TeachingLinkType> test3 = academicAffiliationType.getTeachingLink();
					for (TeachingLinkType teachingLinkType : test3) {
						if (teachingLinkType.getRef().contains("BAC")) {
							System.out.println(teachingLinkType.getRef());
						}
					}
				}
			}
			System.out.println(affiliationDataType.getAffiliation().getClass());
			
		}
		assertNotNull(person);
	}
	
	private PersonWebService getWS() {
		//String targetUrl = "http://wsapps-test01.uis.no/ws-person/person";
		InputStream cfgStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties props = new Properties();
		try {
			props.load(cfgStream);
		} catch (IOException e) {
			System.out.println("Dette gikk ikke bra!!!");
			e.printStackTrace();
		}
		
		PersonService ps = new PersonService();
		PersonWebService port = ps.getPersonServicePort();
		
		String targetUrl = props.getProperty("personws.target.url");
		if (targetUrl != null) {
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					targetUrl);
		}

		return port;
	}

}
