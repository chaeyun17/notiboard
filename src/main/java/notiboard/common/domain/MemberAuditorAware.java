package notiboard.common.domain;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import notiboard.member.application.MemberSearchService;
import notiboard.member.domain.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuditorAware implements AuditorAware<Member> {

  private final MemberSearchService memberSearchService;

  @Override
  public Optional<Member> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return Optional.empty();
    }
    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      return Optional.empty();
    }
    Member member = (Member) authentication.getPrincipal();
    return memberSearchService.findById(member.getId());
  }

}
