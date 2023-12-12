package eda.teamred.service

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class GeneralConsumer {
    private val logger= LoggerFactory.getLogger(this.javaClass)

    var countDownLatch = CountDownLatch(1)
    var payload = ""
    var stringData = ""
    @KafkaListener(topics = ["\${test.topic}"])
    fun firstListener(consumerRecord: ConsumerRecord<Any, Any>){
        logger.info("Message received: [${consumerRecord}]")
        payload = consumerRecord.toString()
        stringData = consumerRecord.value().toString()
        countDownLatch.countDown()
    }

    fun resetLatch(){
        countDownLatch = CountDownLatch(1)
    }
}
