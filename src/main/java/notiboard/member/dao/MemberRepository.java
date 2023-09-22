package notiboard.member.dao;

import java.util.Optional;
import notiboard.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findOneByUsername(String username);

  boolean existsByUsername(String username);
}
