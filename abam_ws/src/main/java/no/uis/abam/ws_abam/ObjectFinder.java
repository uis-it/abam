package no.uis.abam.ws_abam;

import no.uis.abam.dom.AbamType;

import org.springframework.orm.jpa.JpaTemplate;

public abstract class ObjectFinder<T extends AbamType> {
  private final JpaTemplate jpa;

  public ObjectFinder(JpaTemplate jpa) {
    this.jpa = jpa;
  }
  
  public T findOrCreate(T sourceObject) {
    T targetObject = null;
    if (sourceObject.getOid() == null) {
      // not previously persisted
    
      targetObject = findObject(sourceObject);
      if (targetObject == null) {
        targetObject = jpa.merge(sourceObject);
      } 
    } else {
      targetObject = jpa.merge(sourceObject);
    }
    return targetObject;
  }
  
  protected abstract T findObject(T source);
}