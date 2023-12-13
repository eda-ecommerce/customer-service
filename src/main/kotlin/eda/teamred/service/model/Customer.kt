package eda.teamred.service.model

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class Customer(
    var firstName: String,
    var lastName: String,
    @Embedded
    var address: Address,
    @Id
    val id: UUID = UUID.randomUUID()
){
}
