package eda.teamred.service.eventing

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.Headers
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class GeneralConsumer {
    private val logger= LoggerFactory.getLogger(this.javaClass)

    var countDownLatch = CountDownLatch(1)
    lateinit var savedConsumerRecord: ConsumerRecord<Any, Any>
    lateinit var headers: Headers
    var stringData = ""

    @KafkaListener(topics = ["\${spring.kafka.default-topic}"])
    fun customerListener(consumerRecord: ConsumerRecord<Any, Any>){
        logger.info("Message received: [${consumerRecord}]")
        savedConsumerRecord = consumerRecord
        stringData = consumerRecord.toString()
        headers = consumerRecord.headers()
        headers.forEach {
            header: Header ->
            logger.info("Header: ${header.key()} : ${String(header.value())}")

        }
        countDownLatch.countDown()
    }

    fun resetLatch(){
        countDownLatch = CountDownLatch(1)
    }
}
