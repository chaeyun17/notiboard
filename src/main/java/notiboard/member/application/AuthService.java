package notiboard.member.application;

import static notiboard.exception.ErrorCode.NOT_MATCH_CREDENTIAL;

import lombok.RequiredArgsConstructor;
import notiboard.exception.CustomException;
import notiboard.member.dto.TokenDto.Request;
import notiboard.member.dto.TokenDto.Response;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final JwtUtils jwtUtils;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public Response getToken(Request request) {
    UserDetails userDetails;
    try {
      userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    } catch (UsernameNotFoundException ex) {
      throw new CustomException(NOT_MATCH_CREDENTIAL);
    }
    if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
      throw new CustomException(NOT_MATCH_CREDENTIAL);
    }
    return new Response(jwtUtils.generateJwtToken(userDetails.getUsername()));
  }
}
