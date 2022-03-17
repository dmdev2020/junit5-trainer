package com.dmdev.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
