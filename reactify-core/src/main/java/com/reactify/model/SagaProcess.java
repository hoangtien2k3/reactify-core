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

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * <p>
 * The Abstract SagaProcess class provides a template for executing a series of
 * steps in a saga pattern. It defines the structure for saga execution and
 * rollback procedures. Subclasses must implement the {@link #getSteps()} method
 * to provide the specific steps that make up the saga.
 * </p>
 *
 * <p>
 * This class utilizes Reactor's reactive programming model, allowing for
 * non-blocking execution of the saga steps and handling of potential errors
 * during execution. It ensures that if any step fails, the process is rolled
 * back to maintain data consistency.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public abstract class SagaProcess {

    /**
     * Constructs a new instance of {@code SagaProcess}.
     */
    public SagaProcess() {}

    /**
     * <p>
     * getSteps.
     * </p>
     *
     * <p>
     * This method must be implemented by subclasses to return a list of
     * {@link com.reactify.model.SagaStep} objects representing the steps to be
     * executed in the saga. Each step should implement the logic for its respective
     * operation.
     * </p>
     *
     * @return a {@link java.util.List} of {@link com.reactify.model.SagaStep}
     *         objects
     */
    public abstract List<SagaStep> getSteps();

    /** List of executed steps for rollback purposes. */
    protected final List<SagaStep> executedStep = new LinkedList<>();

    /**
     * <p>
     * execute.
     * </p>
     *
     * <p>
     * This method initiates the execution of the saga steps in order. It logs the
     * start of execution, processes each step, and handles success or failure. If a
     * step fails, it triggers the rollback process and propagates the error.
     * </p>
     *
     * @return a {@link reactor.core.publisher.Flux} object that represents the
     *         execution flow
     */
    public Flux<?> execute() {
        log.info("==================Start execute================");
        return Flux.fromIterable(getSteps())
                .flatMap(SagaStep::execute) // Execute each step
                .handle((stepResult, synchronousSink) -> {
                    if (stepResult.isSuccess()) {
                        synchronousSink.next(true); // Step succeeded
                    } else {
                        synchronousSink.error(
                                new BusinessException(CommonErrorCode.BAD_REQUEST, stepResult.getMessage())); // Handle
                        // failure
                    }
                })
                .subscribeOn(Schedulers.boundedElastic()) // Run on a bounded elastic scheduler
                .onErrorResume(ex -> revert().then(Mono.error(ex))); // Rollback on error
    }

    /**
     * <p>
     * revert.
     * </p>
     *
     * <p>
     * This method is called to roll back the executed steps in reverse order. It
     * filters out steps that are already completed and executes their revert logic
     * to undo any changes made during the saga.
     * </p>
     *
     * @return a {@link reactor.core.publisher.Flux} object that represents the
     *         rollback flow
     */
    public Flux<?> revert() {
        log.info("==================Start rollback================");
        Collections.reverse(getSteps()); // Reverse the order of steps for rollback
        return Flux.fromIterable(getSteps())
                .filter(SagaStep::complete) // Filter only the completed steps
                .flatMap(SagaStep::revert); // Execute revert logic for each step
    }
}
