package edu.asu.diging.gilesecosystem.nepomuk.core.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.impl.RegisteredApp;

public class RegisteredAppValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return arg0 == RegisteredApp.class;
    }

    @Override
    public void validate(Object arg0, Errors arg1) {
        ValidationUtils.rejectIfEmpty(arg1, "name", "app_name_required");
    }

}
