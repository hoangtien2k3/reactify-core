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
package com.reactify.test.model;

import lombok.Data;

@Data
public class GeoPluginResponse {
    private String geoplugin_request;
    private int geoplugin_status;
    private String geoplugin_delay;
    private String geoplugin_credit;
    private String geoplugin_city;
    private String geoplugin_region;
    private String geoplugin_regionCode;
    private String geoplugin_regionName;
    private String geoplugin_areaCode;
    private String geoplugin_dmaCode;
    private String geoplugin_countryCode;
    private String geoplugin_countryName;
    private int geoplugin_inEU;
    private boolean geoplugin_euVATrate;
    private String geoplugin_continentCode;
    private String geoplugin_continentName;
    private String geoplugin_latitude;
    private String geoplugin_longitude;
    private String geoplugin_locationAccuracyRadius;
    private String geoplugin_timezone;
    private String geoplugin_currencyCode;
    private String geoplugin_currencySymbol;
    private String geoplugin_currencySymbol_UTF8;
    private double geoplugin_currencyConverter;
}
