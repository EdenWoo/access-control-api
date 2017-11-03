package com.cfgglobal.test.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = CannotContainSpacesValidator.class) //具体的实现
@Target({java.lang.annotation.ElementType.METHOD,
        java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface CannotContainSpaces {
    String message() default "{cannot.contain.spaces}"; //提示信息,可以写死,可以填写国际化的key

    int length() default 5;

    //下面这两个属性必须添加  
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
      
