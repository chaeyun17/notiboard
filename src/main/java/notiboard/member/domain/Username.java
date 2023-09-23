package notiboard.member.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import notiboard.exception.CustomException;
import notiboard.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Username {

  public static final int MAX_LENGTH = 20;
  public static final int MIN_LENGTH = 5;


  private String username;

  public static Username of(String username) {
    validate(username);
    return new Username(username);
  }

  private static void validate(String input) {
    if (StringUtils.isBlank(input) || input.length() < MIN_LENGTH || input.length() > MAX_LENGTH) {
      throw new CustomException(ErrorCode.INVALID_INPUT_USERNAME_SIZE);
    }
  }

  public String toText() {
    return this.username;
  }

}
