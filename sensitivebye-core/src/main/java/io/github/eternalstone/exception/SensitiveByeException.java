package io.github.eternalstone.exception;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 17:14
 */
public class SensitiveByeException extends RuntimeException{

    public SensitiveByeException(String message) {
        super(message);
    }

    public SensitiveByeException(String message, Throwable cause) {
        super(message, cause);
    }
}
