package notiboard.notice.acceptance;

import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_검색_요청;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_상세_조회_요청;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_상세_조회_응답_확인;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_생성_요청;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_생성_응답_확인;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_수정_요청;
import static notiboard.notice.acceptance.step.NoticeAcceptStep.공지사항_수정_응답_확인;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import notiboard.AcceptanceTest;
import notiboard.member.acceptance.step.MemberAcceptStep;
import notiboard.member.dto.MemberDto;
import notiboard.member.dto.MemberDto.Request;
import notiboard.member.dto.TokenDto;
import notiboard.notice.acceptance.step.NoticeAcceptStep;
import notiboard.notice.dto.NoticeDto;
import notiboard.notice.fixture.NoticeRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("[Acceptance] 공지사항 인수테스트")
public class NoticeAcceptanceTest extends AcceptanceTest {

  private TokenDto.Response 토큰;
  private LocalDateTime now;

  @BeforeEach
  void setup() {
    // 회원가입
    MemberDto.Request 채윤_가입_요청 = new Request("chaeyun2", "admin123", "채윤2");
    MemberAcceptStep.회원가입(채윤_가입_요청);
    // 로그인 및 토큰 발급
    토큰 = MemberAcceptStep.토큰_발급(채윤_가입_요청);
    now = LocalDateTime.of(2023, 9, 26, 10, 0, 0);
    Mockito.when(timeUtils.now()).thenReturn(now);
  }

  @Test
  @DisplayName("공지사항 관리 시나리오")
  public void 공지사항_관리_시나리오() {
    String title = "추석을 맞아하여 임직원분들께";
    String content = "안녕하세요 기업 임직원 여러분! 따뜻한 마음을 주고 받는 한가위 되시길 바랍니다.";
    LocalDateTime openingTime = now.minusDays(1);
    LocalDateTime closingTime = now.plusDays(3);
    String 공지사항_생성_요청_첨부파일 = "one.txt";
    String 공지사항_생성_요청_첨부파일2 = "two.txt";
    NoticeDto.Request 공지사항_데이터 = new NoticeDto.Request(title, content, openingTime,
        closingTime, null, null);
    NoticeRequestFixture 공지사항_생성_요청_데이터 = new NoticeRequestFixture(공지사항_데이터,
        List.of(공지사항_생성_요청_첨부파일, 공지사항_생성_요청_첨부파일2));

    // 공지사항 생성
    ExtractableResponse<Response> 공지사항_생성_응답 = 공지사항_생성_요청(공지사항_생성_요청_데이터, 토큰);
    Long 공지사항_식별자 = 공지사항_생성_응답_확인(공지사항_생성_응답);

    // 공지사항 상세 조회
    ExtractableResponse<Response> 공지사항_상세_조회_응답 = 공지사항_상세_조회_요청(공지사항_식별자, 토큰);
    NoticeDto.Response 공지사항_상세_조회_응답_데이터 = 공지사항_상세_조회_응답_확인(공지사항_상세_조회_응답, 공지사항_식별자,
        공지사항_생성_요청_데이터);

    // 공지사항 수정
    NoticeDto.Request 공지사항_수정_데이터 = new NoticeDto.Request(
        공지사항_상세_조회_응답_데이터.getTitle() + "_",
        공지사항_상세_조회_응답_데이터.getContent() + "_",
        공지사항_상세_조회_응답_데이터.getOpeningTime().plusHours(1),
        공지사항_상세_조회_응답_데이터.getClosingTime().plusHours(1),
        null,
        null
    );
    String 공지사항_수정_요청_첨부파일 = "two.txt";
    NoticeRequestFixture 공지사항_수정_요청_데이터 = new NoticeRequestFixture(공지사항_수정_데이터,
        List.of(공지사항_수정_요청_첨부파일));
    ExtractableResponse<Response> 공지사항_수정_응답 = 공지사항_수정_요청(공지사항_식별자,
        공지사항_수정_요청_데이터, 토큰);
    NoticeDto.Response 공지사항_수정_응답_데이터 = 공지사항_수정_응답_확인(공지사항_수정_응답, 공지사항_수정_요청_데이터,
        공지사항_식별자);

    // 공지사항 검색
    ExtractableResponse<Response> 공지사항_검색_응답 = 공지사항_검색_요청();
    NoticeAcceptStep.공지사항_검색_응답_확인(공지사항_검색_응답, 공지사항_수정_응답_데이터);

    // 공지사항 삭제
    ExtractableResponse<Response> 공지사항_삭제_응답 = NoticeAcceptStep.공지사항_삭제_요청(공지사항_식별자, 토큰);
    NoticeAcceptStep.공지사항_삭제_응답_확인(공지사항_삭제_응답);
  }


}
