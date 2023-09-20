package notiboard.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    return new PostingPeriod(openingTime, closingTime);
  }

}
