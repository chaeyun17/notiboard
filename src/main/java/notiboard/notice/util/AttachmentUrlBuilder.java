package notiboard.notice.util;

import notiboard.notice.ui.AttachmentController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class AttachmentUrlBuilder {

  private static String getBaseUrl() {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
  }

  public static String getDownloadUrl(Long attachmentId) {
    return getBaseUrl() + AttachmentController.BASE_URL + "/" + attachmentId + "/download";
  }

}
