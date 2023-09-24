package notiboard.notice.application;

import static notiboard.notice.application.NoticeService.NOTICE_CACHE_MANAGER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import notiboard.common.dao.RedisDao;
import notiboard.notice.dao.PostStatsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
    return viewCount;
  }

  @Caching(evict = {
      @CacheEvict(value = NoticeService.CACHE_NOTICES, allEntries = true, cacheManager = NOTICE_CACHE_MANAGER)
  })
  @Transactional
  public void syncAllViewCnt() {
    postStatsRepository.findAll().forEach((stats) -> {
      Optional<String> value = redisDao.getValues(String.valueOf(stats.getId()));
      if (value.isEmpty()) {
        return;
      }
      long count = Long.parseLong(value.get());
      stats.setViewCount(count);
    });
  }


}
