package notiboard.notice.application;

import java.io.OutputStream;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;

public interface FileStorageService {

  UploadFile saveFile(UploadFileDto dto);

  void getFileByStream(UploadFile uploadFile, OutputStream outputStream);

  void delete(UploadFile uploadFile);
}
