package nl.codecentric.impactmapping.dummyclient

import nl.codecentric.impactmapping.userservice.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface DummyDataRepository: ReactiveMongoRepository<User, String> {
}
