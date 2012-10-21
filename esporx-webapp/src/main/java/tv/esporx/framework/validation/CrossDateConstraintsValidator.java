package tv.esporx.framework.validation;

import static java.util.Arrays.asList;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CrossDateConstraintsValidator implements ConstraintValidator<CrossDateConstraints, Object> {

	private String startDateField;
	private String endDateField;
	private String message;
    private List<String> nullables = new ArrayList<String>();

    @Override
	public void initialize(final CrossDateConstraints constraintAnnotation) {
		startDateField = constraintAnnotation.startDateFieldName();
		endDateField = constraintAnnotation.endDateFieldName();
		message = constraintAnnotation.message();
        nullables = asList(constraintAnnotation.nullableColumns());
    }

	@Override
	public boolean isValid(final Object annotatedClassInstance, final ConstraintValidatorContext context) {
        boolean isValid = false;
		try {
            if (isLegallyNull(annotatedClassInstance, startDateField) || isLegallyNull(annotatedClassInstance, endDateField)) {
                isValid = true;
            }
            else {
                Date startDate = getDateFieldValue(annotatedClassInstance, startDateField);
                Date endDate = getDateFieldValue(annotatedClassInstance, endDateField);
                isValid = endDate.after(startDate);
            }

            if (!isValid) {
                forceErrorsOnBothFields(context);
            }

			return isValid;
		}
		catch (Exception e) {
			return isValid;
		}
	}

    private boolean isLegallyNull(Object annotatedClassInstance, String fieldName) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        return nullables.contains(fieldName) && getDateFieldValue(annotatedClassInstance, fieldName) == null;
    }

    private void forceErrorsOnBothFields(final ConstraintValidatorContext context) {
		context.buildConstraintViolationWithTemplate(message).addNode(startDateField).addConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addNode(endDateField).addConstraintViolation();
	}

	private Date getDateFieldValue(final Object annotatedClassInstance, final String fieldName) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Method dateGetter = new PropertyDescriptor(fieldName, annotatedClassInstance.getClass()).getReadMethod();
		Date date = (Date) dateGetter.invoke(annotatedClassInstance);
		return date;
	}
}
