package notiboard.member.application;

import static notiboard.common.error.ErrorCode.DUPLICATE_MEMBER_USERNAME;

import lombok.RequiredArgsConstructor;
import notiboard.common.error.CustomException;
import notiboard.member.dao.MemberRepository;
import notiboard.member.domain.Member;
import notiboard.member.dto.MemberDto.Request;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void signUp(Request request) {
    validate(request);
    Member member = Member.of(request, passwordEncoder);
    memberRepository.save(member);
  }

  public void validate(Request request) {
    if (memberRepository.existsByUsername(request.getUsername())) {
      throw new CustomException(DUPLICATE_MEMBER_USERNAME);
    }
  }
}
