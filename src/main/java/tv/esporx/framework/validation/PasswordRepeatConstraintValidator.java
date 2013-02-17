package tv.esporx.framework.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class PasswordRepeatConstraintValidator implements ConstraintValidator<PasswordRepeatConstraint, Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CrossDateConstraintsValidator.class);
    private String passworldField;
    private String passworldConfirmationField;
    private String message;

    @Override
    public void initialize(PasswordRepeatConstraint passwordRepeatConstraint) {
        passworldField = passwordRepeatConstraint.passwordFieldName();
        passworldConfirmationField = passwordRepeatConstraint.passwordConfirmationFieldName();
        message = passwordRepeatConstraint.message();
    }

    @Override
    public boolean isValid(Object annotatedClassInstance, ConstraintValidatorContext context) {
        boolean isValid = false;
        try {
            String password = getField(passworldField, annotatedClassInstance);
            String passwordConfirmation = getField(passworldConfirmationField, annotatedClassInstance);
            isValid = password != null && password.equals(passwordConfirmation);
            if (!isValid) {
                forceErrorsOnBothFields(context);
            }
            return isValid;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return isValid;
        }
    }

    private String getField(String fieldName, Object annotatedClassInstance) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        return (String) new PropertyDescriptor(fieldName, annotatedClassInstance.getClass()).getReadMethod().invoke(annotatedClassInstance);
    }

    private void forceErrorsOnBothFields(final ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message).addNode(passworldField).addConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addNode(passworldConfirmationField).addConstraintViolation();
    }
}
