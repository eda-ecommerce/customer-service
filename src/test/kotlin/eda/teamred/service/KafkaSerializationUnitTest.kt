package eda.teamred.service

import com.google.gson.Gson
import eda.teamred.service.eventing.CustomerEventProducer
import eda.teamred.service.eventing.GeneralConsumer
import eda.teamred.service.eventing.Operation
import eda.teamred.service.model.CustomerDTO
import org.apache.kafka.common.header.Header
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@ActiveProfiles("unit-test")
class KafkaSerializationUnitTest {

    @Autowired
    lateinit var customerEventProducer: CustomerEventProducer

    @Autowired
    lateinit var generalConsumer: GeneralConsumer

    @Value("\${spring.kafka.default-topic}")
    lateinit var topic: String

    @Test
    fun dtoSerializationIsCorrectlyDeserializable(){
        customerEventProducer.emitEvent(Operation.CREATED, testCustomerDTO, topic)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val payload =  generalConsumer.savedConsumerRecord.value().toString()
        val dto = Gson().fromJson(payload, CustomerDTO::class.java)
        assert(dto.equals(testCustomerDTO))
    }

    @Test
    fun testHeaders(){
        customerEventProducer.emitEvent(Operation.CREATED, testCustomerDTO, topic)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        val headers = generalConsumer.savedConsumerRecord.headers()
        var operation : Operation? = null
        var source : String? = null
        var timestamp: String? = null
        headers.forEach { header: Header ->
            if (header.key().toString() == "operation"){
                operation = Operation.valueOf(String(header.value()))
            }
            if (header.key().toString() == "source"){
                source = String(header.value())
            }
            if (header.key().toString() == "timestamp"){
                timestamp = String(header.value())
            }
        }
        assert(operation == Operation.CREATED)
        assert(source == "customer")
        assert(timestamp != null) //TODO check if timestamp is actually correct
    }
}