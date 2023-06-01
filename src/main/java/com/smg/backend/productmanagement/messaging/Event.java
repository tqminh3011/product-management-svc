package com.smg.backend.productmanagement.messaging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event<D> {

  @NotBlank
  private String id;

  @NotBlank
  private String version;

  @NotNull
  @Positive
  private Long time;

  @NotBlank
  private String source;

  @Valid
  @NotNull
  private D detail;
}