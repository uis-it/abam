package no.uis.abam.dom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbamType implements Serializable, Comparable<AbamType> {

  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "OID_", scale = 0, nullable=false)
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

  @Override
  public int compareTo(AbamType o) {
    if (!getClass().equals(o.getClass())) {
      throw new IllegalArgumentException();
    }
    Long thisOid = oid;
    Long thatOid = o.getOid();
    if (thisOid == null) {
      thisOid = (long)this.hashCode();
    }
    if (thatOid == null) {
      thatOid = (long)o.hashCode();
    }
    return thisOid.compareTo(thatOid);
  }
}
