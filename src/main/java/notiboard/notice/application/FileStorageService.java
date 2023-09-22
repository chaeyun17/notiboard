package notiboard.notice.application;

import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;

public interface FileStorageService {

  UploadFile saveFile(UploadFileDto dto);

  void delete(UploadFile uploadFile);
}
