package notiboard.notice.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import notiboard.member.dto.TokenDto;
import notiboard.notice.dto.AttachmentDto;
import notiboard.notice.dto.NoticeDto;
import notiboard.notice.fixture.NoticeRequestFixture;
import notiboard.testutils.RestAssuredUtils;
import org.springframework.http.HttpStatus;

public class NoticeAcceptStep {

  private static final String 공지사항_리소스_주소 = "/api/v1/notices";

  public static ExtractableResponse<Response> 공지사항_생성_요청(NoticeRequestFixture 생성_요청_데이터,
      TokenDto.Response 토큰) {
    return RestAssuredUtils.postWithFiles(공지사항_리소스_주소,
        토큰.getAccessToken(),
        "notice",
        생성_요청_데이터.getJsonBody(), "attachments", 생성_요청_데이터.getFileNames());
  }

  public static Long 공지사항_생성_응답_확인(
      ExtractableResponse<Response> 공지사항_생성_응답) {
    assertThat(공지사항_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    Long id = Long.parseLong(공지사항_생성_응답.header("location").split("/")[4]);
    assertThat(id).isNotNull();
    return id;
  }

  public static ExtractableResponse<Response> 공지사항_상세_조회_요청(Long id, TokenDto.Response 토큰) {
    return RestAssuredUtils.get(공지사항_리소스_주소 + "/" + id, 토큰.getAccessToken());
  }

  public static NoticeDto.Response 공지사항_상세_조회_응답_확인(ExtractableResponse<Response> 공지사항_상세_조회_응답,
      Long 공지사항_식별자, NoticeRequestFixture 공지사항_생성_요청_데이터) {
    assertThat(공지사항_상세_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    NoticeDto.Response 응답_데이터 = 공지사항_상세_조회_응답.as(NoticeDto.Response.class);
    assertThat(응답_데이터.getId()).isEqualTo(공지사항_식별자);
    assertThat(응답_데이터.getTitle()).isEqualTo(공지사항_생성_요청_데이터.getJsonBody().getTitle());
    assertThat(응답_데이터.getContent()).isEqualTo(공지사항_생성_요청_데이터.getJsonBody().getContent());
    assertThat(응답_데이터.getOpeningTime()).isEqualToIgnoringNanos(
        공지사항_생성_요청_데이터.getJsonBody().getOpeningTime());
    assertThat(응답_데이터.getClosingTime()).isEqualToIgnoringNanos(
        공지사항_생성_요청_데이터.getJsonBody().getClosingTime());
    assertThat(hasAnyAttachment(응답_데이터.getAttachments(), 공지사항_생성_요청_데이터.getFileNames())).isTrue();
    return 응답_데이터;
  }

  public static ExtractableResponse<Response> 공지사항_수정_요청(Long 생성_수정_요청_식별자,
      NoticeRequestFixture 생성_수정_데이터,
      TokenDto.Response 토큰) {
    return RestAssuredUtils.putWithFiles(공지사항_리소스_주소 + "/" + 생성_수정_요청_식별자,
        토큰.getAccessToken(),
        "notice",
        생성_수정_데이터.getJsonBody(), "attachments", 생성_수정_데이터.getFileNames());
  }


  public static NoticeDto.Response 공지사항_수정_응답_확인(ExtractableResponse<Response> 공지사항_수정_응답,
      NoticeRequestFixture 공지사항_수정_요청_데이터, Long 공지사항_식별자) {

    assertThat(공지사항_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

    NoticeDto.Response 응답_데이터 = 공지사항_수정_응답.as(NoticeDto.Response.class);
    assertThat(응답_데이터.getId()).isEqualTo(공지사항_식별자);

    assertThat(응답_데이터.getTitle()).isEqualTo(공지사항_수정_요청_데이터.getJsonBody().getTitle());
    assertThat(응답_데이터.getContent()).isEqualTo(공지사항_수정_요청_데이터.getJsonBody().getContent());
    assertThat(응답_데이터.getOpeningTime()).isEqualTo(공지사항_수정_요청_데이터.getJsonBody().getOpeningTime());
    assertThat(응답_데이터.getClosingTime()).isEqualTo(공지사항_수정_요청_데이터.getJsonBody().getClosingTime());
    assertThat(hasAnyAttachment(응답_데이터.getAttachments(), 공지사항_수정_요청_데이터.getFileNames())).isTrue();

    return 응답_데이터;
  }

  public static ExtractableResponse<Response> 공지사항_검색_요청() {
    return RestAssuredUtils.get(공지사항_리소스_주소);
  }

  private static boolean hasAnyAttachment(List<AttachmentDto.Response> attachments,
      List<String> fileNames) {
    return attachments.stream()
        .anyMatch(attachment -> fileNames.contains(attachment.getFileName()));
  }

  public static void 공지사항_검색_응답_확인(ExtractableResponse<Response> 공지사항_검색_응답,
      NoticeDto.Response 공지사항_수정_응답_데이터) {

    assertThat(공지사항_검색_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

    List<NoticeDto.Response> 공지사항_검색_응답_데이터 = extractFromJson(공지사항_검색_응답.as(JsonNode.class));
    assertThat(공지사항_검색_응답_데이터.size()).isEqualTo(1);

    NoticeDto.Response 첫번째 = 공지사항_검색_응답_데이터.get(0);
    assertThat(첫번째.getTitle()).isEqualTo(공지사항_수정_응답_데이터.getTitle());
    assertThat(첫번째.getOpeningTime()).isEqualTo(공지사항_수정_응답_데이터.getOpeningTime());
  }

  private static List<NoticeDto.Response> extractFromJson(JsonNode jsonNode) {
    List<NoticeDto.Response> 공지사항_검색_응답_데이터 = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      공지사항_검색_응답_데이터 = objectMapper.readValue(jsonNode.get("content").toString(),
          new TypeReference<>() {
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
    return 공지사항_검색_응답_데이터;
  }


  public static ExtractableResponse<Response> 공지사항_삭제_요청(Long 공지사항_식별자, TokenDto.Response 토큰) {
    return RestAssuredUtils.delete(공지사항_리소스_주소 + "/" + 공지사항_식별자, 토큰.getAccessToken());
  }

  public static void 공지사항_삭제_응답_확인(ExtractableResponse<Response> 공지사항_삭제_응답) {
    assertThat(공지사항_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
