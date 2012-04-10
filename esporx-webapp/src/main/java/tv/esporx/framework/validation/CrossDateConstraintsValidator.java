package tv.esporx.framework.validation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CrossDateConstraintsValidator implements ConstraintValidator<CrossDateConstraints, Object> {

	private String startDateField;
	private String endDateField;
	private String message;

	@Override
	public void initialize(final CrossDateConstraints constraintAnnotation) {
		startDateField = constraintAnnotation.startDateFieldName();
		endDateField = constraintAnnotation.endDateFieldName();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object annotatedClassInstance, final ConstraintValidatorContext context) {
		boolean isValid = false;
		try {
			Date startDate = getDateFieldValue(annotatedClassInstance, startDateField);
			Date endDate = getDateFieldValue(annotatedClassInstance, endDateField);
			isValid = endDate.after(startDate);

			if (!isValid) {
				forceErrorsOnBothFields(context);
			}

			return isValid;
		}
		catch (Exception e) {
			return isValid;
		}
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
