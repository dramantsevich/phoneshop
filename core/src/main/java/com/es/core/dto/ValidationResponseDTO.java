package com.es.core.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ValidationResponseDTO {
    private String exception;
    private HashMap<String, List<String>> message;
    private String code;
}
