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
package com.reactify.model;

import lombok.Getter;

/**
 * <p>
 * The StepResult class represents the result of executing a step within a saga
 * process. It encapsulates the outcome of the step execution, indicating
 * whether it was successful or not, along with an optional message providing
 * more context.
 * </p>
 *
 * <p>
 * This class is used to communicate the result of a step back to the calling
 * process, allowing for error handling and control flow management in a saga.
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
public class StepResult {
    private final boolean success; // Indicates whether the step was successful
    private final String message; // Optional message providing details about the step execution

    private StepResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * <p>
     * success.
     * </p>
     *
     * <p>
     * Static factory method to create a successful StepResult. This method returns
     * an instance of StepResult indicating success without any accompanying
     * message.
     * </p>
     *
     * @return a {@link com.reactify.model.StepResult} object indicating success
     */
    public static StepResult success() {
        return new StepResult(true, null);
    }

    /**
     * <p>
     * failure.
     * </p>
     *
     * <p>
     * Static factory method to create a failed StepResult. This method takes an
     * error message as a parameter and returns an instance of StepResult indicating
     * failure along with the provided message.
     * </p>
     *
     * @param message
     *            a {@link java.lang.String} object providing details about the
     *            failure
     * @return a {@link com.reactify.model.StepResult} object indicating failure
     *         with an error message
     */
    public static StepResult failure(String message) {
        return new StepResult(false, message);
    }
}
