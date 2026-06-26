package com.digitalMarket.productservice.infrastructure.rest.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}