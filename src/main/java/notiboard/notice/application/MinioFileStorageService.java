package notiboard.notice.application;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import notiboard.config.minio.MinioConfig;
import notiboard.notice.domain.StorageType;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;
import notiboard.notice.util.FileNameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@ConditionalOnBean(MinioConfig.class)
public class MinioFileStorageService implements FileStorageService {

  private static final int MAX_LENGTH_FILE_BASE_NAME = 50;
  private final MinioClient minioClient;
  private final MinioConfig minioConfig;

  @Override
  public UploadFile saveFile(UploadFileDto dto) {
    String objectFileName = FileNameUtils.buildUniqueFileName(dto.getOriginalFileName(),
        MAX_LENGTH_FILE_BASE_NAME);
    try {
      minioClient.putObject(PutObjectArgs.builder()
          .bucket(minioConfig.getBucket())
          .object(objectFileName)
          .stream(dto.getInputStream(), dto.getFileSize(), -1)
          .build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException |
             InvalidKeyException | InvalidResponseException | IOException |
             NoSuchAlgorithmException | ServerException | XmlParserException e) {
      throw new RuntimeException(e);
    }
    return UploadFile.of(objectFileName, dto.getOriginalFileName(), dto.getFileSize(),
        StorageType.Minio);
  }

  @Override
  public void getFileByStream(UploadFile uploadFile, OutputStream outputStream) {
    try {
      GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
          .bucket(minioConfig.getBucket())
          .object(uploadFile.getFilePath())
          .build());
      response.transferTo(outputStream);
    } catch (ErrorResponseException | InsufficientDataException | InternalException |
             InvalidKeyException | InvalidResponseException | IOException |
             NoSuchAlgorithmException | ServerException | XmlParserException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void delete(UploadFile uploadFile) {
    try {
      minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucket())
          .object(uploadFile.getFilePath()).build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException |
             InvalidKeyException | InvalidResponseException | IOException |
             NoSuchAlgorithmException | ServerException | XmlParserException e) {
      throw new RuntimeException(e);
    }
  }


}
