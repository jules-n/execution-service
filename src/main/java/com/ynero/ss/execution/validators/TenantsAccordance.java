package com.ynero.ss.execution.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TenantsAccordanceValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantsAccordance {
    String message() default "One of nodes belongs to another tenant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
