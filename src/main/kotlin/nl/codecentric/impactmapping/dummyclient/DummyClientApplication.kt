package nl.codecentric.impactmapping.dummyclient

import nl.codecentric.impactmapping.userservice.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.Disposable

@SpringBootApplication
open class DummyClientApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(DummyClientApplication::class.java)
                    .properties(mapOf("server.port" to "8081"))
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
                    .flatMap { it.bodyToFlux(User::class) }
                    .subscribe(::println)
    }
}