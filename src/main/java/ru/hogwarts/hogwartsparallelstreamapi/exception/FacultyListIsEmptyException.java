package ru.hogwarts.hogwartsparallelstreamapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacultyListIsEmptyException extends RuntimeException{


    public FacultyListIsEmptyException() {
    }

    public FacultyListIsEmptyException(String message) {
        super(message);
    }

    public FacultyListIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacultyListIsEmptyException(Throwable cause) {
        super(cause);
    }

    public FacultyListIsEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
