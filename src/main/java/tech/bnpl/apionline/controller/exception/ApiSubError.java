package tech.bnpl.apionline.controller.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiSubError(
  @JsonProperty("object")
  String object,

  @JsonProperty("field")
  String field,

  @JsonProperty("valor_rechazado")
  Object rejectedValue,

  @JsonProperty("mensaje")
  String message

  ) {

  public static ApiSubError from(String object, String message) {
    return new ApiSubError(object, null, null, message);
  }


}
