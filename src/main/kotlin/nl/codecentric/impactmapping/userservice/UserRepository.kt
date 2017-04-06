package nl.codecentric.impactmapping.userservice

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository: ReactiveMongoRepository<User, String> {
}
