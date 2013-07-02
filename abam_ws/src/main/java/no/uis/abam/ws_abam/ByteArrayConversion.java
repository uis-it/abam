package no.uis.abam.ws_abam;

import net.sf.sojo.core.SimpleConversion;
import net.sf.sojo.core.reflect.ReflectionHelper;

public class ByteArrayConversion extends SimpleConversion {

  static {
    ReflectionHelper.addSimpleType(byte[].class);
  }
  
  public ByteArrayConversion() {
    super(byte[].class);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object convert(Object pvObject, Class pvToType) {
    return pvObject;
  }
  
}
