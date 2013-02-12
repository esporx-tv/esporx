package tv.esporx.framework.validation;

import com.google.common.base.Predicates;
import tv.esporx.collections.predicates.IsValidHashtag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.any;

public class HashtagsValidator implements ConstraintValidator<ValidHashtags, String> {

    @Override
    public void initialize(ValidHashtags validHashtags) {
    }

    @Override
    public boolean isValid(String hashtags, ConstraintValidatorContext constraintValidatorContext) {
        if(hashtags.trim().isEmpty())
            return true;
        else {
            List<String> splitted = Arrays.asList(hashtags.split(","));

            if(splitted.size() > 2)
                return false;
            else if(any(splitted, Predicates.not(new IsValidHashtag())))
                return false;
            else
                return true;
        }
    }
}
