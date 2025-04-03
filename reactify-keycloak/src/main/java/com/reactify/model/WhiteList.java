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
package com.reactify.model;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * WhiteList class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class WhiteList {

    /**
     * uri the URI to be whitelisted
     */
    private final String uri;

    /**
     * methods the list of HTTP methods allowed for the URI
     */
    private final List<String> methods;

    public WhiteList(String uri, List<String> methods) {
        this.uri = uri;
        this.methods = methods;
    }

    public String getUri() {
        return uri;
    }

    public List<String> getMethods() {
        return methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteList whiteList)) return false;
        return Objects.equals(getUri(), whiteList.getUri()) && Objects.equals(getMethods(), whiteList.getMethods());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getMethods());
    }
}
