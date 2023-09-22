package notiboard.member.application;

import static notiboard.common.error.ErrorCode.NOT_FOUND_MEMBER;

import lombok.RequiredArgsConstructor;
import notiboard.member.dao.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSearchService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findOneByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_MEMBER.getMessage()));
  }
}
