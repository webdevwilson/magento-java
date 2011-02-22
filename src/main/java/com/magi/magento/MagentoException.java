package com.magi.magento;

public class MagentoException extends RuntimeException {

    public MagentoException(Throwable cause) {
        super(cause);
    }

    public MagentoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MagentoException(String message) {
        super(message);
    }

}
