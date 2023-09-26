package notiboard.member.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notiboard.member.dto.MemberDto.Request;
import notiboard.notice.domain.AuditEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class Member extends AuditEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @AttributeOverride(name = "username", column = @Column(name = "username", unique = true))
  private Username username;

  @Embedded
  private Password password;

  @Embedded
  private Nickname nickname;

  @Column
  private boolean deleted = false;

  private Member(Username username, Password password, Nickname nickname) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
  }

  public static Member of(Request request, PasswordEncoder passwordEncoder) {
    return new Member(Username.of(request.getUsername()),
        Password.ofEncrpyt(request.getPassword(), passwordEncoder),
        Nickname.of(request.getNickname()));
  }

  public static Member of(Username username, Password password, Nickname nickname) {
    return new Member(username, password, nickname);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public String getPassword() {
    return this.password.toText();
  }

  @Override
  public String getUsername() {
    return this.username.toText();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return !this.deleted;
  }
}
