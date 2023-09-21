package notiboard.application;

import notiboard.domain.UploadFile;
import notiboard.dto.UploadFileDto;

public interface FileStorageService {

  UploadFile saveFile(UploadFileDto dto);

}
