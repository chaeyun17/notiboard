package notiboard.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import notiboard.domain.StorageType;
import notiboard.domain.UploadFile;
import notiboard.dto.UploadFileDto;
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

  @Override
  public UploadFile saveFile(UploadFileDto dto) {
    String saveFileName = generateFileName(dto.getOriginalFileName());
    Path filePath = FILE_STORAGE_PATH.resolve(saveFileName);
    try {
      Files.createDirectories(FILE_STORAGE_PATH);
      Files.createFile(filePath);
      Files.write(filePath, dto.getInputStream().readAllBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return UploadFile.of(filePath, saveFileName, dto.getFileSize(), StorageType.LOCAL);
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
