package com.davidbejarcaceres.kotlincoroutinessuspend

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"]) //CORS security, Allows connecting to the API from external paths.
@RestController
@RequestMapping("/players")
class ProductControllerCoroutines(val repoPlayers: PlayersRepository) {

    @GetMapping(produces = ["application/json"])
    suspend fun getPlayers(): Flow<Player> = repoPlayers.findAll().asFlow()

    @GetMapping("/{id}", produces = ["application/json"])
    suspend fun getPlayersById(@PathVariable id: String): Player? = repoPlayers.findById(id).awaitFirstOrNull()

    @GetMapping("/dni/{dni}", produces = ["application/json"])
    suspend fun getPlayersByDNI(@PathVariable("dni") dni : String): Player? = repoPlayers.findByDni(dni)?.awaitFirstOrNull()

    @GetMapping("/name/{name}", produces = ["application/json"])
    suspend fun getPlayersByName(@PathVariable("name") name : String): Flow<Player>? = repoPlayers.findByName(name)?.asFlow()

    @GetMapping("/lastname/{lastname}", produces = ["application/json"])
    suspend fun getPlayersBylastname(@PathVariable("lastname") lastname : String): Flow<Player>? = repoPlayers.findByLastname(lastname)?.asFlow()

    // @ApiOperation(value = "Add new Player", response = Players::class)
    @PostMapping(value = ["/addPlayer"], produces = ["application/json"])
    suspend fun createNewPLayer(@RequestBody newPlayer: Player): ResponseEntity<Any> {
        if (!newPlayer._id.isNullOrEmpty() && getPlayersById(newPlayer._id!!) != null) {
            return ResponseEntity.status(400).body("ERROR: Player with same ID already found")
        }

        if (getPlayersByDNI(newPlayer.dni) != null) {
            return ResponseEntity.status(400).body("ERROR: Player with same DNI found")
        }
        repoPlayers.save(newPlayer.apply { _id = ObjectId.get().toHexString() })
        return ResponseEntity.status(201).body(newPlayer)
    }


    //@ApiOperation(value = "Add new Player or array of players", response = Players::class)
    @PostMapping(value = [""], produces = ["application/json"])
    suspend fun createNewPLayerOrMultiplePlayers(@RequestBody newPlayer: Any): ResponseEntity<Any> {
        val mapper = ObjectMapper()
        try {
            //Try to convert single object (LinkedHashMap) to Json String (String) and back to Players
            val jsonInString: String = mapper.writeValueAsString(newPlayer)
            val playerSerielized: Player = mapper.readValue(jsonInString, Player::class.java)

            if (!playerSerielized._id.isNullOrEmpty() && getPlayersById(playerSerielized._id!!) != null) {
                return ResponseEntity.status(400).body("ERROR: Player with same ID already found")
            }

            if (getPlayersByDNI(playerSerielized.dni) != null) {
                return ResponseEntity.status(400).body("ERROR: Player with same DNI found")
            }
            repoPlayers.save(playerSerielized.apply { _id = ObjectId.get().toHexString() })
            return ResponseEntity.status(201).body(playerSerielized)
        } catch (error: Throwable) {
            print(error)
        }

        try {//Try to convert multiple objects (Array<LinkedHashMap>) to Json String (String) and back to Array<Players>
            val jsonInString: String = mapper.writeValueAsString(newPlayer)
            val listOfPlayers: Array<Player> = mapper.readValue(jsonInString, Array<Player>::class.java);

            if (listOfPlayers is Array<Player> && !listOfPlayers.isEmpty()) {
                listOfPlayers.forEach { it ->
                    if (it is Player && it != null) {
                        if (!it._id.isNullOrEmpty() && getPlayersById(it._id!!) != null) {
                            return ResponseEntity.status(400).body("ERROR: Player with same ID ${it._id} already found")
                        }

                        if (getPlayersByDNI(it.dni) != null) {
                            return ResponseEntity.status(400).body("ERROR: Player with same DNI ${it.dni} found")
                        }
                    }
                }
                repoPlayers.saveAll(listOfPlayers.toMutableList()) // Important, convert to mutable list before saving to Mongo
                return ResponseEntity.status(201).body(listOfPlayers)
            }
        } catch (e: Exception) {
            return ResponseEntity.status(400).body("Error Parsing Array of Players")
        }

        return ResponseEntity.status(400).body("Not Saved")
    }

    //@ApiOperation(value = "Update player info", response = Player::class)
    @PutMapping(value = ["/{id}"], produces = ["application/json"])
    @PatchMapping
    suspend fun updatePlayerInfo(@PathVariable("id") id: String, @RequestBody player: Player): ResponseEntity<Any> {
        if (!id.isNullOrEmpty()) {
            val originalPlayer: Player? = getPlayersById(id)
            if (originalPlayer != null) {
                originalPlayer.apply {
                    name = if (player.name != null && player.name != originalPlayer.name && player.name.length > 2 ) player.name else originalPlayer.name
                    lastname = if (player.lastname != null && player.lastname != originalPlayer.lastname && player.lastname.length > 2 ) player.lastname else originalPlayer.lastname
                    age = if (player.age != null && player.age != originalPlayer.age ) player.age else originalPlayer.age
                    dni = if (player.dni != null && player.dni != originalPlayer.dni && player.dni.length > 6 ) player.dni else originalPlayer.dni
                }
                repoPlayers.save(originalPlayer)
                return ResponseEntity.ok().body(null)
            } else {
                return ResponseEntity.status(404).body(null)
            }
        }
        return ResponseEntity.status(404).body(null)
    }

    @DeleteMapping(value = ["/{id}"])
    suspend fun deletePlayer(@PathVariable id: String) {
        val originalPlayer: Player? = getPlayersById(id)
        if (originalPlayer != null) {
            repoPlayers.delete(getPlayersById(id)!!)
        } else {
            ResponseEntity.ok()
        }
    }

    @GetMapping("/helloworld/{name}")
    suspend fun helloWorldUser(@PathVariable("name") name: String?): String = "Hellow World $name";

    @GetMapping("/hello/{name}")
    suspend fun findOne(@PathVariable name: String): String = GlobalScope.async(Dispatchers.Default, start = CoroutineStart.LAZY) {
        delay(5000)
        "Hola $name"
    }.await()
}