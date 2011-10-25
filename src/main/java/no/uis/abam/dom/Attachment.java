package no.uis.abam.dom;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity(name="Attachment")
@Inheritance(strategy=InheritanceType.JOINED)
public class Attachment extends AbamType {

  private static final long serialVersionUID = 1L;

  @Basic(fetch=FetchType.LAZY)
  @Lob
  private byte[] data;
  
  private String fileName;
  
  private String contentType;
  
  public Attachment() {
  }
  
  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
