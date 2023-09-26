package notiboard.notice.domain;


import static notiboard.exception.ErrorCode.INVALID_INPUT_CONTENT;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import notiboard.exception.NotiboardException;
import org.apache.commons.lang3.StringUtils;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

  @Lob
  @Column(columnDefinition = "LONGTEXT")
  private String content;

  private Content(String content) {
    this.content = content;
  }

  public static Content of(String content) {
    validate(content);
    return new Content(content);
  }

  private static void validate(String input) {
    if (StringUtils.isBlank(input)) {
      throw new NotiboardException(INVALID_INPUT_CONTENT);
    }
  }

  public String toText() {
    return this.content;
  }

}
