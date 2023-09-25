package notiboard.notice.util;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

  public LocalDateTime now() {
    return LocalDateTime.now();
  }

}
