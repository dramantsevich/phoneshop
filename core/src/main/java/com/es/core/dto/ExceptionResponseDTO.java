package com.es.core.dto;

import lombok.Data;

@Data
public class ExceptionResponseDTO {
    private String exception;
    private String message;
    private String code;
}
