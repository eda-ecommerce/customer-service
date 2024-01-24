package eda.teamred.service

import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import eda.teamred.service.eventing.GeneralConsumer
import eda.teamred.service.model.*
import eda.teamred.service.repository.CustomerRepository
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class KafkaIntegrationTests {

    @Autowired
    lateinit var customerApplicationService: CustomerApplicationService

    @MockkBean
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var generalConsumer: GeneralConsumer

    @BeforeEach
    fun reset(){
        generalConsumer.resetLatch()
    }

    @Test
    fun creatingCustomerEmitsEvent(){
        every { customerRepository.findById(any()) } returns Optional.of(testCustomer)
        every{ customerRepository.save(any())} returns testCustomer
        customerApplicationService.createCustomer(testCustomerDTO)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val payloadValue =  generalConsumer.savedConsumerRecord.value().toString()
        val dto = Gson().fromJson(payloadValue, CustomerDTO::class.java)
        equalsTestDto(dto)
    }

    @Test
    fun updateCustomerUpdatesReturnsUpdatedDTOEmitsEvent(){
        every { customerRepository.findById(any()) } returns Optional.of(testCustomer)
        every{ customerRepository.save(any())} returns testCustomer

        val newCustomer = customerApplicationService.updateCustomer(updatedTestCustomerDTO, testCustomer.id)
        assert(newCustomer != null)
        assertDtoEqualsDto(newCustomer!!, updatedTestCustomerDTO)

        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val payloadValue =  generalConsumer.savedConsumerRecord.value().toString()
        val dto = Gson().fromJson(payloadValue, CustomerDTO::class.java)
        assertDtoEqualsDto(dto, updatedTestCustomerDTO)
    }

    @Test
    fun deleteCustomerReturnsTrueIfDeletedEmitsEvent(){
        every { customerRepository.findById(any()) } returns Optional.of(testCustomer)
        every{ customerRepository.save(any())} returns testCustomer
        every{customerRepository.deleteById(any())} returns Unit

        val success = customerApplicationService.deleteCustomer(UUID.randomUUID())
        assert(success)

        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val payloadValue =  generalConsumer.savedConsumerRecord.value().toString()
        val dto = Gson().fromJson(payloadValue, CustomerDTO::class.java)
        equalsTestDto(dto)
    }

}
