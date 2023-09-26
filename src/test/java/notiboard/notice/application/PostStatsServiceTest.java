package notiboard.notice.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import notiboard.notice.dao.PostStatsRepository;
import notiboard.notice.domain.PostStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostStatsServiceTest {

  private PostStatsService postStatsService;
  @Mock
  private PostStatsRepository postStatsRepository;

  @BeforeEach
  void setup() {
    this.postStatsService = new PostStatsService(postStatsRepository);
  }

  @Test
  void increaseViewCnt() {
    Long expectedViewCount = 1L;
    Long postStatsId = 1L;
    PostStats postStats = PostStats.of(expectedViewCount);
    Mockito.when(postStatsRepository.findById(postStatsId)).thenReturn(Optional.of(postStats));

    Long viewCount = postStatsService.increaseViewCnt(postStatsId);

    assertThat(viewCount).isEqualTo(expectedViewCount);
  }
}