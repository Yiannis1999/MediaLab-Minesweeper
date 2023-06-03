package com.example;

import java.io.IOException;

public class InvalidDescriptionException extends IOException {
    public InvalidDescriptionException() {
        super();
    }

    public InvalidDescriptionException(String message) {
        super(message);
    }

    public InvalidDescriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDescriptionException(Throwable cause) {
        super(cause);
    }
}