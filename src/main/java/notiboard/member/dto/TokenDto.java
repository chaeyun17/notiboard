package notiboard.member.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class TokenDto {

  @Data
  @NoArgsConstructor
  public static class Request {

    @NotNull
    private String username;
    @NotNull
    private String password;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String accessToken;
  }

}
