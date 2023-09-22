package notiboard.application;

import static notiboard.error.ErrorCode.INVALID_INPUT_UPLOAD_FILE_TOO_LARGE;
import static notiboard.error.ErrorCode.INVALID_INPUT_UPLOAD_FILE_TOO_SMALL;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import notiboard.dao.AttachmentRepository;
import notiboard.domain.Attachment;
import notiboard.domain.Notice;
import notiboard.domain.UploadFile;
import notiboard.dto.AttachmentDto;
import notiboard.dto.AttachmentDto.Response;
import notiboard.dto.UploadFileDto;
import notiboard.error.CustomException;
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
    Path path = Path.of(attachment.getUploadFile().getFilePath());
    try {
      outputStream.write(Files.readAllBytes(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new AttachmentDto.Response(attachment);
  }
}
