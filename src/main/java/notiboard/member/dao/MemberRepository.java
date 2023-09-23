package notiboard.member.dao;

import java.util.Optional;
import notiboard.member.domain.Member;
import notiboard.member.domain.Username;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findOneByUsername(Username username);

  boolean existsByUsername(Username username);
}
