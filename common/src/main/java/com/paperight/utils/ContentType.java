package com.paperight.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ContentTypeMultipartFileValidator.class})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
public @interface ContentType {

    String message() default "{com.paperight.utils.ContentType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

/**
 * Specify accepted content types.
 *
 * Content type example :
 * <ul>
 * <li>application/pdf - accepts PDF documents only</li>
 * <li>application/msword - accepts MS Word documents only</li>
 * <li>images/png - accepts PNG images only</li>
 * </ul>
 *
 * @return accepted content types
 */
     String[] value();
}