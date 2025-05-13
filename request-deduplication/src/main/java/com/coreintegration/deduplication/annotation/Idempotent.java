package com.coreintegration.deduplication.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    String headerKey() default "Idempotency-Key";

}
