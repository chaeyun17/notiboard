package notiboard.member.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_PASSWORD_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import notiboard.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("비밀번호 테스트")
class PasswordTest {

  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setup() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @ParameterizedTest
  @ValueSource(strings = {"pass1234", "word1234", "%2fk6@#46", "123412323"})
  @DisplayName("암호화 테스트")
  void encryptTest(String raw) {
    Password password = Password.ofEncrpyt(raw, passwordEncoder);

    assertThat(passwordEncoder.matches(raw, password.toText())).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"abcd", "abc", "a", "9PnGR8a3gQqZFDjjZu9Sc8Rjk6rsbRGdsTcAjT8pNQfUStwXsp1",
      ""})
  @DisplayName("비밀번호는 5자 미만 및 50자 초과면 안된다")
  public void ofTest1(String input) {
    assertThatThrownBy(() -> {
      Password.ofEncrpyt(input, passwordEncoder);
    }).isInstanceOf(CustomException.class)
        .hasMessage(INVALID_INPUT_PASSWORD_SIZE.message);
  }

  @ParameterizedTest
  @ValueSource(strings = {"abcde", "9PnGR8a3gQqZFDjjZu9Sc8Rjk6rsbRGdsTcAjT8pNQfUStwXsp"})
  @DisplayName("비밀번호는 5자에서 50자여야 한다.")
  public void ofTest2(String input) {
    Password password = Password.ofEncrpyt(input, passwordEncoder);

    assertThat(password.toText()).isNotBlank();
  }
}