package notiboard.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) {
    String id = authentication.getName();
    String pw = (String) authentication.getCredentials();

    if (id.isEmpty() || pw.isEmpty()) {
      throw new BadCredentialsException(id);
    }

    UserDetails userDetail = userDetailsService.loadUserByUsername(id);
    if (!matchPassword(pw, userDetail.getPassword())) {
      throw new BadCredentialsException(id);
    }

    return new UsernamePasswordAuthenticationToken(userDetail, authentication.getCredentials(),
        userDetail.getAuthorities());
  }

  @Override
  public boolean supports(Class authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  private boolean matchPassword(String loginPw, String passWord) {
    return passwordEncoder.matches(loginPw, passWord);
  }
}
