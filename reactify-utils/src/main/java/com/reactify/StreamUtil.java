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

public class StreamUtil {

    /**
     * Converts an {@link java.io.InputStream} into a byte array.
     * <p>
     * This method reads data from the input stream in chunks and writes it to a
     * {@link java.io.ByteArrayOutputStream}. Once all data has been read, it is
     * converted into a byte array and returned.
     * </p>
     *
     * @param inStream
     *            The input stream to be read. Must not be <code>null</code>.
     * @return A byte array containing the data read from the input stream. If an
     *         error occurs, an empty byte array is returned.
     */
    public static byte[] streamToByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception ignored) {

        }
        return in_b;
    }
}
