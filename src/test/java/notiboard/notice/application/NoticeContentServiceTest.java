package notiboard.notice.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import notiboard.member.domain.Member;
import notiboard.member.domain.Nickname;
import notiboard.member.domain.Password;
import notiboard.member.domain.Username;
import notiboard.notice.dao.NoticeRepository;
import notiboard.notice.domain.Content;
import notiboard.notice.domain.Notice;
import notiboard.notice.domain.PostStats;
import notiboard.notice.domain.PostingPeriod;
import notiboard.notice.domain.Title;
import notiboard.notice.dto.NoticeDto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoticeContentServiceTest {

  private NoticeContentService noticeContentService;
  @Mock
  private NoticeRepository noticeRepository;

  @BeforeEach
  void setup() {
    this.noticeContentService = new NoticeContentService(noticeRepository);
  }

  @Test
  void findById() {
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice notice = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(), member);
    Mockito.when(noticeRepository.findByIdFetch(1L)).thenReturn(Optional.of(notice));

    Response response = noticeContentService.findById(1L);

    assertThat(response.getTitle()).isEqualTo(notice.getTitle().toText());
    assertThat(response.getContent()).isEqualTo(notice.getContent().toText());
    assertThat(response.getPostStats().getViewCount()).isEqualTo(
        notice.getPostStats().getViewCount());
    assertThat(response.getOpeningTime()).isEqualTo(notice.getPostingPeriod().getOpeningTime());
    assertThat(response.getClosingTime()).isEqualTo(notice.getPostingPeriod().getClosingTime());
  }

  @Test
  void findByIdFetchOrElseThrow() {
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice expected = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(), member);
    Mockito.when(noticeRepository.findByIdFetch(1L)).thenReturn(Optional.of(expected));

    Notice notice = noticeContentService.findByIdFetchOrElseThrow(1L);

    assertThat(notice.getTitle()).isEqualTo(expected.getTitle());
  }
}