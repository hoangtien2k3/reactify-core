<h3 align="center">
<img src="reactify_banner.png" alt="reactify-core" width="300" />

<a href="https://github.com/hoangtien2k3/reactify/blob/main/docs/en/README.md">📚Docs</a> |
<a href="https://discord.com/invite/dhCKEJmG">💬Chat</a> |
<a href="https://github.com/hoangtien2k3/keycloak-auth-service">✨Live Demo</a>
</h3>

## Powerful Reactive Java Library for Spring WebFlux

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=ncloc)](https://sonarcloud.io/summary/overall?id=hoangtien2k3_reactify)
[![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest%20release)](https://mvnrepository.com/artifact/io.github.hoangtien2k3/reactify-core)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![OpenSSF Best Practices](https://www.bestpractices.dev/projects/9383/badge)](https://www.bestpractices.dev/projects/9383)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/reactify/actions)

🚀 Supercharge your reactive microservices with Reactify – a high-performance Java library built on Spring WebFlux and
Reactor Core, designed to streamline backend development with essential tools for modern reactive systems.

🔥 Features

- ✔ Seamless Keycloak Integration – Secure your apps effortlessly with built-in Keycloak support.
- ✔ Smart Caching – Boost performance with reactive caching mechanisms.
- ✔ Utility Helpers – Handy utilities to reduce boilerplate and speed up development.
- ✔ Reactive API Client – Effortless HTTP calls with non-blocking, reactive HTTP clients.
- ✔ Optimized for Microservices – Lightweight, fast, and built for scalable architectures.

## Table of contents

<details>
<summary><b>📖 Table of Contents</b></summary>

- [Getting started](#getting-started)
- [Tutorials](#tutorials)
- [Configs](#configs)
    - [reactify-core configs](#reactify-core-configs)
    - [reactify-cache configs](#reactify-cache-configs)
    - [reactify-client configs](#reactify-client-configs)
    - [reactify-utils configs](#reactify-utils-configs)
- [Features](#features)
    - [Local cache](#local-cache)
    - [Call api](#call-api)
    - [Data utils](#data-utils)
    - [More](#more)
- [Project demo](#project-demo)
- [Contributing](#contributing)
- [Star History](#star-history)
- [Contributors](#contributors)
- [License](#license)

</details>

## Getting Started

Add the dependency to your project build.gradle file or maven pom:

⬇️ Download Gradle and Maven

```groovy
dependencies {
    //Core reactive framework with Spring WebFlux integration 
    implementation("io.github.hoangtien2k3:reactify-core:1.2.9")

    //In-memory reactive caching for high-speed data access
    implementation("io.github.hoangtien2k3:reactify-cache:1.2.4")

    //Reactive REST/SOAP client with fault-tolerant design
    implementation("io.github.hoangtien2k3:reactify-client:1.2.4")

    //Use in minio (aws s3)
    implementation("io.github.hoangtien2k3:reactify-minio:1.2.5")

    //Essential utilities for reactive development
    implementation("io.github.hoangtien2k3:reactify-utils:1.2.5")
}
```

```xml
<dependencies>
   <!-- Core reactive framework with Spring WebFlux integration -->
   <dependency>
      <groupId>io.github.hoangtien2k3</groupId>
      <artifactId>reactify-core</artifactId>
      <version>1.2.9</version>
   </dependency>
   
   <!-- In-memory reactive caching for high-speed data access -->
   <dependency>
      <groupId>io.github.hoangtien2k3</groupId>
      <artifactId>reactify-cache</artifactId>
      <version>1.2.4</version>
   </dependency>
   
   <!-- Reactive REST/SOAP client with fault-tolerant design -->
   <dependency>
      <groupId>io.github.hoangtien2k3</groupId>
      <artifactId>reactify-client</artifactId>
      <version>1.2.4</version>
   </dependency>

   <!-- //Use in minio (aws s3) -->
   <dependency>
     <groupId>io.github.hoangtien2k3</groupId>
     <artifactId>reactify-minio</artifactId>
     <version>1.2.5</version>
   </dependency>
   
   <!-- Essential utilities for reactive development -->
   <dependency>
      <groupId>io.github.hoangtien2k3</groupId>
      <artifactId>reactify-utils</artifactId>
      <version>1.2.5</version>
   </dependency>
</dependencies>
```

## Tutorials

- Document: [https://reactify-sand.vercel.app](https://reactify-sand.vercel.app)

## Configs

### Reactify core configs

Config your project file `application.yml` or `application.properties`

- Required security configuration `Oauth2 with Keycloak`:
- Install keycloak and postgresql on docker [docker-compose](docker-compose.yml)
- Next configure in the `application.yml` resource file

```yml
spring:
  # Config OAuth2 Keycloak
  security:
    oauth2:
      client:
        provider:
          oidc:
            token-uri: ${keycloak.serverUrl}/realms/${keycloak.realm}/protocol/openid-connect/token
        registration:
          oidc:
            client-id: ${keycloak.clientId}
            client-secret: ${keycloak.clientSecret}
            authorization-grant-type: ${keycloak.grantType} #password || #client_credentials
      resourceserver:
        jwt:
          jwk-set-uri: ${keycloak.serverUrl}/realms/${keycloak.realm}/protocol/openid-connect/certs
      keycloak:
        client-id: ${keycloak.clientId}

#keycloak client config
keycloak:
  clientId: ezbuy-client
  clientSecret: mI92QDfvi20tZgFtjpRAPWu8TR6eMHmw
  realm: ezbuy-server
  serverUrl: http://localhost:8080
  grantType: password
  host: localhost
```

### Reactify cache configs

Use library with @LocalCache annotation

```java
@LocalCache(
    durationInMinute = 30, //cache duration in minutes
    maxRecord = 10000,     //maximum cache size
    autoCache = true       //true to enable automatic caching, false otherwise
)
@GetMapping("/students")
public Mono<List<Student>> getStudents() {
    var lstStudent = studentService.getAllStudents();
    if (DataUtil.isNullOrEmpty(lstStudent)) {
        return null;
    }
    return lstStudent;
}
```

### Reactify client configs

Configuration in `application.yml` or `application.properties` file

```yml
# web client config
client:
  #demo notification config
  notification:
    address: http://localhost:7878
    name: notiServiceClient
    pool:
      max-size: 100
      max-pending-acquire: 100
    timeout:
      read: 60000
      write: 1000
```

Using Rest/Soap API calls

- See configuration demo in
      client [reactify-test](https://github.com/hoangtien2k3/reactify-core/tree/main/reactify-test/src/main/java/com/reactify/test/client)

### Reactify utils configs

Just add `maven pom` or `build.gradle` and use the utilities available in reactify-utils

## Features

### Local cache

```java
@LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
@GetMapping("/students")
public Mono<List<Student>> getStudents() {
    var lstStudent = studentService.getAllStudents();
    if (DataUtil.isNullOrEmpty(lstStudent)) {
        return null;
    }
    return lstStudent;
}
```

### Call api

```java
public Mono<GeoPluginResponse> getBaseCurrency(String baseCurrency) {
    MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
    req.set("base_currency", baseCurrency);
    return baseRestClientQualifier.get(baseCurrencyClient, "/json.gp", null, req, GeoPluginResponse.class)
        .flatMap(optionalResponse -> optionalResponse
            .map(Mono::just)
            .orElseGet(Mono::empty)
        );
}
```

## Project demo

- Project using reactify-core library can be
  referenced: [https://github.com/hoangtien2k3/keycloak-auth-service](https://github.com/hoangtien2k3/keycloak-auth-service)

## Contributing

- We welcome contributions! Please check our [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

- If you would like to contribute to the development of this project, please follow our contribution guidelines.

![Alt](https://repobeats.axiom.co/api/embed/31a861bf21d352264c5c122808407abafb97b0ef.svg "Repobeats analytics image")

## Star History

<a href="https://star-history.com/#hoangtien2k3/fw-commons&Timeline">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
 </picture>
</a>

## Contributors

<a href="https://github.com/hoangtien2k3/reactify/graphs/contributors" target="_blank" rel="noopener noreferrer">
  <img src="https://contrib.rocks/image?repo=hoangtien2k3/reactify" alt="Contributors" />
</a>

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

```
Copyright 2024-2025 the original author Hoàng Anh Tiến.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
