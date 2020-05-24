# Spring Boot Kotlin Coroutines Reactive MongoDB

 Spring Boot API implemented using Reactive an non-blocking IO from MongoDB using Coroutines.

## Run the project from terminal

---------------------

```shell
$ gradle Bootrun

      .   ____          _            __ _ _
    /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
    \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
    =========|_|==============|___/=/_/_/_/
    :: Spring Boot ::        (v2.3.0.RELEASE)
```

## Reactive Spring Boot Web-flux

---------------------
Spring boot uses Kotlin as first class citizen, and has support for coroutines (lightweight threads) to be able to use multi-threading/non-blocking operations.
Web-flux (replacing spring web MVC) uses by default a Netty embedded server capable of managing reactive async programming (https://netty.io/).

As mentioned in (https://medium.com/faun/spring-mongodb-reactive-programming-case-study-c2077169aeec) Spring Framework 5 includes a new spring-webflux module. The module contains support for reactive HTTP and WebSocket clients as well as for reactive server web applications including REST, HTML browser, and WebSocket style interactions.

```gradle

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}
```

Handling return types like Flux and Mono, Kotlin has added support to easly convert between those types to Kotlin Flow< T >? and < T >? and use them like normal Kotlin classes in coroutines:

https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-reactive/kotlinx.coroutines.reactive/org.reactivestreams.-publisher/index.html.

## MongoDB Reactive non-blocking queries

---------------------
Mongo also added support for Reactive programming a while ago, making IO Database access non-blocking:
  - https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.reactive

```gradle
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
}
```
## Benefits of Reactive non-blocking

---------------------
Most of it benefits comes from performance and real-time applications. When we are using reactive, will be capable to finish more requests/s, also with a lower failure rate.
For more information and benchmarks about performance you can check here:
  - Reactive vs Non-Reactive Spring Performance: https://frandorado.github.io/spring/2019/06/26/spring-reactive-vs-non-reactive-performance.html
  - Spring Boot performance benchmarks with Tomcat, Undertow and Webflux (Netty): https://dev.to/azure/spring-boot-performance-benchmarks-with-tomcat-undertow-and-webflux-4d8k

## OpenAPI 3.0 (a.k.a. Swagger) to document the API

---------------------
 To document the API and test the end-points of the controller (https://www.baeldung.com/spring-rest-openapi-documentation)
 
 