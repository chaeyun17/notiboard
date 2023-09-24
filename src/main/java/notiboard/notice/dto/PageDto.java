package notiboard.notice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class PageDto<T> extends PageImpl<T> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PageDto(@JsonProperty("content") List<T> content,
      @JsonProperty("number") int page,
      @JsonProperty("size") int size,
      @JsonProperty("totalElements") long total) {
    super(content, PageRequest.of(page, size), total);
  }

  public PageDto(Page<T> page) {
    super(page.getContent(), page.getPageable(), page.getTotalElements());
  }

  public PageDto(List<T> content) {
    super(content);
  }
}
