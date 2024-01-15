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
    var email : String,
    var phoneNumber : String,
    @Id
    val id: UUID

){
    constructor(firstName: String,lastName: String,address: Address) : this(firstName,lastName,address,"","",UUID.randomUUID())
    constructor(firstName: String,lastName: String,address: Address,email: String,phoneNumber: String) : this(firstName,lastName,address,email,phoneNumber,UUID.randomUUID())
    constructor(firstName: String,lastName: String,address: Address,id: UUID) : this(firstName,lastName,address,"","",id)

}