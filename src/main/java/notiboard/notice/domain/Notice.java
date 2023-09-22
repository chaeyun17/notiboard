package notiboard.notice.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notiboard.notice.dto.NoticeDto.Request;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Getter
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE notice SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class Notice extends AuditEntity {


  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = Title.MAX_LENGTH, nullable = false)
  @Embedded
  private Title title;

  @Column(nullable = false)
  @Embedded
  private Content content;

  @Column(nullable = false)
  @Embedded
  private PostingPeriod postingPeriod;

  @Column(nullable = false)
  private boolean deleted = false;

  @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY)
  private List<Attachment> attachments = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "post_stats_id",
      foreignKey = @ForeignKey(name = "fk_notice_to_post_stats"))
  private PostStats postStats;

  private Notice(Title title, Content content, PostingPeriod postingPeriod, PostStats postStats) {
    this.title = title;
    this.content = content;
    this.postingPeriod = postingPeriod;
    this.postStats = postStats;
  }

  public static Notice of(Request request) {
    return new Notice(Title.of(request.getTitle()), Content.of(request.getContent()),
        PostingPeriod.of(request.getOpeningTime(), request.getClosingTime()), PostStats.of());
  }

  public void addAttachments(List<Attachment> attachments) {
    this.attachments.addAll(attachments);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Notice notice = (Notice) o;
    return id != null && Objects.equals(id, notice.id);
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }

  public void modify(Request request) {
    this.title = Title.of(request.getTitle());
    this.content = Content.of(request.getContent());
    this.postingPeriod = PostingPeriod.of(request.getOpeningTime(), request.getClosingTime());
  }

  public void addAttachment(Attachment attachment) {
    this.attachments.add(attachment);
  }

  public void removeAttachment(Attachment attachment) {
    this.attachments.remove(attachment);
  }

  public void increaseViewCnt() {
    this.postStats.increaseViewCnt();
  }

}
