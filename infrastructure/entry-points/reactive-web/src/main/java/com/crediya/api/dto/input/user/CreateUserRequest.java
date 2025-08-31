package com.crediya.api.dto.input.user;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.crediya.api.constants.ValidationMessage.*;

public record CreateUserRequest(
        @NotNull(message = NAME_NOT_BLANK)
        @NotBlank(message = NAME_NOT_BLANK)
        String name,
        
        @NotNull(message = LAST_NAME_NOT_BLANK)
        @NotBlank(message = LAST_NAME_NOT_BLANK)
        String lastname,
        
        @Past(message = BIRTH_DATE_PAST)
        LocalDate birthDate,
        
        @NotBlank(message = ADDRESS_NOT_BLANK)
        String address,
        
        @NotBlank(message = PHONE_NOT_BLANK)
        String phone,
        
        @NotNull(message = EMAIL_NOT_BLANK)
        @NotBlank(message = EMAIL_NOT_BLANK)
        @Email(message = EMAIL_FORMAT)
        String email,
        
        @NotNull(message = BASE_SALARY_NOT_NULL)
        @Positive(message = BASE_SALARY_POSITIVE)
        BigDecimal salaryBase,
    
    	@NotNull(message = IDENTIFICATION_NOT_BLANK)
        @NotBlank(message = IDENTIFICATION_NOT_BLANK)
        @Size(max = 20, message = IDENTIFICATION_NOT_LARGER_THAN_20)
        @Pattern(regexp = "^\\d+$", message = INVALID_IDENTIFICATION)
        @Positive(message = INVALID_IDENTIFICATION)
        String identification,
    
        @NotNull(message = PASSWORD_NOT_BLANK)
        @NotBlank(message = PASSWORD_NOT_BLANK)
        @Size(min = 5, message = PASSWORD_MIN_LENGTH_5)
        String password
) {
}