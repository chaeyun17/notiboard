package notiboard.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

  @Lob
  private String content;

  private Content(String content) {
    this.content = content;
  }

  public static Content of(String title) {
    return new Content(title);
  }

  public String toText() {
    return this.content;
  }

}
