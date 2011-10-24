package no.uis.abam.commons;

import java.util.List;

import no.uis.service.model.BaseText;

public final class BaseTextUtil {

  public static String getText(List<BaseText> txtList, String lang) {
    if (lang != null) {
      for (BaseText txt : txtList) {
        if (txt.getLang().equals(lang)) {
          return txt.getValue();
        }
      }
    }
    return txtList.get(0).getValue();
  }

  
  
  // not instantiable
  private BaseTextUtil() {}
}
