package notiboard.member.acceptance;

import static notiboard.member.acceptance.step.MemberAcceptStep.토큰_발급_요청;
import static notiboard.member.acceptance.step.MemberAcceptStep.토큰_발급_확인;
import static notiboard.member.acceptance.step.MemberAcceptStep.회원가입_요청;
import static notiboard.member.acceptance.step.MemberAcceptStep.회원가입_응답_확인;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import notiboard.AcceptanceTest;
import notiboard.member.dto.MemberDto;
import notiboard.member.dto.MemberDto.Request;
import notiboard.member.dto.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("회원 인수테스트")
public class MemberAcceptanceTest extends AcceptanceTest {

  @DisplayName("회원 토큰 발급 시나리오")
  @Test
  void 회원가입_토큰_발급_시나리오() {

    // given
    MemberDto.Request 회원가입정보 = new Request("chaeyun1", "pw123", "채윤1");
    // when
    ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청(회원가입정보);
    // then
    회원가입_응답_확인(회원가입_응답);

    // given
    TokenDto.Request 토큰_발급_요청_데이터 = new TokenDto.Request(회원가입정보.getUsername(),
        회원가입정보.getPassword());
    // when
    ExtractableResponse<Response> 토큰_발급_응답 = 토큰_발급_요청(토큰_발급_요청_데이터);
    // then
    토큰_발급_확인(토큰_발급_응답);
  }

}
