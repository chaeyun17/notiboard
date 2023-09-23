package notiboard.member.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import notiboard.member.dto.MemberDto;
import notiboard.member.dto.TokenDto;
import notiboard.member.dto.TokenDto.Request;
import notiboard.testutils.RestAssuredUtils;
import org.springframework.http.HttpStatus;

public class MemberAcceptStep {

  private static final String 토큰_발급_요청_주소 = "/api/v1/auth/token";
  private static final String 회원가입_요청_주소 = "/api/v1/auth/signup";

  public static ExtractableResponse<Response> 회원가입_요청(MemberDto.Request 회원가입_요청_데이터) {
    return RestAssuredUtils.post(회원가입_요청_주소, 회원가입_요청_데이터);
  }

  public static void 회원가입_응답_확인(ExtractableResponse<Response> 회원가입_응답) {
    assertThat(회원가입_응답.response().statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  public static ExtractableResponse<Response> 토큰_발급_요청(TokenDto.Request 토큰_발급_요청_데이터) {
    return RestAssuredUtils.post(토큰_발급_요청_주소, 토큰_발급_요청_데이터);
  }

  public static TokenDto.Response 토큰_발급_확인(ExtractableResponse<Response> 토큰_발급_응답) {
    TokenDto.Response 토큰_응답_데이터 = 토큰_발급_응답.as(TokenDto.Response.class);
    assertAll(
        () -> assertThat(토큰_발급_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
        () -> assertThat(토큰_응답_데이터.getAccessToken()).isNotBlank()
    );
    return 토큰_응답_데이터;
  }

  public static void 회원가입(MemberDto.Request 회원가입_요청_데이터) {
    ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청(회원가입_요청_데이터);
    회원가입_응답_확인(회원가입_응답);
  }

  public static TokenDto.Response 토큰_발급(MemberDto.Request 회원정보) {
    TokenDto.Request 토큰_발급_요청_데이터 = new Request(회원정보.getUsername(), 회원정보.getPassword());
    ExtractableResponse<Response> 토큰_발급_응답 = 토큰_발급_요청(토큰_발급_요청_데이터);
    return 토큰_발급_확인(토큰_발급_응답);
  }

}
