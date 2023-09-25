package notiboard.notice.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_CONTENT;

import notiboard.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

@DisplayName("본문 테스트")
class ContentTest {

  @DisplayName("본문 길이는 비어있으면 안된다")
  @RepeatedTest(5)
  void ofTest() {
    String content = "";
    
    Assertions.assertThatThrownBy(() -> {
          Content.of(content);
        }).isInstanceOf(CustomException.class)
        .hasMessage(INVALID_INPUT_CONTENT.message);
  }
}