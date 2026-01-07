/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String[] value();
    LogicalOperator operator() default LogicalOperator.OR;

    enum LogicalOperator {
        AND, OR
    }
}
