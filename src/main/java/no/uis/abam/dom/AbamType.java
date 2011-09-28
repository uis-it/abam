package no.uis.abam.dom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbamType implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "OID_", scale = 0)
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long oid;

  public Long getOid() {
    return oid;
  }

  public void setOid(Long oid) {
    this.oid = oid;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AbamType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (((AbamType)obj).getOid() == this.getOid()) {
      return true;
    }
    return false;
  }
  
  
}
