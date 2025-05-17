package org.hamal.abhishek.vpp.adapters.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception
    ) {
        log.warn("Illegal argument: {}", exception.getMessage());
        return Mono.just(Map.of(
                ErrorCodes.BAD_REQUEST.getCodeAndDescription(),
                exception.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public Mono<Map<String, String>> handleGeneralException(Exception exception) {
        log.error("Unexpected error occurred", exception);
        return Mono.just(Map.of(
                ErrorCodes.INTERNAL_ERROR.getCodeAndDescription(),
                exception.getMessage()
        ));
    }

}
