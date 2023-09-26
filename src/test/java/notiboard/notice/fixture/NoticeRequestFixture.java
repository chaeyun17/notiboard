package notiboard.notice.fixture;

import java.util.List;
import notiboard.notice.dto.NoticeDto;
import notiboard.notice.dto.NoticeDto.Request;

public class NoticeRequestFixture {

  private NoticeDto.Request jsonBody;
  private List<String> fileNames;

  public NoticeRequestFixture(Request jsonBody, List<String> fileNames) {
    this.jsonBody = jsonBody;
    this.fileNames = fileNames;
  }

  public Request getJsonBody() {
    return jsonBody;
  }

  public void setJsonBody(Request jsonBody) {
    this.jsonBody = jsonBody;
  }

  public List<String> getFileNames() {
    return fileNames;
  }

  public void setFileNames(List<String> fileNames) {
    this.fileNames = fileNames;
  }

}
