package eda.teamred.service

import eda.teamred.service.model.CustomerDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class CustomerController (private val customerApplicationService: CustomerApplicationService) {

    @PostMapping("/customer")
    fun create(@RequestBody customerDTO : CustomerDTO) : ResponseEntity<CustomerDTO>{
        return ResponseEntity(customerApplicationService.createCustomer(customerDTO), HttpStatus.CREATED)
    }

    @GetMapping("/customer")
    fun fetch() : ResponseEntity<List<CustomerDTO>>{
        return ResponseEntity(customerApplicationService.fetchCustomers(), HttpStatus.OK)
    }

    @GetMapping("/customer/{id}")
    fun fetchById(@PathVariable("id") id : UUID) : ResponseEntity<CustomerDTO>{
        val customerDto = customerApplicationService.fetchCustomerById(id)
        if (customerDto != null)
            return ResponseEntity(customerDto, HttpStatus.OK)
        return ResponseEntity(null, HttpStatus.NOT_FOUND)
    }

    @PostMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@RequestBody customerDTO: CustomerDTO, @PathVariable("id") id : UUID): ResponseEntity<CustomerDTO>{
        val updated = customerApplicationService.updateCustomer(customerDTO, id)
        if (updated!=null){
            return ResponseEntity
                .ok()
                .body(updated)
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable("id") id : UUID){
        customerApplicationService.deleteCustomer(id)
    }

}
