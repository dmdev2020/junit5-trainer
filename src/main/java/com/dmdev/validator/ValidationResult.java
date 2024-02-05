package com.dmdev.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    @Getter
    private final List<Error> errors = new ArrayList<>();

    public void add(Error error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
