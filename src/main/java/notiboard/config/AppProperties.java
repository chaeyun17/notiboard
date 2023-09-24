package notiboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app")
public class AppProperties {

  private Minio minio = new Minio();
  private Jwt jwt = new Jwt();
  private String fileStorageProvider;

  @Data
  public static class Minio {

    private String accessKey;
    private String secretKey;
    private String bucket;
  }

  @Data
  public static class Jwt {

    private String secretKey;
    private int expirationMs;
  }

}
