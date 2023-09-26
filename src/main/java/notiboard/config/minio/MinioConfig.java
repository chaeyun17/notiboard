package notiboard.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import notiboard.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Getter
@ConditionalOnProperty(
    value = "app.fileStorageProvider",
    havingValue = "MINIO"
)
public class MinioConfig {

  private final String bucket;
  private final String accessKey;
  private final String secretKey;
  private final String endpoint;

  @Autowired
  public MinioConfig(AppProperties appProperties) {
    this.bucket = appProperties.getMinio().getBucket();
    this.accessKey = appProperties.getMinio().getAccessKey();
    this.secretKey = appProperties.getMinio().getSecretKey();
    this.endpoint = appProperties.getMinio().getEndpoint();
  }

  @Bean
  public MinioClient minioClient() {
    MinioClient client = new MinioClient.Builder().credentials(accessKey, secretKey)
        .endpoint(endpoint).build();
    try {
      boolean existsBucket = client.bucketExists(
          BucketExistsArgs.builder().bucket(bucket).build());
      if (!existsBucket) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
      }
    } catch (ErrorResponseException | InsufficientDataException | InternalException |
             InvalidKeyException | InvalidResponseException | IOException |
             NoSuchAlgorithmException | ServerException | XmlParserException e) {
      throw new RuntimeException(e);
    }
    return client;
  }

}
