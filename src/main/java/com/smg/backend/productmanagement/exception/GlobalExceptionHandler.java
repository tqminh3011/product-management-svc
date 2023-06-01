package com.smg.backend.productmanagement.exception;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductDomainException.class)
    public Map<String, List<Object>> handleDomainException(ServerHttpRequest request, ProductDomainException e) {
        log.error("method: handleDomainException, endpoint: {}", request.getURI(), e);

        return ErrorResponse.getErrorResponse(e.getDomainCode().toUniversalCode(), e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class, ServerWebInputException.class})
    public Map<String, List<Object>> handleValidationException(ServerHttpRequest request, Exception e) {
        log.error("method: handleDomainException, endpoint: {}", request.getURI(), e);

        return ErrorResponse.getErrorResponse(
                DomainErrorCode.INVALID_PARAMETER.toUniversalCode(),
                DomainErrorCode.INVALID_PARAMETER.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public Map<String, List<Object>> handleGenericException(ServerHttpRequest request, Exception e) {
        log.error("method: handleDomainException, endpoint: {}", request.getURI(), e);

        return ErrorResponse.getErrorResponse(
                DomainErrorCode.GENERIC_ERROR.toUniversalCode(),
                DomainErrorCode.GENERIC_ERROR.getMessage()
        );
    }
    
    public static class ErrorResponse {
        
        private ErrorResponse() {
            // Prevent create Error response without any information
        }
        
        public static Map<String, List<Object>> getErrorResponse(String errorCode, String errorMessage) {
            Map<String, List<Object>> response = new HashMap<>();
            String errorKey = "errors";
            ErrorDetail errorDetail = new ErrorDetail(errorCode, errorMessage);
            response.put(errorKey, Collections.singletonList(errorDetail));
            return response;
        }
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ErrorDetail {
            private String errorCode;
            private String errorMessage;
        }
    }
}
