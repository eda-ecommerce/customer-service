package eda.teamred.service.model

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerMapper {
    fun toDto(customer: Customer): CustomerDTO{
        return CustomerDTO(
            firstName = customer.firstName,
            lastName = customer.lastName,
            address = customer.address,
            id = customer.id
        )
    }
    fun toEntity(customerDTO: CustomerDTO):Customer{
        return Customer(
            firstName = customerDTO.firstName ?: throw ValidationException("First name cannot be null"),
            lastName = customerDTO.lastName ?: throw ValidationException("Last name cannot be null"),
            address = customerDTO.address ?: throw ValidationException("Address cannot be null")
        )
    }
    fun toEntity(id: UUID,customerDTO: CustomerDTO):Customer{
        return Customer(
            firstName = customerDTO.firstName ?: throw ValidationException("First name cannot be null"),
            lastName = customerDTO.lastName ?: throw ValidationException("Last name cannot be null"),
            address = customerDTO.address ?: throw ValidationException("Address cannot be null"),
            id = id
        )
    }
}
