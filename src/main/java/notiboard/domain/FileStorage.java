package notiboard.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class FileStorage {

  private String filePath;

  private String fileName;

  @Enumerated(EnumType.STRING)
  private StorageType storageType;


}
