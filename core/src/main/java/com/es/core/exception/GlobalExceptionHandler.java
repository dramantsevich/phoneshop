package com.es.core.exception;

import com.es.core.dto.ErrorCodeDTO;
import com.es.core.dto.ExceptionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = OutOfStockException.class)
    public ResponseEntity<?> handleOutOfStockExceptions(OutOfStockException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setException("Out of stock");
        exceptionResponseDTO.setMessage("No such quantity available ");
        exceptionResponseDTO.setCode(ErrorCodeDTO.NOT_ACCEPTABLE.getCode());

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
}
