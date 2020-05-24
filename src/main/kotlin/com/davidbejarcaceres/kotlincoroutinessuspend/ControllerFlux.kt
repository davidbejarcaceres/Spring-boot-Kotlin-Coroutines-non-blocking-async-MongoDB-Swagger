package com.davidbejarcaceres.kotlincoroutinessuspend

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@CrossOrigin(origins = ["*"]) //CORS security, Allows connecting to the API from external paths.
@RestController
@RequestMapping("/flux")
class ControllerFlux(val repoPlayers: PlayersRepository, val jsonMapper : ObjectMapper) {

    @GetMapping("/hello/{name}")
    fun helloFlux(@PathVariable("name") name: String?): Flux<String> = Flux.interval(
            Duration.ofSeconds(1))
            .take(5)
            .delayElements(Duration.ofSeconds(2))
            .map { "Hola $name  (${java.time.LocalDateTime.now().hour}:${java.time.LocalDateTime.now().second}) \n\n" }

    @GetMapping("")
    fun getPlayersSlowFlux(): Any? = repoPlayers.findAll()
            .delayElements(Duration.ofMillis(500) )
            .map { "Player ID: ${it._id} " +
                    "name: ${it.name}, " +
                    "lastname: ${it.lastname}, " +
                    "dni: ${it.dni} \n"}

    @GetMapping("/{id}")
    fun getPlayersByIdSlowFlux(@PathVariable("id") id: String): Mono<Player>? =
            repoPlayers.findBy_id(id)

    @GetMapping("/name/{name}")
    fun getPlayersByNameSlowFlux(@PathVariable("name") name: String): Flux<String>? =
            repoPlayers.findByName(name)?.delayElements(Duration.ofMillis(500) )?.map { it.toString()}

    @GetMapping("/lastname/{lastname}")
    fun getPlayersByLastnameSlowFlux(@PathVariable("lastname") lastname: String): Flux<String>? =
            repoPlayers.findByLastname(lastname)?.delayElements(Duration.ofMillis(500) )?.map { it.toString()}

    @GetMapping("/dni/{dni}")
    fun getPlayersByDniSlowFlux(@PathVariable("dni") dni: String): Mono<Player>? =
            repoPlayers.findByDni(dni)
}