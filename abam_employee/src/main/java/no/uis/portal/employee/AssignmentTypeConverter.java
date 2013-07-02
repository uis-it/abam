package no.uis.portal.employee;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.uis.abam.dom.AssignmentType;

public class AssignmentTypeConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
    return AssignmentType.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
    return ((AssignmentType)value).name();
  }
}
