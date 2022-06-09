package com.es.core.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeDTO {
    VALIDATION_ERROR("validation_error"),
    ENTITY_NOT_FOUND("entity_not_found"),
    LOGIC_ERROR("logic_error"),
    PARSE_ERROR("parse_error"),
    NEGATIVE_VALUE("negative_value"),
    NOT_ACCEPTABLE("out_of_stock"),
    PRICE_IS_NULL("price_is_null"),
    QUANTITY_IS_NULL("quantity_is_null");

    private final String code;
}
