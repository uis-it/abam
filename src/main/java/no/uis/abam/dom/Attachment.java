package no.uis.abam.dom;

import java.io.File;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Transient;

@Entity
public class Attachment extends AbamType {

  private static final long serialVersionUID = 1L;
  @Transient
  private File file;
  
  @Basic(fetch=FetchType.LAZY)
  @Lob
  @Column(name="attachment")
  private byte[] data;

  
  public Attachment() {
  }
  
  public Attachment(String physicalPath) {
    file = new File(physicalPath);
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
