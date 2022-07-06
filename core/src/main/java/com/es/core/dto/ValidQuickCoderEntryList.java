package com.es.core.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidQuickCoderEntryList {
    @Valid
    private List<QuickCoderEntryDTO> list = new ArrayList<>();
}
