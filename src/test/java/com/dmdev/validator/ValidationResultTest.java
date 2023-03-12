package com.dmdev.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationResultTest {

	@Test
	void isValid() {
		var validationResult = new ValidationResult();
		Assertions.assertThat(validationResult.isValid())
				.isTrue();
	}

	@Test
	void isNotValid() {
		var validationResult = new ValidationResult();
		validationResult.add(Error.of("invalid", "dummy"));
		Assertions.assertThat(validationResult.isValid())
				.isFalse();
	}

}
