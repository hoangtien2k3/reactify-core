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
package com.reactify.constants;

public final class AuthConstants {
    public static final class OAuth {
        public static final String AUTHOR_CODE = "authorization_code";
        public static final String UMA_TICKET = "urn:ietf:params:oauth:grant-type:uma-ticket";
        public static final String RESPONSE_MODE_PERMISSION = "permissions";
        public static final String REDIRECT_URI = "http://localhost:3000/callback";
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";
        public static final String CLIENT_CREDENTIALS = "client_credentials";
    }
}
