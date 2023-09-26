package notiboard.member.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_PASSWORD_SIZE;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import notiboard.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

  public static final int MAX_LENGTH = 50;
  public static final int MIN_LENGTH = 5;

  private String password;

  public static Password ofEncrpyt(String raw, PasswordEncoder passwordEncoder) {
    validate(raw);
    return new Password(passwordEncoder.encode(raw));
  }

  public static Password of(String encrypted) {
    return new Password(encrypted);
  }

  private static void validate(String raw) {
    if (StringUtils.isBlank(raw) || raw.length() < MIN_LENGTH || raw.length() > MAX_LENGTH) {
      throw new CustomException(INVALID_INPUT_PASSWORD_SIZE);
    }
  }

  public String toText() {
    return this.password;
  }

}
