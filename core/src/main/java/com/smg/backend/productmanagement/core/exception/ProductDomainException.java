package com.smg.backend.productmanagement.core.exception;

import lombok.Getter;

@Getter
public class ProductDomainException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    private final DomainErrorCode domainCode;

    public ProductDomainException(DomainErrorCode domainCode) {
        super(domainCode.getMessage(), null);
        this.errorCode = domainCode.toUniversalCode();
        this.errorMessage = domainCode.getMessage();
        this.domainCode = domainCode;
    }

    public ProductDomainException(DomainErrorCode domainCode, Throwable throwable) {
        super(String.format(domainCode.getMessage() + " :: " + throwable.getMessage()));
        this.errorCode = domainCode.toUniversalCode();
        this.errorMessage = domainCode.getMessage();
        this.domainCode = domainCode;
    }


}
