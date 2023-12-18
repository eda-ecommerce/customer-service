package eda.teamred.service

import eda.teamred.service.dto.CustomerDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class Controller (private val customerApplicationService: CustomerApplicationService) {

    @PostMapping("/customer")
    fun create(@RequestBody customerDTO : CustomerDTO) : ResponseEntity<UUID>{
        return ResponseEntity(customerApplicationService.createCustomer(customerDTO), HttpStatus.CREATED)
    }

    @GetMapping("/customer")
    fun fetch() : ResponseEntity<List<CustomerDTO>>{
        return ResponseEntity(customerApplicationService.fetchCustomers(), HttpStatus.OK)
    }

    @GetMapping("/customer/{id}")
    fun fetchById(@PathVariable("id") id : UUID) : ResponseEntity<CustomerDTO>{
        val customerDto = customerApplicationService.fetchCustomerById(id)
        return if (customerDto == null) ResponseEntity(null, HttpStatus.NOT_FOUND) else ResponseEntity(customerDto, HttpStatus.OK)
    }

    @PutMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@RequestBody customerDTO: CustomerDTO, @PathVariable("id") id : UUID){
        customerApplicationService.updateCustomer(customerDTO, id)
    }

    @PutMapping("/customer/address/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateAddress(@RequestBody address : String, @PathVariable("id") id : UUID){
        customerApplicationService.updateCustomerAddress(address, id)
    }

    @PutMapping("/customer/lastName/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateLastName(@RequestBody lastName : String, @PathVariable("id") id : UUID){
        customerApplicationService.updateCustomerLastName(lastName, id)
    }

    @PutMapping("/customer/email/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateEmail(@RequestBody email : String, @PathVariable("id") id : UUID){
        customerApplicationService.updateCustomerEmail(email, id)
    }

    @PutMapping("/customer/phoneNumber/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePhoneNumber(@RequestBody phoneNumber : String, @PathVariable("id") id : UUID){
        customerApplicationService.updateCustomerPhoneNumber(phoneNumber, id)
    }

    @DeleteMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable("id") id : UUID){
        customerApplicationService.deleteCustomer(id)
    }

}
