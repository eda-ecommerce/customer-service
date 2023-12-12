package eda.teamred.service

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
    lateinit var generalConsumer: GeneralConsumer

    @Value("\${test.topic}")
    lateinit var topic: String

    @Test
    fun givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived(){
        val data = "Sending with our own simple KafkaProducer"
        stringProducer.sendStringMessage(topic, data)
        val messageConsumed = generalConsumer.countDownLatch.await(10, TimeUnit.SECONDS)
        assert(messageConsumed)
        assert(generalConsumer.stringData == data)
    }
}
