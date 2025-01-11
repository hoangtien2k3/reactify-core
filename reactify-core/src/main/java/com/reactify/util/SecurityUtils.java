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

import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.SignedJWT;
import com.reactify.constants.Constants;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.TokenUser;
import com.reactify.model.UserDTO;
import java.security.SignatureException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Utility class for security-related operations. Provides methods to extract
 * the current user, generate HMAC, and check authorization.
 *
 * @author hoangtien2k3
 */
public class SecurityUtils {

    /**
     * Constructs a new instance of {@code SecurityUtils}.
     */
    public SecurityUtils() {}

    /**
     * Retrieves the current authenticated user as a TokenUser.
     *
     * @return a Mono containing the TokenUser, or empty if no user is authenticated
     */
    public static Mono<TokenUser> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.justOrEmpty(extractUser(authentication)));
    }

    /**
     * Retrieves the token of the current authenticated user.
     *
     * @return a Mono containing the token as a String, or empty if no token is
     *         found
     */
    public static Mono<String> getTokenUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.justOrEmpty(extractToken(authentication)));
    }

    /**
     * Extracts the token from the given Authentication object.
     *
     * @param authentication
     *            the Authentication object from which to extract the token
     * @return the token as a String, or null if no token is found
     */
    public static String extractToken(Authentication authentication) {
        if (authentication == null) return null;
        Jwt jwt = (Jwt) authentication.getPrincipal();
        if (jwt == null) return null;
        return jwt.getTokenValue();
    }

    /**
     * Extracts the TokenUser from the given Authentication object.
     *
     * @param authentication
     *            the Authentication object from which to extract the user
     * @return the TokenUser, or null if no user is found
     */
    public static TokenUser extractUser(Authentication authentication) {
        if (authentication == null) return null;
        Jwt jwt = (Jwt) authentication.getPrincipal();
        if (jwt == null) return null;
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) return null;
        return TokenUser.builder()
                .username((String) claims.get(Constants.TokenProperties.USERNAME))
                .id((String) claims.get(Constants.TokenProperties.ID))
                .email((String) claims.get(Constants.TokenProperties.EMAIL))
                .name((String) claims.get(Constants.TokenProperties.NAME))
                .organizationId(DataUtil.safeToString(claims.get(Constants.TokenProperties.ORGANIZATION_ID)))
                .build();
    }

    /**
     * Generates an HMAC for the given data using the specified key and algorithm.
     *
     * @param data
     *            the data to be signed
     * @param key
     *            the key to be used for signing
     * @param algorithm
     *            the algorithm to be used for signing
     * @return the generated HMAC as a String
     * @throws java.security.SignatureException
     *             if an error occurs during HMAC generation
     */
    public static String hmac(String data, String key, String algorithm) throws SignatureException {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = String.valueOf(Base64.encode(rawHmac));
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e);
        }
        return result;
    }

    /**
     * Checks if the current user is authorized.
     *
     * @return a Mono containing true if the user is authorized, false otherwise
     */
    public static Mono<Boolean> isAuthorized() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication == null || authentication.getPrincipal() == null) {
                        return Mono.just(false);
                    }
                    return Mono.just(true);
                })
                .switchIfEmpty(Mono.just(false));
    }

    /**
     * Retrieves the UserDTO from the given access token.
     *
     * @param accessToken
     *            the access token from which to extract the user
     * @return the UserDTO, or null if an error occurs during extraction
     */
    public static UserDTO getUserByAccessToken(String accessToken) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(accessToken);
            String data = signedJWT.getPayload().toString();
            return ObjectMapperFactory.getInstance().readValue(data, UserDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
