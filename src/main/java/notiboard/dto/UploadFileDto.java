package notiboard.dto;

import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileDto {

  private String originalFileName;
  private InputStream inputStream;
  private Long fileSize;

  public UploadFileDto(MultipartFile multipartFile) {
    this.originalFileName = multipartFile.getOriginalFilename();
    try {
      this.inputStream = multipartFile.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.fileSize = multipartFile.getSize();
  }

}
