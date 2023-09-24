package notiboard.notice.application;

import static notiboard.exception.ErrorCode.INVALID_INPUT_UPLOAD_FILE_TOO_LARGE;
import static notiboard.exception.ErrorCode.INVALID_INPUT_UPLOAD_FILE_TOO_SMALL;
import static notiboard.exception.ErrorCode.NOT_FOUND_ATTACHMENT;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import notiboard.exception.CustomException;
import notiboard.notice.dao.AttachmentRepository;
import notiboard.notice.domain.Attachment;
import notiboard.notice.domain.Notice;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.AttachmentDto.Response;
import notiboard.notice.dto.UploadFileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentService {

  private static final int MAX_FILE_SIZE = 2 * 1024 * 1024;
  private final AttachmentRepository attachmentRepository;
  private final FileStorageService fileStorageService;

  @Transactional
  public List<Attachment> saveFiles(List<UploadFileDto> files, Notice notice) {
    files.forEach(this::validateFile);
    return files.stream().map(file -> this.save(file, notice)).collect(
        Collectors.toList());
  }

  @Transactional
  public Attachment save(UploadFileDto fileDto, Notice notice) {
    UploadFile uploadFile = fileStorageService.saveFile(fileDto);
    Attachment attachment = Attachment.of(notice, uploadFile);
    return attachmentRepository.save(attachment);
  }

  private void validateFile(UploadFileDto dto) {
    if (dto.getFileSize() > MAX_FILE_SIZE) {
      throw new CustomException(INVALID_INPUT_UPLOAD_FILE_TOO_LARGE);
    }
    if (dto.getFileSize() == 0) {
      throw new CustomException(INVALID_INPUT_UPLOAD_FILE_TOO_SMALL);
    }
  }


  public Response download(Long attachmentId, OutputStream outputStream) {
    Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow();
    fileStorageService.getFileByStream(attachment.getUploadFile(), outputStream);
    return new Response(attachment);
  }

  @Transactional
  public void deleteAttachments(List<Long> deleteAttachmentIds) {
    deleteAttachmentIds.forEach(this::deleteById);
  }

  @Transactional
  public void deleteByNotice(Notice notice) {
    List<Attachment> attachments = attachmentRepository.findAllByNotice(notice);
    attachments.stream().map(Attachment::getId).forEach(this::deleteById);
  }

  private void deleteById(Long id) {
    Attachment attachment = attachmentRepository.findById(id)
        .orElseThrow(() -> new CustomException(NOT_FOUND_ATTACHMENT));
    fileStorageService.delete(attachment.getUploadFile());
    attachment.delete();
    attachmentRepository.delete(attachment);
  }

}
