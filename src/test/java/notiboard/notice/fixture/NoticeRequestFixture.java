package notiboard.notice.fixture;

import notiboard.notice.dto.NoticeDto;
import notiboard.notice.dto.NoticeDto.Request;

public class NoticeRequestFixture {

  private NoticeDto.Request jsonBody;
  private String fileName;

  public NoticeRequestFixture(Request jsonBody, String fileName) {
    this.jsonBody = jsonBody;
    this.fileName = fileName;
  }

  public Request getJsonBody() {
    return jsonBody;
  }

  public void setJsonBody(Request jsonBody) {
    this.jsonBody = jsonBody;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

}
