package ru.iskratel.client.exception;

import java.io.IOException;

public class InvalidConfigurationException extends IOException {

    public InvalidConfigurationException(String message, Exception cause) {
        super(message, cause);
    }
}
