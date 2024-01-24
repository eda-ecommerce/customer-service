package eda.teamred.service

import com.ninjasquad.springmockk.MockkBean
import eda.teamred.service.eventing.CustomerEventProducer
import eda.teamred.service.eventing.Operation
import eda.teamred.service.model.CustomerDTO
import eda.teamred.service.repository.CustomerRepository
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("unit-test")
class CustomerApplicationServiceUnitTest {

    @MockkBean
    lateinit var customerRepository: CustomerRepository

    @MockkBean
    lateinit var customerEventProducer: CustomerEventProducer

    @Autowired
    lateinit var customerApplicationService: CustomerApplicationService

    @BeforeEach
    fun setup(){
        every { customerRepository.save(any()) } returns testCustomer
        every { customerRepository.findById(any()) } returns Optional.of(testCustomer)
        every { customerRepository.findAll() } returns listOf(testCustomer, testCustomer, testCustomer)
        every { customerEventProducer.emitEvent(any(),any(),any()) } returns Unit
        every { customerRepository.save(any()) } returns testCustomer
        every { customerRepository.deleteById(any()) } returns Unit
        every { customerEventProducer.defaultTopic } returns "embedded-test-topic"
    }
    @Test
    fun fetchCustomersReturnsListOfAllCustomersAsDTO(){
        val customers : List<CustomerDTO> = customerApplicationService.fetchCustomers()
        assert(customers.isNotEmpty())
        customers.forEach { x -> equalsTestDto(x) }
    }
    @Test
    fun fetchCustomerByIdReturnsDto(){
        val result = customerApplicationService.fetchCustomerById(UUID.randomUUID())
        assert(result != null)
        equalsTestDto(result!!)
    }
    @Test
    fun givenRepository_whenCreatingCustomer_returnsDTO(){
        val result = customerApplicationService.createCustomer(testCustomerDTO)
        equalsTestDto(result)
        verify { customerEventProducer.emitEvent(operation = Operation.CREATED, result,any()) } //Verify that the event producer was called
    }
    @Test
    fun givenRepository_whenUpdatingCustomer_returnsDTO(){
        val result = customerApplicationService.updateCustomer(testCustomerDTO, testCustomer.id)
        assert(result != null)
        equalsTestDto(result!!)
        verify { customerEventProducer.emitEvent(operation = Operation.UPDATED, result,any()) } //Verify that the event producer was called
    }
    @Test
    fun givenRepository_whenDeletingCustomer_returnsTrue(){
        val result = customerApplicationService.deleteCustomer(testCustomer.id)
        assert(result)
        verify { customerEventProducer.emitEvent(operation = Operation.DELETED, testCustomerDTOWithId,any()) } //Verify that the event producer was called
    }

}