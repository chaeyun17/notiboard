package notiboard.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import notiboard.common.applicaiton.PolicyChecker;
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
import notiboard.notice.dto.NoticeDto;
import notiboard.notice.dto.NoticeDto.Request;
import notiboard.notice.dto.NoticeDto.Response;
import notiboard.notice.dto.PageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("[Service] 공지사항 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

  @Mock
  private NoticeRepository noticeRepository;
  @Mock
  private AttachmentService attachmentService;
  @Mock
  private PolicyChecker policyChecker;
  @Mock
  private NoticeContentService noticeContentService;
  @Mock
  private PostStatsService postStatsService;
  @Mock
  private CacheManager cacheManager;

  private NoticeService noticeService;

  @BeforeEach
  void setup() {
    this.noticeService = new NoticeService(noticeRepository, attachmentService, policyChecker,
        noticeContentService, postStatsService, cacheManager);
  }

  @DisplayName("공지사항 생성")
  @Test
  void create() {
    Request request = new Request("제목123", "본문123", LocalDateTime.now(),
        LocalDateTime.now().plusHours(1L), new ArrayList<>(), new ArrayList<>());
    long expectedId = 1L;
    when(noticeRepository.save(any())).thenReturn(Notice.of(expectedId));

    Long id = noticeService.create(request);

    assertThat(id).isEqualTo(expectedId);
  }

  @DisplayName("공지사항 상세 조회")
  @Test
  void findById() {
    Long viewCount = 1L;
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice notice = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(viewCount), member);

    NoticeDto.Response response = new Response(notice);
    when(noticeContentService.findById(any())).thenReturn(response);

    when(postStatsService.increaseViewCnt(any())).thenReturn(viewCount);

    NoticeDto.Response result = noticeService.findById(1L);

    assertThat(result.getTitle()).isEqualTo(notice.getTitle().toText());
    assertThat(result.getPostStats().getViewCount()).isEqualTo(
        notice.getPostStats().getViewCount());

  }

  @DisplayName("공지사항 삭제")
  @Test
  void deleteById() {
    long id = 1L;
    Long viewCount = 1L;
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice notice = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(viewCount), member);
    when(noticeContentService.findByIdFetchOrElseThrow(any())).thenReturn(notice);
    when(noticeRepository.existsByIdAndCreatedById(any(), any())).thenReturn(true);

    noticeService.deleteById(id);
  }

  @DisplayName("공지사항 검색")
  @Test
  void search() {
    Long viewCount = 1L;
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice notice = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(viewCount), member);
    NoticeDto.Response response = new NoticeDto.Response(notice);
    List<NoticeDto.Response> notices = List.of(response);
    when(noticeRepository.search(any())).thenReturn(new PageImpl<>(notices));

    PageDto<Response> result = noticeService.search(Pageable.unpaged());

    assertThat(result.getSize()).isEqualTo(1L);
  }

  @DisplayName("공지사항 수정")
  @Test
  void modify() {
    Request request = new Request("제목123", "본문123", LocalDateTime.now(),
        LocalDateTime.now().plusHours(1L), new ArrayList<>(), new ArrayList<>());
    Member member = Member.of(Username.of("abcdef"), Password.of("pw123"),
        Nickname.of("abcdef"));
    Notice notice = Notice.of(Title.of("abc"), Content.of("abc"),
        PostingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)),
        PostStats.of(0L), member);
    long expectedId = 1L;
    Mockito.when(noticeRepository.existsByIdAndCreatedById(any(), any())).thenReturn(true);
    Mockito.when(noticeContentService.findByIdFetchOrElseThrow(any())).thenReturn(notice);

    Response response = noticeService.modify(expectedId, request);

    assertThat(response.getTitle()).isEqualTo(request.getTitle());
    assertThat(response.getContent()).isEqualTo(request.getContent());
  }

}