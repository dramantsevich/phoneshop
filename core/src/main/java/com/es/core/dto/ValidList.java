package com.es.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidList {
    @Valid
    private List<CartDTO> list = new ArrayList<>();
}
