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
package com.reactify;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Utility class for logging request and response data buffers. Provides methods
 * to log the content of DataBuffer objects. Supports various media types for
 * logging.
 *
 * @since 20/07/2024
 * @author hoangtien2k3
 */
public class LogUtils {

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inStream
     *            the InputStream to be converted
     * @return a byte array containing the data from the InputStream
     */
    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception ignore) {
        }
        return in_b;
    }
}
