package com.spring_api.library_transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatatableResponse<T> {
    private String message;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private int statusCode;

    @JsonProperty("ziyad_error_code")
    private String ziyadErrorCode;

    @JsonProperty("trace_id")
    private String traceId;
    private T data;
}
