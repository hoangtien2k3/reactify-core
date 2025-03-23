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
package com.reactify.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * <p>
 * The {@code PasswordService} class provides a singleton instance for
 * encrypting plaintext strings using SHA-1 hashing algorithm and returning the
 * encrypted string in Base64 format.
 * </p>
 *
 * <p>
 * This class follows the Singleton design pattern to ensure that only one
 * instance of {@code PasswordService} is created throughout the application.
 * </p>
 *
 * <p>
 * Note: SHA-1 is considered weak for cryptographic purposes. It is recommended
 * to use stronger hashing algorithms (e.g., SHA-256) for better security.
 * </p>
 *
 * @author hoangtien2k3
 */
public class PasswordService {

    private static PasswordService instance;

    /**
     * <p>
     * Encrypts the given plaintext string.
     * </p>
     *
     * <p>
     * This method uses the SHA-1 hashing algorithm to hash the provided plaintext.
     * The resulting byte array is then encoded using Base64 encoding.
     * </p>
     *
     * @param plaintext
     *            a {@link java.lang.String} object representing the plaintext to be
     *            encrypted.
     * @return a {@link java.lang.String} object representing the Base64 encoded
     *         encrypted text.
     * @throws java.lang.Exception
     *             if there is an error during the encryption process, such as if
     *             the SHA-1 algorithm is not available.
     */
    public synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.update(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] raw = md.digest();
        return new String(Base64.getEncoder().encode(raw));
    }

    /**
     * <p>
     * Retrieves the singleton instance of the {@code PasswordService}.
     * </p>
     *
     * <p>
     * This method provides a synchronized access to ensure that only one instance
     * of the {@code PasswordService} is created and shared throughout the
     * application.
     * </p>
     *
     * @return a {@link com.reactify.common.PasswordService} object representing the
     *         singleton instance.
     */
    public static synchronized PasswordService getInstance() {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
}
