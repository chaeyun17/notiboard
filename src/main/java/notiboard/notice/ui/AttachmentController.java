package notiboard.notice.ui;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import notiboard.notice.application.AttachmentService;
import notiboard.notice.dto.AttachmentDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttachmentController.BASE_URL)
public class AttachmentController {

  public static final String BASE_URL = "/api/v1/attachments";
  private final AttachmentService attachmentService;

  @PreAuthorize("@policyChecker.permitAnonymous()")
  @GetMapping("/{id}/download")
  public void downloadAttachment(@PathVariable("id") Long attachmentId,
      HttpServletResponse response) {
    OutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    AttachmentDto.Response dto = attachmentService.download(attachmentId, outputStream);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=" + dto.getFileName());
  }

}
