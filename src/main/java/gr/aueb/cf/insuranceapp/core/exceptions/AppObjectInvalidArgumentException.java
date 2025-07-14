package gr.aueb.cf.insuranceapp.core.exceptions;

import gr.aueb.cf.insuranceapp.core.exceptions.AppGenericException;

public class AppObjectInvalidArgumentException extends AppGenericException {
    private static final String DEFAULT_CODE = "InvalidArgument";

    public AppObjectInvalidArgumentException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}