package no.uis.abam.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class AbamGroup extends AbamType {

  private static final long serialVersionUID = 1L;

  public AbamGroup() {
  }
  
  public AbamGroup(String groupName) {
   this.name = groupName; 
  }
  
  @Column(name="_NAME")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
