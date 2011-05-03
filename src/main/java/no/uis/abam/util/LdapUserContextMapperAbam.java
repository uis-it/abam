package no.uis.abam.util;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.DirContextOperations;

import no.uis.abam.dom.Employee;
import no.uis.service.model.Email;
import no.uis.service.model.EmployeeData;
import no.uis.service.model.GroupData;
import no.uis.service.model.Person;
import no.uis.service.model.StudentData;
import no.uis.service.model.TypeOfAffiliation;
import no.uis.service.model.TypeOfEmail;
import no.uis.service.useraccount.impl.LdapUserContextMapper;

public class LdapUserContextMapperAbam extends LdapUserContextMapper{

	@Override
	protected Object doMapFromContext(DirContextOperations context) {
		Person person =  new Person();
		EmployeeData ed = null;
//		StudentData sd = null;
		
		Attributes attributes = context.getAttributes();
		
		person.setFullName(getAttributeValue(attributes, "displayName"));
		person.setUserId(getAttributeValue(attributes, "userId"));
		person.setFirstName("test@woho");
		
		ed = new EmployeeData();
		ed.setAffiliation(TypeOfAffiliation.EMPLOYEE);
		ed.setGroup(getGroupAttributeValue(attributes, "securityEquals"));
		ed.setEmployeeNumber(getAttributeValue(attributes, "workforceID"));
		person.getAffiliationData().add(ed);
		return person;
	}
	private List<GroupData> getGroupAttributeValue(Attributes attributes,
			String name) {
		List<GroupData> groupData = new ArrayList<GroupData>();
		Attribute attribute = attributes.get(name);
		try {
			NamingEnumeration<String> attributeEnum = (NamingEnumeration<String>) attribute.getAll(); 
			while(attributeEnum.hasMore()) {
				
				String var = attributeEnum.next();
				String[] varSplit = var.split(",");
				for (String string : varSplit) {
					if(string.contains("cn=")) {
						GroupData gd = new GroupData();
						gd.setDescription(string.replace("cn=", ""));
						groupData.add(gd);
					}
				}
			}
			return groupData;
		} catch (NamingException e) {
			return null;
		} catch (NullPointerException npe){
			return null;
		}
	}
	private String getAttributeValue (Attributes attributes, String name) {
		
		Attribute attribute = attributes.get(name);
		try {
			return attribute.get().toString();
			
		} catch (NamingException e) {
			return null;
		} catch (NullPointerException npe){
			return null;
		}
	}
	private boolean containsValue(Attributes attributes, String name, String value) {
		
		Attribute attribute = attributes.get(name);
		return null == attribute ? false : attribute.contains(value);
	}
}
