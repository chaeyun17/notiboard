package notiboard.ui;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import notiboard.application.NoticeService;
import notiboard.dto.NoticeDto;
import notiboard.error.CustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Void> create(
      @RequestPart(value = "notice") @Valid NoticeDto.Request request,
      @RequestPart List<MultipartFile> attachments) {
    request.addAttachments(attachments);
    Long id = noticeService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/notice/" + id)).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Map<String, String> errors = new HashMap<>();
    ex.getFieldErrors().forEach((fieldError) -> {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(errors)
        .message("입력값을 확인해주세요.")
        .httpStatus(httpStatus)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

}
