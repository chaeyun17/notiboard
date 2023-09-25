package notiboard.notice.application;

import lombok.RequiredArgsConstructor;
import notiboard.notice.dao.PostStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostStatsService {

  private final PostStatsRepository postStatsRepository;

  @Transactional
  public long increaseViewCnt(Long postStatsId) {
    postStatsRepository.increaseViewCount(postStatsId);
    return postStatsRepository.findById(postStatsId).orElseThrow().getViewCount();
  }

}
