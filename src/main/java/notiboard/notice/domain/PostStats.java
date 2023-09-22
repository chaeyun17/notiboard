package notiboard.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "post_stats")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post_stats SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class PostStats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Long viewCount;

  @Column
  private boolean deleted = false;

  private PostStats(Long viewCount) {
    this.viewCount = viewCount;
  }

  public static PostStats of() {
    return new PostStats(0L);
  }

  public void increaseViewCnt() {
    this.viewCount++;
  }
}
