package notiboard.notice.application;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import notiboard.notice.domain.StorageType;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;
import notiboard.notice.util.FileNameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@ConditionalOnProperty(
    value = "app.fileStorageProvider",
    havingValue = "LOCAL"
)
public class LocalFileStorageService implements FileStorageService {

  public static final Path FILE_STORAGE_PATH = Paths.get("files").toAbsolutePath();
  private static final int MAX_LENGTH_FILE_BASE_NAME = 50;

  @Transactional
  @Override
  public UploadFile saveFile(UploadFileDto dto) {
    String saveFileName = dto.getOriginalFileName();
    Path filePath = FILE_STORAGE_PATH.resolve(
        FileNameUtils.buildUniqueFileName(dto.getOriginalFileName(), MAX_LENGTH_FILE_BASE_NAME));
    try {
      Files.createDirectories(FILE_STORAGE_PATH);
      Files.createFile(filePath);
      Files.write(filePath, dto.getInputStream().readAllBytes());
    } catch (IOException e) {
      deleteFile(filePath);
      throw new RuntimeException(e);
    }
    return UploadFile.of(filePath.toAbsolutePath().toString(), saveFileName, dto.getFileSize(),
        StorageType.LOCAL);
  }

  @Override
  public void getFileByStream(UploadFile uploadFile, OutputStream outputStream) {
    Path path = Path.of(uploadFile.getFilePath());
    try {
      outputStream.write(Files.readAllBytes(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UploadFile uploadFile) {
    Path file = Path.of(uploadFile.getFilePath());
    try {
      Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void deleteFile(Path filePath) {
    try {
      Files.delete(filePath);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
