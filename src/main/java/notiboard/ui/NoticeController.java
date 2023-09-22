package notiboard.ui;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import notiboard.application.AttachmentService;
import notiboard.application.NoticeService;
import notiboard.dto.AttachmentDto;
import notiboard.dto.NoticeDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

  private final NoticeService noticeService;
  private final AttachmentService attachmentService;

  @GetMapping("/attachments/{id}/download")
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


  @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Void> create(
      @RequestPart(value = "notice") @Valid NoticeDto.Request request,
      @RequestPart List<MultipartFile> attachments) {
    request.addAttachments(attachments);
    Long id = noticeService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/notices/" + id)).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<NoticeDto.Response> findById(@PathVariable Long id) {
    NoticeDto.Response response = noticeService.findById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<NoticeDto.Response> deleteById(@PathVariable Long id) {
    noticeService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
