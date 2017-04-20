package nl.codecentric.impactmapping.dummyclient

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@SpringBootApplication
open class DummyClientApplication(){

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(DummyClientApplication::class.java)
                    .properties(mapOf("server.port" to "8081"))
                    .run(*args)
        }
    }

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create("mongodb://localhost:27017")
    }

    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(mongoClient(), "impactmapping")
    }
}
