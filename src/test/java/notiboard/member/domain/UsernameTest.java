package notiboard.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("아이디 테스트")
class UsernameTest {

  @ParameterizedTest
  @ValueSource(strings = {"user123"})
  @DisplayName("아이디는 5자 이상 20자 이하여야 한다")
  void of() {
  }

  @Test
  void toText() {
  }
}