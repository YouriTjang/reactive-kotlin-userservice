package nl.codecentric.impactmapping.dummyDataGenerator

import nl.codecentric.impactmapping.userservice.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.Disposable
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.stream.IntStream


@SpringBootApplication
open class DummyDataGenerator {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(DummyDataGenerator::class.java)
                    .properties(mapOf("server.port" to "8082"))
                    .run(*args)
        }
    }

    @Bean
    fun client(): WebClient {
        return WebClient.create("http://localhost:8080")
    }

    @Bean
    fun demo(client: WebClient): Disposable {
        return  client
                .get()
                .uri("/userflux")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .map{it.bodyToFlux(User::class)}
                .subscribe(::println)
    }
}

@RestController
class UserController(){

    @GetMapping(produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE), value = "/generate")
    fun generatorUsersFromStream(): Flux<User> {
        val users = listOf(
                User("Jack", Arrays.asList(1L, 2L)),
                User("Chloe", Arrays.asList(1L, 2L)),
                User("Kim", Arrays.asList(0L, 2L)),
                User("David", Arrays.asList(1L, 3L)),
                User("Michelle", Arrays.asList(1L, 4L))
        )

        val rand = Random()
        val randomUsers =
                Flux.fromStream(
                        IntStream
                                .iterate(0) { rand.nextInt(users.size) }
                                .mapToObj { users.get(it) })

        val interval = Flux.interval(Duration.ofSeconds(1))

        return Flux
                .zip(randomUsers, interval)
                .map{ it.t1 }
    }
}