package notiboard.notice.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

  private static final int MAX_LENGTH_FILE_BASE_NAME = 50;

  private String fileName;

  private String filePath;

  private Long fileSize;

  @Enumerated(EnumType.STRING)
  private StorageType storageType;

  private UploadFile(String filePath, String fileName, Long fileSize, StorageType storageType) {
    this.filePath = filePath;
    this.fileName = fileName;
    this.storageType = storageType;
    this.fileSize = fileSize;
  }

  public static UploadFile of(Path savedFile, String saveFileName, Long fileSize,
      StorageType storageType) {
    if (savedFile == null) {
      throw new IllegalArgumentException("저장되지 않은 경로입니다.");
    }
    return new UploadFile(savedFile.toAbsolutePath().toString(), saveFileName, fileSize,
        storageType);
  }


}
