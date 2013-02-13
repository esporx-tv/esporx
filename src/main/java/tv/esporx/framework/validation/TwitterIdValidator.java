package tv.esporx.framework.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.google.common.base.Objects.firstNonNull;

public class TwitterIdValidator implements ConstraintValidator<ValidTwitterId, String> {
    @Override
    public void initialize(ValidTwitterId validTwitterId) {
    }

    @Override
    public boolean isValid(String twitterId, ConstraintValidatorContext constraintValidatorContext) {
        String trimmed = firstNonNull(twitterId, "").trim();

        if(trimmed.isEmpty()) {
            return true;
        } else if (trimmed.startsWith("@")) {
            String subStr = trimmed.substring(1);
            boolean hasSpaces = subStr.contains(" ");
            boolean isTooShort = subStr.length() <= 0;
            boolean isTooLong = subStr.length() > 20;

            if(hasSpaces || isTooShort || isTooLong)
                return false;
            else
                return true;
        } else {
            return false;
        }
    }
}
