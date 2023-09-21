package notiboard.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import notiboard.dao.AttachmentRepository;
import notiboard.domain.Attachment;
import notiboard.domain.Notice;
import notiboard.domain.UploadFile;
import notiboard.dto.UploadFileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentService {

  private final AttachmentRepository attachmentRepository;
  private final FileStorageService fileStorageService;

  @Transactional
  public List<Attachment> saveFiles(List<UploadFileDto> files, Notice notice) {
    return files.stream().map(file -> this.save(file, notice)).collect(
        Collectors.toList());
  }

  @Transactional
  public Attachment save(UploadFileDto fileDto, Notice notice) {
    UploadFile uploadFile = fileStorageService.saveFile(fileDto);
    Attachment attachment = Attachment.of(notice, uploadFile);
    return attachmentRepository.save(attachment);
  }


}
