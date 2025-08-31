package com.crediya.api.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidationMessage {

    public final String NAME_NOT_BLANK = "Name is required and cannot be blank";
    public final String LAST_NAME_NOT_BLANK = "Last name is required and cannot be blank";
    public final String BIRTH_DATE_PAST = "Birth date must be in the past";
    public final String ADDRESS_NOT_BLANK = "Address cannot be blank";
    public final String PHONE_NOT_BLANK = "Phone cannot be blank";
    public final String EMAIL_NOT_BLANK = "Email is required and cannot be blank";
    public final String EMAIL_FORMAT = "Email must have a valid format";
    public final String BASE_SALARY_NOT_NULL = "Base salary cannot be null";
    public final String BASE_SALARY_POSITIVE = "Base salary must be a positive number";
    public final String IDENTIFICATION_NOT_BLANK = "Identification is required and cannot be blank";
    public final String IDENTIFICATION_NOT_LARGER_THAN_20 = "Identification cannot be longer than 20 characters";
    public final String INVALID_IDENTIFICATION = "Identification is invalid";
    public final String PASSWORD_NOT_BLANK = "Password is required and cannot be blank";
    public final String PASSWORD_MIN_LENGTH_5 = "Password must be at least 5 characters long";

}