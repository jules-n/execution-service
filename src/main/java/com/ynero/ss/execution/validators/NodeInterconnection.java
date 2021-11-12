package com.ynero.ss.execution.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NodeInterconnectionValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeInterconnection {
    String message() default "Wrong sequence";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
