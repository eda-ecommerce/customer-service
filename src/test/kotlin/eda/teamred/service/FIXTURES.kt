package eda.teamred.service

import eda.teamred.service.model.Address
import eda.teamred.service.model.Customer
import eda.teamred.service.model.CustomerDTO

val testCustomer = Customer(
    firstName = "Odin",
    lastName = "Hammerfall",
    address = Address(
        street = "Valhalla",
        "1",
        "50667 Heaven"
    )
)

val updatedTestCustomer = Customer(
    firstName = "TheCoolerOdin",
    lastName = "Hammerfallv2",
    address = Address(
        street = "Valhalla",
        "69",
        "50667 Heaven"
    )
)

val testCustomer2 = Customer(
    firstName = "Thor",
    lastName = "Hammerrise",
    address = Address(
        street = "Valhalla",
        "2",
        "50667 Heaven"
    )
)

val testCustomerDTO = CustomerDTO(
    firstName = testCustomer.firstName,
    lastName = testCustomer.lastName,
    address = testCustomer.address
)

val updatedTestCustomerDTO = CustomerDTO(
    firstName = updatedTestCustomer.firstName,
    lastName = updatedTestCustomer.lastName,
    address = updatedTestCustomer.address
)
val updatedTestCustomerDTOWithId = CustomerDTO(
    firstName = updatedTestCustomer.firstName,
    lastName = updatedTestCustomer.lastName,
    address = updatedTestCustomer.address,
    id = testCustomer.id
)

val testCustomerDTOWithId = CustomerDTO(
    firstName = testCustomer.firstName,
    lastName = testCustomer.lastName,
    address = testCustomer.address,
    id = testCustomer.id
)

fun equalsTestDto(pDTO: CustomerDTO){
    assert(pDTO.firstName == testCustomerDTO.firstName)
    assert(pDTO.lastName == testCustomerDTO.lastName)
    assert(pDTO.address == testCustomerDTO.address)
    assert(pDTO.email == testCustomerDTO.email)
    assert(pDTO.phoneNumber == testCustomerDTO.phoneNumber)
}

fun assertDtoEqualsDto(pDTO: CustomerDTO, toBeChecked: CustomerDTO){
    assert(pDTO.firstName == toBeChecked.firstName)
    assert(pDTO.lastName == toBeChecked.lastName)
    assert(pDTO.address == toBeChecked.address)
    assert(pDTO.email == toBeChecked.email)
    assert(pDTO.phoneNumber == toBeChecked.phoneNumber)
}