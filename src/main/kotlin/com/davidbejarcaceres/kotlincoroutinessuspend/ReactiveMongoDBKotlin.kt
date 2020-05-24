package com.davidbejarcaceres.kotlincoroutinessuspend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveMongoDBKotlin

fun main(args: Array<String>) {
	runApplication<ReactiveMongoDBKotlin>(*args)
}
