package org.ors.cross.share_kernel.exception;

// TODO: ném khi login sai email/password. Map -> HTTP 401.
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
