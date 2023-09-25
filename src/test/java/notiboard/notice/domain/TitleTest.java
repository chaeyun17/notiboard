package notiboard.notice.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_TITLE;

import notiboard.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("제목 테스트")
class TitleTest {

  @DisplayName("제목 길이는 비어있거나 100자를 초과하면 안된다")
  @ParameterizedTest
  @ValueSource(strings = {"",
      "65qaanNQz8aLYt8VnVX2tSL8ZPj2oj9SENEwm69Wf4jxTx972qAzYtido2YSGAircVPL4KMu6cdRy9aR7LR6DEfwdVHqWGgdtxHjG",
      "StisUZxX63g2FiH7XFPfPiRziPRS5ntWn3rmsTbWA52FeBCCcZtJEusUvRxHtbKdPcUA23zCUPKzu9oZvyGRHrD9eMg7gQiiMnNGTb",
      ""})
  void ofTest(String input) {
    Assertions.assertThatThrownBy(() -> {
          Title.of(input);
        }).isInstanceOf(CustomException.class)
        .hasMessage(INVALID_INPUT_TITLE.message);
  }
}