package nl.codecentric.impactmapping.dummyclient

import nl.codecentric.impactmapping.userservice.User
import org.springframework.http.MediaType
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono



@RestController
class UserController(val userRepository: DummyDataRepository) {

    @GetMapping(path = arrayOf("/users"), produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    @ResponseBody
    fun fetchQuotesStream(): Flux<User> {
        return WebClient.create("http://localhost:8082")
                .get()
                .uri("/generate")
                .accept(TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(User::class)
                .map{ cr ->
                    println("inserting: " + cr)
                    userRepository.insert(Mono.just(cr)).blockFirst()
                }
    }

}