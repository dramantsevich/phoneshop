package com.es.core.exception;

import com.es.core.dto.ErrorCodeDTO;
import com.es.core.dto.ExceptionResponseDTO;
import com.es.core.dto.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @ExceptionHandler(value = OutOfStockException.class)
    public ResponseEntity<?> handleOutOfStockExceptions(OutOfStockException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("Out of stock");
        exceptionResponseDTO.setMessage("No such quantity available ");
        exceptionResponseDTO.setCode(ErrorCodeDTO.NOT_ACCEPTABLE.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = NegativeQuantityException.class)
    public ResponseEntity<?> handleNegativeQuantityExceptions(NegativeQuantityException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("Negative value");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setCode(ErrorCodeDTO.NEGATIVE_VALUE.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = PhonePriceException.class)
    public ResponseEntity<?> handlePhonePriceExceptions(PhonePriceException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("price is null");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setCode(ErrorCodeDTO.PRICE_IS_NULL.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<?> handleNullPointerExceptions(NullPointerException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("null");
        exceptionResponseDTO.setMessage("Object is null");
        exceptionResponseDTO.setCode(ErrorCodeDTO.ENTITY_NOT_FOUND.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = QuantityNullException.class)
    public ResponseEntity<?> handleQuantityNullExceptions(QuantityNullException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("empty");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setCode(ErrorCodeDTO.QUANTITY_IS_NULL.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessExceptions(EmptyResultDataAccessException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("url");
        exceptionResponseDTO.setMessage("is invalid URL");
        exceptionResponseDTO.setCode(ErrorCodeDTO.ENTITY_NOT_FOUND.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundExceptions(OrderNotFoundException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("url");
        exceptionResponseDTO.setMessage("is invalid URL");
        exceptionResponseDTO.setCode(ErrorCodeDTO.ENTITY_NOT_FOUND.getCode());

        return new ResponseEntity<>(exceptionResponseDTO, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = OrderOutOfStockException.class)
    public String handleOrderOutOfStockExceptions(OrderOutOfStockException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("Out of stock");
        exceptionResponseDTO.setMessage("No such phones available ");
        exceptionResponseDTO.setCode(ErrorCodeDTO.NOT_ACCEPTABLE.getCode());

        return "error";
    }
}
