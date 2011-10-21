package no.uis.abam.ws_abam;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.sojo.core.ComplexConversion;
import net.sf.sojo.core.IConverter;
import net.sf.sojo.core.IConverterExtension;
import net.sf.sojo.core.NonCriticalExceptionHandler;
import net.sf.sojo.core.conversion.ComplexBean2MapConversion;
import net.sf.sojo.core.reflect.Property;
import net.sf.sojo.core.reflect.ReflectionHelper;
import net.sf.sojo.core.reflect.ReflectionPropertyHelper;
import net.sf.sojo.util.Util;
import no.uis.abam.dom.Assignment;
import no.uis.abam.dom.Employee;

/**
 * This class allows SOJO to convert and load lazy loaded Collections.
 * 
 * @author Martin Goldhahn
 */
public class LazyLoadConversion extends ComplexConversion {

  
  public LazyLoadConversion() {
  }
  
  @SuppressWarnings("rawtypes")
  @Override
  public Object convert(Object pvObject, Class pvToType, IConverterExtension pvConverterExtension) {
    Class<?> propClass = findTargetClass(pvObject);
    Map lvGetterMap = ReflectionPropertyHelper.getAllGetterProperties(propClass, null);
    Object target = ReflectionHelper.createNewIteratableInstance(propClass, lvGetterMap.size()); 
    Iterator it = lvGetterMap.entrySet().iterator();
    return super.iterate(pvObject, target, it, pvConverterExtension);
  }

  private Class<?> findTargetClass(Object pvObject) {
    if (pvObject == null) {
      return null;
    }
    if (ProxyFactory.isProxyClass(pvObject.getClass())) {
      ProxyObject po = (ProxyObject)pvObject;
      MethodHandler poHandler = po.getHandler();
      if (poHandler instanceof JavassistLazyInitializer) {
        JavassistLazyInitializer jli = (JavassistLazyInitializer)poHandler;
        return jli.getImplementation().getClass();
      }
    }
    Package targetPackage = pvObject.getClass().getPackage();
    if (targetPackage != null && targetPackage.getName().equals("no.uis.abam.dom")) {
      return pvObject.getClass();
    }
    return null;
  }

  @Override
  protected Object[] doTransformIteratorObject2KeyValuePair(Object pvIteratorObject) {
    @SuppressWarnings("unchecked")
    Map.Entry<String, Method> entry = (Entry<String, Method>)pvIteratorObject;
    return new Object[] {entry.getKey(), entry.getValue()};
  }

  @Override
  protected Object[] doConvert(Object pvSourceObject, Object pvNewTargetObject, Object pvKey, Object pvValue,
      IConverter pvConverter)
  {
    String propName = (String) pvKey;
    Object lvNewValue = null;
    if (Util.getKeyWordClass().equals(propName) == false) {
      Object lvValue = null;
      AccessibleObject lvAccessibleObject = null;
          try {
            lvAccessibleObject = (AccessibleObject) pvValue;
            lvValue = new Property(lvAccessibleObject).executeGetValue(pvSourceObject);
            lvNewValue = pvConverter.convert(lvValue);

          } catch (Exception e) {
            if (NonCriticalExceptionHandler.isNonCriticalExceptionHandlerEnabled()) {
              NonCriticalExceptionHandler.handleException(LazyLoadConversion.class, e, "Problem by invoke get from property: " + lvAccessibleObject);
            }
          }
        
    } 
    return new Object [] { pvKey, lvNewValue };
  }

  @Override
  protected void doAddObject(Object pvSourceObject, Object pvNewTargetObject, Object pvKey, Object pvValue,
      int pvIteratorPosition)
  {
    if (pvKey != null && pvKey.equals("class")) {
      return;
    }
    if (pvNewTargetObject == null) {
      return;
    }
    @SuppressWarnings("unchecked")
    Map<String, Method> setterMap = ReflectionPropertyHelper.getAllSetterProperties(pvNewTargetObject.getClass(), null);
    AccessibleObject lvAccessibleObject = null;
    try {
      lvAccessibleObject = setterMap.get(pvKey);
      new Property(lvAccessibleObject).executeSetValue(pvNewTargetObject, pvValue);
    } catch(Exception e) {
      if (NonCriticalExceptionHandler.isNonCriticalExceptionHandlerEnabled()) {
        NonCriticalExceptionHandler.handleException(LazyLoadConversion.class, e, "Problem by invoke set on property: " + lvAccessibleObject);
      }
    }
  }

  @Override
  public boolean isAssignableFrom(Object pvObject) {
    Class<?> targetType = findTargetClass(pvObject);
    if (targetType != null) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean isAssignableTo(Class pvToType) {
    return false;
  }
}