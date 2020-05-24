package com.davidbejarcaceres.kotlincoroutinessuspend

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.core.mapping.Document
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlayersRepository : ReactiveMongoRepository<Player, String> {
    fun findBy_id(_id: String): Mono<Player>?
    fun findByName(name: String): Flux<Player>?
    fun findByDni(dni: String): Mono<Player>?
    fun findByLastname(lastname: String): Flux<Player>?
}