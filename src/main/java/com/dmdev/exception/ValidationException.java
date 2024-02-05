package com.dmdev.exception;

import com.dmdev.validator.Error;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    @Getter
    private final List<Error> errors;
}
