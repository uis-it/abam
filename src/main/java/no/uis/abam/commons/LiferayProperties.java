package no.uis.abam.commons;

import java.util.Properties;

import com.liferay.portal.kernel.util.PortalClassInvoker;

public class LiferayProperties {

  private static final String _METHOD_GET_PROPERTIES = "getProperties";
  private static final String _CLASS = "com.liferay.portal.util.PropsUtil";

  public static Properties getProperties() throws Exception {
    Object returnObj = PortalClassInvoker.invoke(
      _CLASS, _METHOD_GET_PROPERTIES, false);

    if (returnObj != null) {
      return (Properties)returnObj;
    } else {
      return null;
    }
  }
  
  public static Properties getProperties(String prefix, boolean removePrefix) throws Exception {
    Object returnObj = PortalClassInvoker.invoke(
      _CLASS, _METHOD_GET_PROPERTIES, prefix, Boolean.valueOf(removePrefix), false);
    
    if (returnObj != null) {
      return (Properties)returnObj;
    } else {
      return null;
    }
  }
  
}
