package notiboard.notice.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_POSTING_PERIOD;
import static notiboard.exception.ErrorCode.INVALID_INPUT_POSTING_PERIOD_IS_NULL;

import java.time.LocalDateTime;
import notiboard.exception.NotiboardException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("[Domain] 공지사항 기간 테스트")
class PostingPeriodTest {

  @ParameterizedTest
  @CsvSource({"2000-01-01T10:00:00,2000-01-01T09:00", "2023-09-25T01:00:00,2023-09-24T23:59:00"})
  @DisplayName("시작일시가 마감일시보다 늦을 수 없습니다")
  void ofTest(LocalDateTime openingTime, LocalDateTime closingTime) {
    Assertions.assertThatThrownBy(() -> {
      PostingPeriod.of(openingTime, closingTime);
    }).isInstanceOf(NotiboardException.class).hasMessage(INVALID_INPUT_POSTING_PERIOD.message);
  }

  @ParameterizedTest
  @CsvSource({"2000-01-01T10:00:00,", ",2023-09-24T23:59:00", ","})
  @DisplayName("시작일시가 마감일시는 둘 중 하나라도 Null이면 안됩니다")
  void ofTest2(LocalDateTime openingTime, LocalDateTime closingTime) {
    Assertions.assertThatThrownBy(() -> {
          PostingPeriod.of(openingTime, closingTime);
        }).isInstanceOf(NotiboardException.class)
        .hasMessage(INVALID_INPUT_POSTING_PERIOD_IS_NULL.message);
  }

}