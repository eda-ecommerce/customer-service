package eda.teamred.service

import eda.teamred.service.model.CustomerDTO
import eda.teamred.service.model.Customer
import eda.teamred.service.model.CustomerMapper
import eda.teamred.service.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerApplicationService(private val customerRepository: CustomerRepository, private val producer: StringProducer) {
    @Autowired
    lateinit var customerMapper: CustomerMapper

    fun createCustomer(customerDTO: CustomerDTO): CustomerDTO {
        //check for exact duplicate?
        //we disregard any ids
        val newCustomer = customerMapper.toEntity(customerDTO)
        customerRepository.save(newCustomer)
        //TODO refactor to event schema
        producer.sendStringMessage("customer","Created new Customer with id: ${newCustomer.id}")
        return customerMapper.toDto(newCustomer)
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
            producer.sendStringMessage("customer","Updated Customer $updatedCustomer")
            //producer.emitEvent(EventType.UPDATED,updatedCustomer)
            return customerMapper.toDto(updatedCustomer)
        }
        return null
    }

    fun updateCustomerAddress(address: String, id : UUID){
        val customer = customerRepository.findById(id).get()
        customer.address = address
        customerRepository.save(customer)
        producer.sendStringMessage("Updated Customer with id: $id")
    }

    fun updateCustomerLastName(lastName : String, id : UUID){
        val customer = customerRepository.findById(id).get()
        customer.lastName = lastName
        customerRepository.save(customer)
        producer.sendStringMessage("Updated Customer with id: $id")
    }

    fun updateCustomerEmail(email : String, id : UUID){
        val customer = customerRepository.findById(id).get()
        customer.email = email
        customerRepository.save(customer)
        producer.sendStringMessage("Updated Customer with id: $id")
    }

    fun updateCustomerPhoneNumber(phoneNumber : String, id : UUID){
        val customer = customerRepository.findById(id).get()
        customer.phoneNumber = phoneNumber
        customerRepository.save(customer)
        producer.sendStringMessage("Updated Customer with id: $id")
    }

    fun deleteCustomer(id: UUID): Boolean{
        val found = customerRepository.findById(id)
        if (found.isPresent) {
            customerRepository.deleteById(id)
            //TODO refactor to event schema
            producer.sendStringMessage("customer", "Deleted Customer with id: $id")
            return true
        }
        return false
    }
}
