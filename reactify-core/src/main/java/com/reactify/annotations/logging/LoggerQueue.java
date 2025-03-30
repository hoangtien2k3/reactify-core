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
package com.reactify.annotations.logging;

import brave.Span;
import com.reactify.model.logging.LoggerDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import reactor.util.context.Context;

/**
 * <p>
 * The {@code LoggerQueue} class is a singleton that provides a thread-safe
 * queue for logging operations using an
 * {@link java.util.concurrent.ArrayBlockingQueue} to manage instances of
 * {@link com.reactify.model.logging.LoggerDTO}. It tracks the number of
 * successful and failed logging attempts.
 * </p>
 *
 * <p>
 * This class ensures that logging tasks are queued efficiently and can be
 * processed asynchronously, making it suitable for high-throughput logging
 * scenarios.
 * </p>
 *
 * <p>
 * The implementation follows the singleton design pattern to ensure that only
 * one instance of the {@code LoggerQueue} exists throughout the application.
 * </p>
 *
 * <p>
 * This class is annotated with {@link lombok.extern.slf4j.Slf4j} for logging
 * functionality.
 * </p>
 *
 * @author hoangtien2k3
 */
public class LoggerQueue {

    private static LoggerQueue mMe = null;
    private final ArrayBlockingQueue<LoggerDTO> myQueue;

    private int countFalse = 0;
    private int countSuccess = 0;

    public int getCountFalse() {
        return countFalse;
    }

    public int getCountSuccess() {
        return countSuccess;
    }

    /**
     * <p>
     * getInstance.
     * </p>
     *
     * @return a {@link com.reactify.annotations.logging.LoggerQueue} object
     */
    public static LoggerQueue getInstance() {
        if (mMe == null) {
            mMe = new LoggerQueue();
        }
        return mMe;
    }

    /**
     * Constructs a new instance of {@code UnmarshallerFactory}.
     */
    private LoggerQueue() {
        myQueue = new ArrayBlockingQueue<>(100000) {};
    }

    /**
     * <p>
     * clearQueue.
     * </p>
     */
    public void clearQueue() {
        myQueue.clear();
    }

    /**
     * <p>
     * getQueue.
     * </p>
     *
     * @return a {@link com.reactify.model.logging.LoggerDTO} object
     */
    public LoggerDTO getQueue() {
        return myQueue.poll();
    }

    /**
     * <p>
     * addQueue.
     * </p>
     *
     * @param task
     *            a {@link com.reactify.model.logging.LoggerDTO} object
     * @return a boolean
     */
    public boolean addQueue(LoggerDTO task) {
        if (myQueue.add(task)) {
            countSuccess++;
            return true;
        }
        countFalse++;
        return false;
    }

    /**
     * <p>
     * addQueue.
     * </p>
     *
     * @param contextRef
     *            a {@link java.util.concurrent.atomic.AtomicReference} object
     * @param newSpan
     *            a {@link brave.Span} object
     * @param service
     *            a {@link java.lang.String} object
     * @param startTime
     *            a {@link java.lang.Long} object
     * @param endTime
     *            a {@link java.lang.Long} object
     * @param result
     *            a {@link java.lang.String} object
     * @param obj
     *            a {@link java.lang.Object} object
     * @param logType
     *            a {@link java.lang.String} object
     * @param actionType
     *            a {@link java.lang.String} object
     * @param args
     *            an array of {@link java.lang.Object} objects
     * @param title
     *            a {@link java.lang.String} object
     */
    public void addQueue(
            AtomicReference<Context> contextRef,
            Span newSpan,
            String service,
            Long startTime,
            Long endTime,
            String result,
            Object obj,
            String logType,
            String actionType,
            Object[] args,
            String title) {
        try {
            if (myQueue.add(new LoggerDTO(
                    contextRef, newSpan, service, startTime, endTime, result, obj, logType, actionType, args, title))) {
                countSuccess++;
                return;
            }
        } catch (Exception ignore) {
        }
        countFalse++;
    }

    /**
     * <p>
     * getRecords.
     * </p>
     *
     * @return a {@link java.util.List} object
     */
    public List<LoggerDTO> getRecords() {
        List<LoggerDTO> records = new ArrayList<>();
        myQueue.drainTo(records, 100000);
        return records;
    }

    /**
     * <p>
     * getQueueSize.
     * </p>
     *
     * @return a int
     */
    public int getQueueSize() {
        return myQueue.size();
    }

    /**
     * <p>
     * resetCount.
     * </p>
     */
    public void resetCount() {
        countSuccess = 0;
        countFalse = 0;
    }
}
