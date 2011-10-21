package no.uis.abam.dom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.io.IOUtils;

@Entity(name="Attachment")
@Inheritance(strategy=InheritanceType.JOINED)
public class Attachment extends AbamType {

  private static final long serialVersionUID = 1L;

  @Transient
  private File file;
  
  @Basic(fetch=FetchType.LAZY)
  @Lob
  private byte[] data;
  
  public Attachment() {
  }
  
  public Attachment(String physicalPath) {
    file = new File(physicalPath);
  }

  public File getFile() {
    if (file == null && data != null) {
      File tmpFile;
      FileOutputStream fos = null;
      try {
        tmpFile = File.createTempFile("abam", ".tmp");
        fos = new FileOutputStream(tmpFile);
        IOUtils.write(data, fos);
        file = tmpFile;
      } catch(IOException e) {
      } finally {
        IOUtils.closeQuietly(fos);
      }
    }
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public byte[] getData() {
    if (data == null && file != null && file.exists()) {
      try {
        FileInputStream fi = new FileInputStream(file);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        IOUtils.copyLarge(fi, bo);
        data = bo.toByteArray();
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
