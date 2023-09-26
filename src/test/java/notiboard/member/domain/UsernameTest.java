package notiboard.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import notiboard.exception.NotiboardException;
import notiboard.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("아이디 Username 테스트")
class UsernameTest {

  @ParameterizedTest
  @ValueSource(strings = {"", "abce", "95XQZw5tV7A9FpM5sr5sE", "a",
      "UZ9MwDvSKVDAsRc36r33F7c725NNR"})
  @DisplayName("아이디는 5자 미만 및 20자 초과면 에러를 발생시킨다")
  void ofTest1(String input) {
    assertThatThrownBy(() -> {
      Username.of(input);
    }).isInstanceOf(NotiboardException.class)
        .hasMessage(ErrorCode.INVALID_INPUT_USERNAME_SIZE.message);
  }

  @ParameterizedTest
  @ValueSource(strings = {"L8FWi", "chaeyun1", "abcde", "9MJnwsda6hpjAud3UKG4"})
  @DisplayName("아이디는 5자에서 20자여야 한다")
  void ofTest2(String input) {
    Username username = Username.of(input);
    assertThat(username).isNotNull();
  }

}