package notiboard.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class NoticeUrlBuilder {

  private static String getBaseUrl() {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
  }

  public static String getDownloadUrl(Long attachmentId) {
    return getBaseUrl() + "/api/v1/notices/attachments/" + attachmentId + "/download";
  }

}
