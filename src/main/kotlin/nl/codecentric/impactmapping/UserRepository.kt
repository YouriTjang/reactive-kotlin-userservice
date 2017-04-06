package nl.codecentric.impactmapping

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository: ReactiveMongoRepository<User,String> {
}
