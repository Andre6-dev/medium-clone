package com.devandre.mediumclone.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * @author Andre on 15/02/2024
 * @project medium-clone
 */
@Getter
public class InvalidRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String subject;

    private final String violation;

    public InvalidRequestException(final String subject, final String violation) {
        super(String.format("%s %s", subject, violation));
        this.subject = subject;
        this.violation = violation;
    }

    public InvalidRequestException(final String subject, final String violation, final Throwable cause) {
        super(String.format("%s %s", subject, violation), cause);
        this.subject = subject;
        this.violation = violation;
    }
}
