package notiboard.domain;

import static notiboard.error.ErrorCode.INVALID_INPUT_TITLE;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import notiboard.error.CustomException;
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

  public static void validate(String input) {
    if (StringUtils.isBlank(input) || input.length() > 100) {
      throw new CustomException(INVALID_INPUT_TITLE);
    }
  }

  public String toText() {
    return this.title;
  }

}
