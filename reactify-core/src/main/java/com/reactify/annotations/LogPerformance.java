/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reactify.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that performance logging should be applied to a method or a class.
 * This annotation can be used to track the execution time and other performance
 * metrics of the annotated method or class, which is useful for monitoring and
 * optimizing application performance.
 * </p>
 *
 * <p>
 * This annotation supports various logging configurations such as specifying
 * the type of log, whether to log input parameters, output results, and a title
 * for the log entry.
 * </p>
 *
 * <p>
 * <strong>Usage Example:</strong>
 * </p>
 *
 * <pre>
 * &#64;LogPerformance(logType = "INFO", actionType = "DATABASE_OPERATION", logOutput = true)
 * public void fetchData() {
 * 	// Method implementation
 * }
 * </pre>
 *
 * <p>
 * <strong>Note:</strong> The actual logging mechanism must be configured
 * separately in the application.
 * </p>
 *
 * @author hoangtien2k3
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPerformance {

    /**
     * The type of log for categorizing the log entries.
     *
     * @return the log type as a string
     */
    String logType() default "";

    /**
     * The type of action that the log entry is recording.
     *
     * @return the action type as a string
     */
    String actionType() default "";

    /**
     * Specifies whether to log the output or not.
     *
     * @return true if output logging is enabled, false otherwise
     */
    boolean logOutput() default true;

    /**
     * Specifies whether to log the input or not.
     *
     * @return true if input logging is enabled, false otherwise
     */
    boolean logInput() default true;

    /**
     * The title for the performance log entry.
     *
     * @return the title as a string
     */
    String title() default "";
}
