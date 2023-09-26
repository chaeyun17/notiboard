package notiboard.notice.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_TITLE;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import notiboard.exception.NotiboardException;
import org.apache.commons.lang3.StringUtils;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

  public static final int MAX_LENGTH = 100;

  private String title;

  private Title(String title) {
    this.title = title;
  }

  public static Title of(String title) {
    validate(title);
    return new Title(title);
  }

  private static void validate(String input) {
    if (StringUtils.isBlank(input) || input.length() > MAX_LENGTH) {
      throw new NotiboardException(INVALID_INPUT_TITLE);
    }
  }

  public String toText() {
    return this.title;
  }

}
