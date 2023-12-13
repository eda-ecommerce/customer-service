package eda.teamred.service

import com.google.gson.Gson
import eda.teamred.service.eventing.CustomerEventProducer
import eda.teamred.service.eventing.EventType
import eda.teamred.service.model.Customer
import eda.teamred.service.eventing.GeneralConsumer
import eda.teamred.service.eventing.GeneralEvent
import eda.teamred.service.model.Address
import eda.teamred.service.model.CustomerDTO
import eda.teamred.service.model.CustomerMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class KafkaTests {
    @Autowired
    lateinit var stringProducer: StringProducer

    @Autowired
    lateinit var customerEventProducer: CustomerEventProducer


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

    @Test
    fun givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived(){
        val data = "Sending with our own simple KafkaProducer"
        stringProducer.sendStringMessage(topic, data)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        assert(generalConsumer.stringData == data)
    }
    @Test
    fun givenEmbeddedKafkaBroker_whenEmittingCustomerCreated_thenCorrectEventEmitted(){
        val address = Address("Streetest","69","42069")
        val data = Customer("Thor","Heavy Hammer", address)
        val customerDTO = customerMapper.toDto(data)
        customerEventProducer.emitEvent(EventType.CREATED,customerDTO, topic)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val jsonString =  generalConsumer.payload.value().toString()
        val consumedEvent = Gson().fromJson(jsonString,GeneralEvent::class.java)
        val dto = Gson().fromJson(consumedEvent.payload, CustomerDTO::class.java)
        assert(dto.equals(customerDTO))
    }
}
