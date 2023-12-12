package eda.teamred.service

import eda.teamred.service.dto.CustomerDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller (private val customerApplicationService: CustomerApplicationService) {
    data class StringMessage(val message: String)

    @PostMapping("/customer")
    fun create(@RequestBody customerDTO : CustomerDTO){
        customerApplicationService.createCustomer(customerDTO)
    }
}
