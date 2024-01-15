package eda.teamred.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eda.teamred.service.eventing.CustomerEventProducer
import eda.teamred.service.eventing.Operation
import eda.teamred.service.model.Customer
import eda.teamred.service.model.CustomerDTO
import eda.teamred.service.model.CustomerMapper
import eda.teamred.service.repository.CustomerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerApplicationService(private val customerRepository: CustomerRepository, private val producer: CustomerEventProducer) {
    val customerMapper = CustomerMapper()
    fun createCustomer(customerDTO: CustomerDTO): CustomerDTO {
        //check for exact duplicate?
        //we disregard any ids
        val newCustomer = customerMapper.toEntity(customerDTO)
        customerRepository.save(newCustomer)
        val newDTO = customerMapper.toDto(newCustomer)
        producer.emitEvent(Operation.CREATED, newDTO)
        return newDTO
    }

    fun fetchCustomers() : List<CustomerDTO>{
        val customers = customerRepository.findAll().toList()
        return customers.map { customer : Customer -> customerMapper.toDto(customer) }
    }

    fun fetchCustomerById(id : UUID) : CustomerDTO?{
        val customer = customerRepository.findByIdOrNull(id)
        //return dto or null
        return customerMapper.toDto(customer?:return null)
    }

    fun updateCustomer(customerDTO: CustomerDTO, id : UUID): CustomerDTO?{
        val found = customerRepository.findById(id)
        if (found.isPresent){
            val customer = found.get()
            val updatedCustomer = customerMapper.toEntity(customer.id,customerDTO)
            customerRepository.save(updatedCustomer)
            //TODO refactor to event schema
            val newDTO = customerMapper.toDto(updatedCustomer)
            producer.emitEvent(Operation.UPDATED,newDTO)
            return newDTO
        }
        return null
    }
    fun deleteCustomer(id: UUID): Boolean{
        val found = customerRepository.findById(id)
        if (found.isPresent) {
            customerRepository.deleteById(id)
            val deletedDTO = customerMapper.toDto(found.get())
            producer.emitEvent(Operation.DELETED,deletedDTO)
            return true
        }
        return false
    }
}
