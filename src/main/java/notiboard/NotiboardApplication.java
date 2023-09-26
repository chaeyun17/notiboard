package notiboard;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class NotiboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotiboardApplication.class, args);
  }

}
