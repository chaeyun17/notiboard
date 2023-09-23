package notiboard.notice.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import notiboard.notice.domain.StorageType;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@Transactional(readOnly = true)
public class LocalFileStorageService implements FileStorageService {

  public static final Path FILE_STORAGE_PATH = Paths.get("files").toAbsolutePath();
  private static final int MAX_LENGTH_FILE_BASE_NAME = 50;

  @Transactional
  @Override
  public UploadFile saveFile(UploadFileDto dto) {
    String saveFileName = dto.getOriginalFileName();
    Path filePath = FILE_STORAGE_PATH.resolve(generateFileName(dto.getOriginalFileName()));
    try {
      Files.createDirectories(FILE_STORAGE_PATH);
      Files.createFile(filePath);
      Files.write(filePath, dto.getInputStream().readAllBytes());
    } catch (IOException e) {
      deleteFile(filePath);
      throw new RuntimeException(e);
    }
    return UploadFile.of(filePath, saveFileName, dto.getFileSize(), StorageType.LOCAL);
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

  private String generateFileName(String fileName) {
    String extension = FilenameUtils.getExtension(fileName);
    String baseName = FilenameUtils.getBaseName(fileName);
    if (baseName.length() > MAX_LENGTH_FILE_BASE_NAME) {
      baseName = StringUtils.substring(baseName, 0, MAX_LENGTH_FILE_BASE_NAME);
    }
    return baseName + "_" + StringUtils.substring(UUID.randomUUID().toString(), 0, 5) + "."
        + extension;
  }
}
