<h3 align="center">
<img src="docs/images/reactify_banner.png" alt="Ezbuy" width="300" />

<a href="https://github.com/hoangtien2k3/reactify/blob/main/docs/en/README.md">📚Docs</a> |
<a href="https://discord.com/invite/dhCKEJmG">💬Chat</a> |
<a href="https://github.com/hoangtien2k3/keycloak-auth-service">✨Live Demo</a>
</h3>

##

Reactify-core `Java lib` with spring boot framework, Supports using keycloak, filter, trace log, cached, minio
server, exception handler, validate and call API with webclient

This README provides quickstart instructions on running [`reactify-core`]() on bare metal project spring boot.

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)

[![CircleCI](https://circleci.com/gh/hoangtien2k3/reactify.svg?style=svg)](https://app.circleci.com/pipelines/github/hoangtien2k3/reactify)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=ncloc)](https://sonarcloud.io/summary/overall?id=hoangtien2k3_reactify)
[![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest%20release)](https://mvnrepository.com/artifact/io.github.hoangtien2k3/reactify)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![OpenSSF Best Practices](https://www.bestpractices.dev/projects/9383/badge)](https://www.bestpractices.dev/projects/9383)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/reactify/actions)

## Getting Started

Gradle is the only supported build configuration, so just add the dependency to your project build.gradle file:

⬇️ Download Gradle and Maven

```kotlin
dependencies {
    implementation("io.github.hoangtien2k3:reactify-core:1.1.7")
}
```

```maven
<dependency>
   <groupId>io.github.hoangtien2k3</groupId>
   <artifactId>reactify-core</artifactId>
   <version>1.1.7</version>
</dependency>
```

The latest `reactify` version
is: [![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest)](https://mvnrepository.com/artifact/io.github.hoangtien2k3/reactify)

The latest stable lib `reactify` version is: latestVersion
Click [here](https://central.sonatype.com/namespace/io.github.hoangtien2k3) for more information on reactify.

1. Correct and complete setup to start the program `application.yml` or `application.properties`
   with [CONFIG](src/main/resources/application.yml)

2. The [reference documentation]() includes detailed [installation instructions]() as well as a
   comprehensive [getting started]() guide.

Here is a quick teaser of a complete Spring Boot application in Java:

## Start Using Lib `reactify-core`

### 1. Use annotation [`@ComponentScan`]() to scan all libraries

```java

@ComponentScan(basePackages = {
        "com.reactify.*",           // add default: com.reactify.*
        "com.example.myproject"     // varies depending on your project
})
@SpringBootApplication
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
```

### 2. Config your project file `application.yml` or `application.properties`

```yml
# spring config
spring:
  main:
    web-application-type: reactive
    allow-bean-definition-overriding: true
  messages:
    basename: i18n/messages

  #connect db R2DBC PostgreSQL
  r2dbc:
    url: r2dbc:postgresql://localhost:5434/auth
    username: admin
    password: admin
    pool:
      max-size: 10
      initial-size: 5

  # Config connect Keycloak
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

# Web client config
client:
  #keycloak
  keycloak:
    address: http://localhost:8080/realms/ezbuy-server/protocol/openid-connect
    name: keycloak
    auth:
      client-id: ezbuy-client
      client-secret: mI92QDfvi20tZgFtjpRAPWu8TR6eMHmw
  #notification
  notification:
    internal-oauth: true
    address: http://localhost:7777/v1/transmission
    name: notiServiceClient
    pool:
      max-size: 100
      max-pending-acquire: 100
    timeout:
      read: 60000
      write: 1000

# Unauthenticated endpoints config
application:
  http-logging:
    request:
      enable: true
      header: true
      param: true
      body: true
    response:
      enable: true
      body: true
  whiteList:
    - uri: /v1/auth/generate-otp
      methods:
        - POST
    - uri: /**
      methods:
        - OPTIONS
    - uri: /v1/auth/get-all
      methods:
        - GET
  data:
    sync-data:
      limit: 500

#keycloak client config
keycloak:
  clientId: ezbuy-client
  clientSecret: mI92QDfvi20tZgFtjpRAPWu8TR6eMHmw
  realm: ezbuy-server
  serverUrl: http://localhost:8080
  grantType: password
  host: localhost

# minio server config
minio:
  bucket: ezbuy-bucket
  enabled: true
  baseUrl: http://localhost:9000
  publicUrl: http://localhost:9000/ezbuy-bucket
  accessKey: 4DoaZ0KdzpXdDlVK104t
  secretKey: nuRiQUIJNVygMOHhmtR4LT1etAa7F8PQOsRGP5oj
  private:
    bucket: ezbuy-private
```

### 3. After completing the configuration, start running the project.

```yaml
  # Using Maven
  mvn spring-boot:run

  # Using Gradle
  gradle bootRun
```

### 4. Refer to the following project, used
`reactify-core` library for webflux microservice project: [keycloak-auth-service](https://github.com/hoangtien2k3/keycloak-auth-service)

## Contributing

If you would like to contribute to the development of this project, please follow our contribution guidelines.

![Alt](https://repobeats.axiom.co/api/embed/31a861bf21d352264c5c122808407abafb97b0ef.svg "Repobeats analytics image")

## Star History

<a href="https://star-history.com/#hoangtien2k3/fw-commons&Timeline">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
 </picture>
</a>

## Contributors ✨

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
