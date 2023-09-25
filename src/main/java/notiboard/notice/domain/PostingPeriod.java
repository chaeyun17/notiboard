package notiboard.notice.domain;

import static notiboard.exception.ErrorCode.INVALID_INPUT_POSTING_PERIOD;
import static notiboard.exception.ErrorCode.INVALID_INPUT_POSTING_PERIOD_IS_NULL;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notiboard.exception.CustomException;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingPeriod {

  private LocalDateTime openingTime;

  private LocalDateTime closingTime;

  private PostingPeriod(LocalDateTime openingTime, LocalDateTime closingTime) {
    this.openingTime = openingTime;
    this.closingTime = closingTime;
  }

  public static PostingPeriod of(LocalDateTime openingTime, LocalDateTime closingTime) {
    validate(openingTime, closingTime);
    return new PostingPeriod(openingTime, closingTime);
  }

  private static void validate(LocalDateTime openingTime, LocalDateTime closingTime) {
    if (Objects.isNull(openingTime) || Objects.isNull(closingTime)) {
      throw new CustomException(INVALID_INPUT_POSTING_PERIOD_IS_NULL);
    }
    if (openingTime.isAfter(closingTime) || openingTime.isEqual(closingTime)) {
      throw new CustomException(INVALID_INPUT_POSTING_PERIOD);
    }
  }

}
