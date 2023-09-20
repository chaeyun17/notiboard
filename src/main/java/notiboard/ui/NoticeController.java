package notiboard.ui;

import lombok.RequiredArgsConstructor;
import notiboard.application.NoticeService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

}
