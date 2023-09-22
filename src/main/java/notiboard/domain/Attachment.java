package notiboard.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notiboard.util.NoticeUrlBuilder;

@Getter
@Entity
@Table(name = "attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notice_id",
      foreignKey = @ForeignKey(name = "fk_attachment_to_tb_notice"))
  private Notice notice;

  @Embedded
  private UploadFile uploadFile;

  private Attachment(Notice notice, UploadFile uploadFile) {
    this.notice = notice;
    this.uploadFile = uploadFile;
  }

  public static Attachment of(Notice notice, UploadFile uploadFile) {
    return new Attachment(notice, uploadFile);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attachment that = (Attachment) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }


  public String getDownloadUrl() {
    return NoticeUrlBuilder.getDownloadUrl(this.id);
  }

}
