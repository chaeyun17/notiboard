package notiboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("서버 가동 테스트")
@SpringBootTest
class NotiboardApplicationTests {

  @Test
  @DisplayName("Spring Context 로딩 테스트")
  void contextLoads() {
  }

}
