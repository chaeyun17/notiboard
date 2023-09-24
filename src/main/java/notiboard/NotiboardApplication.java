package notiboard;

import lombok.RequiredArgsConstructor;
import notiboard.notice.application.PostStatsService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class NotiboardApplication implements ApplicationRunner {

  private final JobScheduler jobScheduler;
  private final PostStatsService postStatsService;

  public static void main(String[] args) {
    SpringApplication.run(NotiboardApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    jobScheduler.scheduleRecurrently(Cron.minutely(), postStatsService::syncAllViewCnt);
  }
}
