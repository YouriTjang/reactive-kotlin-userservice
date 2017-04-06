package nl.codecentric.impactmapping.userservice

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.LongStream

@RestController
class UserController(userRepository: UserRepository){

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

}