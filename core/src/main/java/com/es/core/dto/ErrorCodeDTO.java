package com.es.core.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeDTO {
    VALIDATION_ERROR("validation_error"),
    ENTITY_NOT_FOUND("entity_not_found"),
    LOGIC_ERROR("logic_error"),
    PARSE_ERROR("parse_error");

    private final String code;
}
