package com.es.core.exception;

import com.es.core.dto.ErrorCodeDTO;
import com.es.core.dto.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HashMap<String, List<String>> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String errorLocation;
            if (error instanceof FieldError) {
                errorLocation = ((FieldError) error).getField();
            } else {
                errorLocation = error.getObjectName();
            }

            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            if (!errors.containsKey(errorLocation)) {
                errors.put(errorLocation, new ArrayList<>(List.of(errorMessage)));
            } else {
                errors.get(errorLocation).add(errorMessage);
            }
        }

        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();
        validationResponseDTO.setException("Validation failed. " + ex.getBindingResult().getErrorCount() + " error(s)");
        validationResponseDTO.setMessage(errors);
        validationResponseDTO.setCode(ErrorCodeDTO.VALIDATION_ERROR.getCode());

        return new ResponseEntity<>(validationResponseDTO, null, HttpStatus.BAD_REQUEST);
    }
}
