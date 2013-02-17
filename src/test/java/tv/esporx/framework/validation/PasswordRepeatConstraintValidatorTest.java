package tv.esporx.framework.validation;

import org.junit.Before;
import org.junit.Test;
import tv.esporx.domain.Esporxer;

import javax.validation.ConstraintValidatorContext;

import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordRepeatConstraintValidatorTest {

    private PasswordRepeatConstraint annotation;
    private PasswordRepeatConstraintValidator validator;
    private Esporxer esporxer;
    private ConstraintValidatorContext validatorContext;

    @Before
    public void prepare() {
        mockedValidationContext();
        mockedAnnotation();
        validator = new PasswordRepeatConstraintValidator();
        validator.initialize(annotation);
        esporxer = new Esporxer();
    }

    @Test
    public void should_validate_equal_password() {
        givenUserWithMatchingPassword();
        assertThat(validator.isValid(esporxer, validatorContext)).isTrue();
    }

    @Test
    public void should_not_validate_null_passwords() {
        givenUserWithNullPasswords();
        assertThat(validator.isValid(esporxer, validatorContext)).isFalse();
    }

    @Test
    public void should_not_validate_different_passwords() {
        givenUserWithDifferentPasswords();
        assertThat(validator.isValid(esporxer, validatorContext)).isFalse();
    }

    private void mockedAnnotation() {
        annotation = mock(PasswordRepeatConstraint.class);
        when(annotation.passwordFieldName()).thenReturn("password");
        when(annotation.passwordConfirmationFieldName()).thenReturn("passwordConfirmation");
        when(annotation.message()).thenReturn("FAIL");
    }

    private void mockedValidationContext() {
        validatorContext = mock(ConstraintValidatorContext.class);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        NodeBuilderDefinedContext nodeContext = mock(NodeBuilderDefinedContext.class);
        when(builder.addNode(anyString())).thenReturn(nodeContext);
        when(validatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    private void givenUserWithMatchingPassword() {
        esporxer.setPassword("y");
        esporxer.setPasswordConfirmation("y");
    }

    private void givenUserWithNullPasswords() {
        esporxer.setPassword(null);
        esporxer.setPasswordConfirmation(null);
    }

    private void givenUserWithDifferentPasswords() {
        esporxer.setPassword("y");
        esporxer.setPasswordConfirmation("n");
    }
}
