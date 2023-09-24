package notiboard;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import notiboard.notice.application.NoticeService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class NotiboardApplication implements ApplicationRunner {

  private final JobScheduler jobScheduler;
  private final NoticeService noticeService;

  public static void main(String[] args) {
    SpringApplication.run(NotiboardApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    jobScheduler.scheduleRecurrently(Duration.ofSeconds(30), noticeService::clearNoticesCache);
    jobScheduler.scheduleRecurrently(Cron.daily(), noticeService::closeNotices);
  }
}
