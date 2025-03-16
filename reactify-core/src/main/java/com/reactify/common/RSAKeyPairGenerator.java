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

import java.security.*;
import java.util.Base64;
import lombok.Getter;

/**
 * <p>
 * The {@code RSAKeyPairGenerator} class is designed to generate a pair of RSA
 * keys, including a public key and a private key, suitable for asymmetric
 * encryption. For more details on RSA encryption and decryption, see <a href=
 * "https://www.devglan.com/java8/rsa-encryption-decryption-java">here</a>.
 * </p>
 *
 * <p>
 * This class utilizes the Java Security API to generate the RSA key pair with a
 * key size of 1024 bits.
 * </p>
 *
 * @author hoangtien2k3
 */
public class RSAKeyPairGenerator {

    @Getter
    private final PrivateKey privateKey;

    @Getter
    private final PublicKey publicKey;

    /**
     * <p>
     * Constructs an instance of {@code RSAKeyPairGenerator}. This constructor
     * initializes the key pair generator and generates the RSA key pair.
     * </p>
     *
     * @throws java.security.NoSuchAlgorithmException
     *             if the RSA algorithm is not available.
     */
    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        // RSA to indicate Asymmetric Encryption (public-key cryptography)
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        // KeySize: 1024 bit
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    /**
     * <p>
     * The main method is the entry point for the program. It generates an RSA key
     * pair and prints the public and private keys in Base64 encoded format.
     * </p>
     *
     * @param args
     *            an array of {@link java.lang.String} objects, not used in this
     *            implementation.
     * @throws java.security.NoSuchAlgorithmException
     *             if the RSA algorithm is not available.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        String publicKey = Base64.getEncoder()
                .encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());
        String privateKey = Base64.getEncoder()
                .encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());
        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
    }
}
