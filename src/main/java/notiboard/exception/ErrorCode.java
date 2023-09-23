package notiboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INVALID_PERIOD_INPUT("시작일과 종료일을 바르게 입력해주세요.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_TITLE("제목은 필수 입력이며 100자 이하여야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_POSTING_PERIOD_IS_NULL("시직일시와 종료일시 입력은 필수입니다.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_POSTING_PERIOD("시작일시가 종료일시보다 늦거나 같을 순 없습니다. ", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_UPLOAD_FILE_TOO_LARGE("파일 사이즈는 2MB 이하이여야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_UPLOAD_FILE_TOO_SMALL("파일이 비어있습니다. 입력을 확인해주세요.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_CONTENT("내용은 필수 입력입니다.", HttpStatus.BAD_REQUEST),
  NOT_FOUND_NOTICE("공지사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  NOT_FOUND_ATTACHMENT("첨부파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  NOT_FOUND_MEMBER("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_MEMBER_USERNAME("중복된 아이디입니다.", HttpStatus.BAD_REQUEST),
  NOT_MATCH_CREDENTIAL("아이디 또는 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
  FORBIDDEN_NOT_LOGGED_MEMBER("인증정보가 없거나 올바르지 않습니다.", HttpStatus.FORBIDDEN),
  FORBIDDEN_NOT_ALLOWED("해당 권한이 없습니다.", HttpStatus.FORBIDDEN);

  public String message;
  public HttpStatus statusCode;

}
