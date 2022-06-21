package com.es.core.service.impl.order;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class DefaultOrderService {
    public void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            String errorMessage = "Value is required";
            errors.put(parameter, errorMessage);
        } else {
            consumer.accept(value);
        }
    }
}
