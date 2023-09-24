package notiboard.notice.application;

import static notiboard.exception.ErrorCode.NOT_FOUND_NOTICE;
import static notiboard.notice.application.NoticeService.NOTICE_CACHE_MANAGER;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notiboard.exception.CustomException;
import notiboard.notice.dao.NoticeRepository;
import notiboard.notice.domain.Notice;
import notiboard.notice.dto.NoticeDto.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeContentService {

  private final NoticeRepository noticeRepository;

  @Cacheable(value = NoticeService.CACHE_NOTICE, key = "#id", cacheManager = NOTICE_CACHE_MANAGER)
  public Response findById(Long id) {
    Notice notice = findByIdFetchOrElseThrow(id);
    return new Response(notice);
  }

  public Notice findByIdFetchOrElseThrow(Long id) {
    return noticeRepository.findByIdFetch(id)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
  }

}
