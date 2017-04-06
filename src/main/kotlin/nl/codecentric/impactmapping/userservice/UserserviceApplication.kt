package nl.codecentric.impactmapping.userservice

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@SpringBootApplication
class UserserviceApplication

fun main(args: Array<String>) {
    SpringApplication.run(UserserviceApplication::class.java, *args)
}

@Bean fun mongoClient(): MongoClient {
    return MongoClients.create("mongodb://localhost:27017")
}

@Bean fun reactiveMongoTemplate(): ReactiveMongoTemplate {
    return ReactiveMongoTemplate(mongoClient(), "impactmapping")
}