package org.ors.cross.share_kernel.exception;

// Ném khi không tìm thấy resource. Map -> HTTP 404 trong GlobalExceptionHandler.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
