package notiboard.notice.util;

import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class FileNameUtils {

  public static String buildUniqueFileName(String fileName, int maxLengthOriginalFilename) {
    String extension = FilenameUtils.getExtension(fileName);
    String baseName = FilenameUtils.getBaseName(fileName);
    if (baseName.length() > maxLengthOriginalFilename) {
      baseName = StringUtils.substring(baseName, 0, maxLengthOriginalFilename);
    }
    return baseName + "_" + StringUtils.substring(UUID.randomUUID().toString(), 0, 5) + "."
        + extension;
  }

}
