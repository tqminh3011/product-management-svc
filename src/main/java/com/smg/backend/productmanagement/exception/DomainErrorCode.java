package com.smg.backend.productmanagement.exception;

import lombok.Getter;

@Getter
public enum DomainErrorCode {
    
    // Common Code
    GENERIC_ERROR("00", "Internal Server Error"),
    INVALID_PARAMETER("01", "Invalid Parameter"),
    INVALID_OWNER("02", "Invalid Product Owner"),

    ;

    public static final String SERVICE_IDENTIFIER = "3011";

    private final String externalCode;
    private final String message;
    
    DomainErrorCode(String externalCode, String message) {
        this.externalCode = externalCode;
        this.message = message;
    }
    
    public static String toUniversalByExternalCode(String code) {
        return SERVICE_IDENTIFIER + code;
    }
    
    public String toUniversalCode() {
        return SERVICE_IDENTIFIER + externalCode;
    }
    
}
