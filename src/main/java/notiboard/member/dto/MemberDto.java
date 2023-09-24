package notiboard.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.member.domain.Member;

public class MemberDto implements Serializable {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request implements Serializable {

    @NotNull(message = "아이디는 필수 입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하여야 합니다.")
    private String username;

    @Size(min = 5, max = 50, message = "비밀번호는 5자 이상 20자 이하여야 합니다.")
    @NotNull(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotNull(message = "닉네임은 필수입니다.")
    @Size(min = 3, max = 50, message = "닉네임은 3~50 글자여야 합니다.")
    private String nickname;

  }

  @Data
  @NoArgsConstructor
  public static class Response implements Serializable {

    private Long id;
    private String username;
    private String nickname;

    public Response(Member member) {
      this.id = member.getId();
      this.username = member.getUsername();
      this.nickname = member.getNickname().toText();
    }

  }

}
