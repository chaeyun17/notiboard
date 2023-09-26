package notiboard.member.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_NICKNAME_SIZE;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import notiboard.exception.NotiboardException;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

  public static final int MAX_LENGTH = 50;
  public static final int MIN_LENGTH = 3;

  private String nickname;

  public static Nickname of(String input) {
    validate(input);
    return new Nickname(input);
  }

  private static void validate(String input) {
    if (StringUtils.isBlank(input) || input.length() < MIN_LENGTH || input.length() > MAX_LENGTH) {
      throw new NotiboardException(INVALID_INPUT_NICKNAME_SIZE);
    }
  }

  public String toText() {
    return this.nickname;
  }


}
