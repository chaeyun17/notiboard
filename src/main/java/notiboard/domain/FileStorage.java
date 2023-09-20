package notiboard.domain;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class FileStorage {

  private String filePath;

  private String fileName;

  private StorageType storageType;


}
