/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
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

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * <p>
 * The {@code RSACryptoExample} class provides methods for RSA encryption and
 * decryption using public and private keys. It includes functionalities for
 * converting string representations of keys to their respective
 * {@link java.security.PublicKey} and {@link java.security.PrivateKey} objects,
 * as well as methods for encrypting and decrypting messages.
 * </p>
 *
 * <p>
 * This class utilizes the Bouncy Castle provider for cryptographic operations.
 * </p>
 *
 * <p>
 * Note: Ensure that the Bouncy Castle library is included in your project's
 * dependencies.
 * </p>
 *
 */
public class RSACryptoExample {

    /**
     * Converts a Base64 encoded string representation of a public key to a
     * {@link PublicKey} object.
     *
     * @param publicKeyString
     *            a {@link String} containing the Base64 encoded public key.
     * @return a {@link PublicKey} object derived from the given string.
     * @throws Exception
     *             if an error occurs during key conversion.
     */
    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Converts a Base64 encoded string representation of a private key to a
     * {@link PrivateKey} object.
     *
     * @param privateKeyString
     *            a {@link String} containing the Base64 encoded private key.
     * @return a {@link PrivateKey} object derived from the given string.
     * @throws Exception
     *             if an error occurs during key conversion.
     */
    private static PrivateKey stringToPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Encrypts the given message using the provided public key.
     *
     * @param message
     *            a {@link String} message to be encrypted.
     * @param stringPublicKey
     *            a {@link String} containing the Base64 encoded public key.
     * @return a byte array containing the encrypted message.
     * @throws Exception
     *             if an error occurs during the encryption process.
     */
    private static byte[] encrypt(String message, String stringPublicKey) throws Exception {
        PublicKey publicKey = stringToPublicKey(stringPublicKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    /**
     * Decrypts the given byte array using the provided private key.
     *
     * @param encryptedBytes
     *            a byte array containing the encrypted message.
     * @param stringPrivateKey
     *            a {@link String} containing the Base64 encoded private key.
     * @return a {@link String} representing the decrypted message.
     * @throws Exception
     *             if an error occurs during the decryption process.
     */
    private static String decrypt(byte[] encryptedBytes, String stringPrivateKey) throws Exception {
        PrivateKey privateKey = stringToPrivateKey(stringPrivateKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    /**
     * Generates a new RSA key pair.
     *
     * @return a {@link KeyPair} object containing the generated public and private
     *         keys.
     * @throws NoSuchAlgorithmException
     *             if the RSA algorithm is not available.
     * @throws NoSuchProviderException
     *             if the Bouncy Castle provider is not available.
     */
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }
}
