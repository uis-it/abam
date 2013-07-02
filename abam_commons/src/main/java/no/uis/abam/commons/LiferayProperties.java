package no.uis.abam.commons;

import java.util.Properties;

import com.liferay.portal.kernel.util.PortalClassInvoker;

public class LiferayProperties {

  private static final String _METHOD_GET_PROPERTIES = "getProperties";
  private static final String _CLASS = "com.liferay.portal.util.PropsUtil";

  private Properties properties;
  
  public LiferayProperties() throws Exception {
    Object returnObj = PortalClassInvoker.invoke(
      _CLASS, _METHOD_GET_PROPERTIES, false);

    if (returnObj != null) {
      this.properties = (Properties)returnObj;
    }
  }
  
  public LiferayProperties(String prefix, boolean removePrefix) throws Exception {
    Object returnObj = PortalClassInvoker.invoke(
      _CLASS, _METHOD_GET_PROPERTIES, prefix, Boolean.valueOf(removePrefix), false);
    
    if (returnObj != null) {
      this.properties = (Properties)returnObj;
    }
  }
  
  public Properties getProperties() {
    return this.properties;
  }
  
}
