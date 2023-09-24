package notiboard.notice.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import notiboard.common.dao.RedisDao;
import notiboard.notice.dao.PostStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostStatsService {

  private final RedisDao redisDao;
  private final PostStatsRepository postStatsRepository;

  public long getViewCount(Long postStatsId) {
    Optional<String> value = redisDao.getValues(String.valueOf(postStatsId));
    String valueText = value.orElseGet(
        () -> String.valueOf(
            postStatsRepository.findById(postStatsId).orElseThrow().getViewCount()));
    return Long.parseLong(valueText);
  }

  @Transactional
  public long increaseViewCnt(Long postStatsId) {
    long viewCount = getViewCount(postStatsId) + 1;
    redisDao.setValues(String.valueOf(postStatsId), String.valueOf(viewCount));
    // TODO: ASYNC Job
    updateViewCountDB(postStatsId, viewCount);
    return viewCount;
  }

  @Transactional
  public void updateViewCountDB(Long postStatsId, long viewCount) {
    postStatsRepository.findById(postStatsId).orElseThrow().setViewCount(viewCount);
  }

}
