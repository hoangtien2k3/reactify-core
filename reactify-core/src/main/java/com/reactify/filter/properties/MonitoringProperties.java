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
package com.reactify.filter.properties;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;

/**
 * <p>
 * The MonitoringProperties class is a record that holds configuration
 * properties for monitoring features in an application. This includes a flag to
 * enable monitoring and a MeterRegistry instance used for collecting and
 * reporting metrics.
 * </p>
 *
 * <p>
 * The default constructor initializes monitoring as enabled and uses a
 * LoggingMeterRegistry for logging metrics to the console or log file.
 * </p>
 *
 * @param isEnable
 *            a flag indicating whether monitoring is enabled
 * @param meterRegistry
 *            the MeterRegistry instance used for collecting and reporting
 *            metrics
 * @author hoangtien2k3
 */
public record MonitoringProperties(boolean isEnable, MeterRegistry meterRegistry) {
    /**
     * <p>
     * Constructor for MonitoringProperties.
     * </p>
     */
    public MonitoringProperties() {
        this(true, new LoggingMeterRegistry());
    }
}
