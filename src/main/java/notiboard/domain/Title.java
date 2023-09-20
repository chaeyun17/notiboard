package notiboard.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

  public static final int MAX_LENGTH = 100;

  private String title;

  private Title(String title) {
    this.title = title;
  }

  public static Title of(String title) {
    return new Title(title);
  }

  public String toText() {
    return this.title;
  }

}
