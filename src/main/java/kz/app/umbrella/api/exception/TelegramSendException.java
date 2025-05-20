package kz.app.umbrella.api.exception;

public class TelegramSendException extends RuntimeException {

    public TelegramSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramSendException(String message) {
        super(message);
    }
}
