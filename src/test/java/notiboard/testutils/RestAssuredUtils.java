package notiboard.testutils;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.config.DecoderConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;

public class RestAssuredUtils {

  public static ExtractableResponse<Response> get(String path, String token) {
    return RestAssured.given()
        .header("Authorization", "Bearer " + token)
        .log().all()
        .when().get(path)
        .then()
        .log().all().extract();
  }

  public static ExtractableResponse<Response> get(String path) {
    return RestAssured.given()
        .log().all()
        .when().get(path)
        .then()
        .log().all().extract();
  }

  public static <T> ExtractableResponse<Response> post(String path, T requestBody) {
    return RestAssured.given().log().all()
        .when()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(requestBody)
        .post(path)
        .then().log().all().extract();
  }

  public static <T> ExtractableResponse<Response> postWithFiles(String path, String token,
      String jsonPartName,
      T requestBody,
      String filePartName, List<String> filePaths) {

    RequestSpecification when = RestAssured.given()
        .header("Authorization", "Bearer " + token)
        .log().all()
        .when();

    for (String filePath : filePaths) {
      when.multiPart(filePartName, loadFile(filePath));
    }

    return when
        .multiPart(
            new MultiPartSpecBuilder(requestBody).controlName(jsonPartName)
                .emptyFileName()
                .mimeType("application/json")
                .charset("UTF-8")
                .build())
        .post(path)
        .then().log().all().extract();
  }

  public static <T> ExtractableResponse<Response> putWithFiles(String path, String token,
      String jsonPartName,
      T requestBody,
      String filePartName, List<String> filePaths) {
    RequestSpecification when = RestAssured.given()
        .config(RestAssured.config()
            .decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8")))
        .header("Authorization", "Bearer " + token)
        .log().all()
        .when();
    for (String filePath : filePaths) {
      when.multiPart(filePartName, loadFile(filePath));
    }

    return when
        .multiPart(
            new MultiPartSpecBuilder(requestBody).controlName(jsonPartName)
                .emptyFileName()
                .mimeType("application/json")
                .charset("UTF-8")
                .build())
        .put(path)
        .then().log().all().extract();
  }

  public static <T> ExtractableResponse<Response> put(String path, T requestBody, String token) {
    return RestAssured.given().log().all()
        .header("Authorization", "Bearer " + token)
        .when()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(requestBody)
        .put(path)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> delete(String path, String token) {
    return RestAssured.given().log().all()
        .header("Authorization", "Bearer " + token)
        .when()
        .delete(path)
        .then().log().all().extract();
  }

  public static File loadFile(String fileName) {
    try {
      return ResourceUtils.getFile(
          "classpath:upload_files/" + fileName);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}