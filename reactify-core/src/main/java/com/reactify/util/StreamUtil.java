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
package com.reactify.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * StreamUtil class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class StreamUtil {

    /**
     * Constructs a new instance of {@code StreamUtil}.
     */
    public StreamUtil() {}

    /**
     * <p>
     * streamToByteArray.
     * </p>
     *
     * @param inStream
     *            a {@link java.io.InputStream} object
     * @return an array of {@link byte} objects
     */
    public static byte[] streamToByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            log.error("streamToByteArray error ", e);
        }
        return in_b;
    }
}
