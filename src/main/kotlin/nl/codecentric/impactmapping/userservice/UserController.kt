package nl.codecentric.impactmapping.userservice

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.stream.LongStream

@RestController
class UserController(val userRepository: UserRepository){
    @GetMapping("/")
    fun findAll(): Flux<User> {
        return Flux.just(
                User("a", listOf(1L, 2L)),
                User("b", listOf(2L, 3L))
        )
    }


    @GetMapping(produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE), value = "/userflux")
    fun generatorUsersFromStream(): Flux<User> {
        val users =
                Flux.fromStream(
                        LongStream
                                .iterate(0) { it + 1 }
                                .mapToObj { User(it.toString(), listOf(it, it + 1)) })

        val interval = Flux.interval(Duration.ofSeconds(1))

        return Flux
                .zip(users, interval)
                .map{ it.t1 }
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE), value = "/usersOnline/{modelId}")
    fun generatorUsersFromStream(@PathVariable modelId: String): Flux<User> {
        return userRepository.findAll().filter{it.modelIds.contains(modelId.toLong())}
    }

    @GetMapping("/inputDemoUsers")
    fun inputDemoUsers(): Flux<User>{
        print("adding users")

        val users = Flux.just(
                User("Jack", Arrays.asList(1L, 2L)),
                User("Chloe", Arrays.asList(1L, 2L)),
                User("Kim", Arrays.asList(0L, 2L)),
                User("David", Arrays.asList(1L, 3L)),
                User("Michelle", Arrays.asList(1L, 4L)))

        return userRepository.insert(users)
    }


}
