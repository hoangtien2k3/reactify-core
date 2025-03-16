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
package com.reactify.config;

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The {@code CipherManager} class provides methods for encrypting messages
 * using RSA encryption. It utilizes the Bouncy Castle provider to support
 * enhanced cryptographic algorithms.
 * </p>
 *
 * <p>
 * This class is responsible for initializing the RSA cipher and performing
 * encryption operations on given messages using a public key.
 * </p>
 *
 * <p>
 * The encryption method converts the message to bytes, encrypts it using the
 * provided public key, and returns the encrypted message as a Base64 encoded
 * string.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class CipherManager {

    private final Cipher rsaCipher;

    /**
     * <p>
     * Constructs a {@code CipherManager} instance and initializes the RSA cipher.
     * </p>
     *
     * @throws java.lang.Exception
     *             if there is an issue initializing the cipher.
     */
    public CipherManager() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        this.rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
    }

    /**
     * <p>
     * Encrypts the given message using the specified public key.
     * </p>
     *
     * @param message
     *            the {@link java.lang.String} message to encrypt
     * @param publicKeyString
     *            the {@link java.lang.String} representation of the public key in
     *            Base64 format
     * @return a {@link java.lang.String} representing the Base64 encoded encrypted
     *         message
     */
    public String encrypt(String message, String publicKeyString) {
        try {
            PublicKey publicKey = stringToPublicKey(publicKeyString);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = rsaCipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.HASHING_PASSWORD_FAULT, ex.getMessage());
        }
    }

    /**
     * <p>
     * Converts a Base64 encoded public key string to a {@link PublicKey} instance.
     * </p>
     *
     * @param publicKeyString
     *            a {@link String} representation of the public key in Base64 format
     * @return a {@link PublicKey} instance derived from the provided string
     * @throws Exception
     *             if there is an issue during key conversion
     */
    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
