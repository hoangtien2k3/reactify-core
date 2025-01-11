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
 * Indicates that a method should be logged when it is executed. This annotation
 * can be used to track method calls, parameters, and return values, which is
 * useful for debugging and monitoring application behavior.
 * </p>
 *
 * <p>
 * This annotation is typically processed by an aspect-oriented programming
 * (AOP) framework that intercepts method calls and performs the logging.
 * </p>
 *
 * <p>
 * <strong>Usage Example:</strong>
 * </p>
 *
 * <pre>
 * &#64;Loggable
 * public void performAction(String parameter) {
 * 	// Method implementation
 * }
 * </pre>
 *
 * <p>
 * <strong>Note:</strong> The actual logging behavior must be configured
 * separately in the application, depending on the logging framework in use.
 * </p>
 *
 * @author hoangtien2k3
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {}
