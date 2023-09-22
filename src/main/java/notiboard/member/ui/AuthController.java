package notiboard.member.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notiboard.member.application.AuthService;
import notiboard.member.application.MemberService;
import notiboard.member.dto.MemberDto;
import notiboard.member.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final MemberService memberService;
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody @Valid MemberDto.Request request) {
    memberService.signUp(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/token")
  public ResponseEntity<TokenDto.Response> getToken(@RequestBody @Valid TokenDto.Request request) {
    TokenDto.Response response = authService.getToken(request);
    return ResponseEntity.ok(response);
  }

}
