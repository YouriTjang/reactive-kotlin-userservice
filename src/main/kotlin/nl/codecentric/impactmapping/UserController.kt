package nl.codecentric.impactmapping

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
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

    @GetMapping("/events")
    fun userEvents(): Flux<User> {
        val users = Flux.generate<Int, Int>({ 1 }) { i, sink ->
            sink.next(i)
            println(i)
            i!! + 1
        }.map { i -> User(i!!.toString(), Arrays.asList(Integer.toUnsignedLong(i))) }

        val interval = Flux.interval(Duration.ofSeconds(1))

        return Flux
                .zip(users, interval)
                .map{ it.t1 }
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE), value = "/events2")
    fun generatorUsersFromStream(): Flux<User> {
        val userStream = LongStream.iterate(0) { it + 1 }
                .mapToObj { User(it.toString(), listOf(it, it + 1)) }
        val users = Flux.fromStream(userStream)

        val interval = Flux.interval(Duration.ofSeconds(1))

        return Flux.zip(users, interval)
                .map{ it.t1 }
    }

}