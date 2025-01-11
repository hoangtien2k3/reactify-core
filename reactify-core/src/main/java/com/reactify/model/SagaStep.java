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

import reactor.core.publisher.Mono;

/**
 * <p>
 * The SagaStep interface defines the contract for a step within a saga process.
 * Each implementation of this interface represents an individual operation that
 * can be executed as part of a larger saga. The interface provides methods for
 * executing, reverting, and checking the completion status of the step.
 * </p>
 *
 * <p>
 * Implementing classes should define the specific logic for each step,
 * including what it means for the step to be completed and how to revert its
 * actions in case of a failure during the saga execution.
 * </p>
 *
 * @author hoangtien2k3
 */
public interface SagaStep {

    /**
     * <p>
     * complete.
     * </p>
     *
     * <p>
     * This method checks if the step has been completed successfully. It should
     * return true if the step's operations were successful, allowing for rollback
     * procedures to skip completed steps.
     * </p>
     *
     * @return a boolean indicating whether the step is complete
     */
    boolean complete();

    /**
     * <p>
     * execute.
     * </p>
     *
     * <p>
     * This method performs the primary operation of the step. It should return a
     * {@link reactor.core.publisher.Mono} containing a
     * {@link com.reactify.model.StepResult} object that indicates the success or
     * failure of the operation and any relevant messages.
     * </p>
     *
     * @return a {@link reactor.core.publisher.Mono} object representing the
     *         execution result of the step
     */
    Mono<StepResult> execute();

    /**
     * <p>
     * revert.
     * </p>
     *
     * <p>
     * This method reverses the actions taken by the step. It should return a
     * {@link reactor.core.publisher.Mono} containing a boolean indicating whether
     * the rollback was successful. This method is called during the rollback
     * process of the saga when a step fails.
     * </p>
     *
     * @return a {@link reactor.core.publisher.Mono} object indicating the success
     *         of the revert operation
     */
    Mono<Boolean> revert();
}
