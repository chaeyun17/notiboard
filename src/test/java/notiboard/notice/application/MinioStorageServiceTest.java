package notiboard.notice.application;

import static org.mockito.ArgumentMatchers.any;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import notiboard.config.minio.MinioConfig;
import notiboard.notice.domain.StorageType;
import notiboard.notice.domain.UploadFile;
import notiboard.notice.dto.UploadFileDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("[Service] MinIO 스토리지 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

  private FileStorageService fileStorageService;
  @Mock
  private MinioConfig minioConfig;
  @Mock
  private MinioClient minioClient;
  @Mock
  private GetObjectResponse response;

  @BeforeEach
  void setup() {
    Mockito.when(minioConfig.getBucket()).thenReturn("notice");
    this.fileStorageService = new MinioFileStorageService(minioClient, minioConfig);
  }

  @DisplayName("파일 저장")
  @Test
  void saveFile() {
    String originalFileName = "abc.txt";
    InputStream inputStream = new ByteArrayInputStream(new byte[10]);
    Long fileSize = 10L;
    UploadFileDto uploadFileDto = new UploadFileDto(originalFileName, inputStream, fileSize);

    UploadFile uploadFile = fileStorageService.saveFile(uploadFileDto);

    Assertions.assertThat(uploadFile.getFileSize()).isEqualTo(fileSize);
    Assertions.assertThat(uploadFile.getFileName()).isEqualTo(originalFileName);
    Assertions.assertThat(uploadFile.getFilePath()).isNotBlank();
  }

  @DisplayName("파일 데이터 읽기")
  @Test
  void getFileByStream() throws Exception {
    String originalFileName = "abc.txt";
    Long fileSize = 10L;
    UploadFile uploadFile = UploadFile.of(originalFileName, originalFileName, fileSize,
        StorageType.Minio);
    OutputStream outputStream = new ByteArrayOutputStream();
    Mockito.when(minioClient.getObject(any())).thenReturn(response);

    fileStorageService.getFileByStream(uploadFile, outputStream);
  }

  @DisplayName("파일 삭제")
  @Test
  void delete() {
    String originalFileName = "abc.txt";
    Long fileSize = 10L;
    UploadFile uploadFile = UploadFile.of(originalFileName, originalFileName, fileSize,
        StorageType.Minio);

    fileStorageService.delete(uploadFile);
  }
}