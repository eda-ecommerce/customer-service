package eda.teamred.service

import com.google.gson.Gson
import eda.teamred.service.eventing.CustomerEventProducer
import eda.teamred.service.eventing.Operation
import eda.teamred.service.eventing.GeneralConsumer
import eda.teamred.service.eventing.GeneralEvent
import eda.teamred.service.model.*
import eda.teamred.service.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class KafkaTests {
    @Autowired
    lateinit var customerEventProducer: CustomerEventProducer

    @Autowired
    lateinit var customerApplicationService: CustomerApplicationService

    @MockK
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var generalConsumer: GeneralConsumer

    @Autowired
    lateinit var customerMapper: CustomerMapper

    @Value("\${spring.kafka.default-topic}")
    lateinit var topic: String

    @BeforeEach
    fun reset(){
        generalConsumer.resetLatch()
    }

    final val testCustomer = Customer(
        firstName = "Odin",
        lastName = "Hammerfall",
        address = Address(
            street = "Valhalla",
            "1",
            "50667 Heaven"
        )
    )

    final val testCustomerDTO = CustomerDTO(
        firstName = testCustomer.firstName,
        lastName = testCustomer.lastName,
        address = testCustomer.address
    )

    fun equalsTestDtoValues(pDTO: CustomerDTO){
        assert(pDTO.firstName == testCustomerDTO.firstName)
        assert(pDTO.lastName == testCustomerDTO.lastName)
        assert(pDTO.address == testCustomerDTO.address)
    }

    @Test
    fun givenEmbeddedKafkaBroker_whenEmittingCustomerCreated_thenCorrectEventEmitted(){
        val address = Address("Streetest","69","42069")
        val data = Customer("Thor","Heavy Hammer", address)
        val customerDTO = customerMapper.toDto(data)
        customerEventProducer.emitEvent(Operation.CREATED,customerDTO, topic)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val jsonString = generalConsumer.payload.value().toString()
        val dto = Gson().fromJson<DTO>(jsonString, CustomerDTO::class.java)
        assert(dto.equals(customerDTO))
    }
    @Test
    fun creatingCustomerEmitsEvent(){
        every { customerRepository.findById(any()) } returns Optional.of(testCustomer)
        customerApplicationService.createCustomer(testCustomerDTO)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val payloadValue =  generalConsumer.payload.value().toString()
        val dto = Gson().fromJson(payloadValue, CustomerDTO::class.java)
        equalsTestDtoValues(dto)
    }
}
