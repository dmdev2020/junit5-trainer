package com.dmdev.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    Integer code;
    String message;
}
