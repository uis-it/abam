package no.uis.abam.commons;

import com.icesoft.faces.context.ByteArrayResource;

public final class AttachmentResource extends ByteArrayResource {
  private static final long serialVersionUID = 1L;
  private final String fileName;
  private final String contentType;

  public AttachmentResource(byte[] content, String fileName, String contentType) {
    super(content);
    this.fileName = fileName;
    this.contentType = contentType;
  }

  public String getFileName() {
    return fileName;
  }

  public String getContentType() {
    return contentType;
  }
}