package notiboard.common.applicaiton;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import notiboard.exception.CustomException;
import notiboard.exception.ErrorCode;
import notiboard.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("policyChecker")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyChecker {

  public boolean requiredLogin() {
    return isLoggedIn();
  }

  public boolean permitAnonymous() {
    return true;
  }

  public boolean isLoggedIn() {
    return getUserDetails().isPresent();
  }


  public Long getLoggedUserId() {
    return getUserDetails().orElseThrow(
            () -> new CustomException(ErrorCode.FORBIDDEN_NOT_LOGGED_MEMBER))
        .getId();
  }

  private Optional<Member> getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return Optional.empty();
    }
    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      return Optional.empty();
    }
    Member member = (Member) authentication.getPrincipal();
    return Optional.of(member);
  }

}
