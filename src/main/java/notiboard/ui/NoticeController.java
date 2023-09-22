package notiboard.ui;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import notiboard.application.AttachmentService;
import notiboard.application.NoticeService;
import notiboard.dto.AttachmentDto;
import notiboard.dto.NoticeDto;
import notiboard.dto.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

  private final NoticeService noticeService;
  private final AttachmentService attachmentService;

  @GetMapping("/search")
  public ResponseEntity<Page<NoticeDto.Response>> search(
      @RequestParam(required = false) SearchType searchType,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) LocalDateTime from,
      @RequestParam(required = false) LocalDateTime to,
      @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC)
      Pageable pageable
  ) {
    Page<NoticeDto.Response> notices = noticeService.search(searchType, keyword, from, to,
        pageable);
    return ResponseEntity.ok(notices);
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

}
