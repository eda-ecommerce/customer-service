package eda.teamred.service

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class StringProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private final val logger = LoggerFactory.getLogger(this.javaClass)
    fun sendStringMessage(topic: String, message: String){
        logger.info("sending payload: '$message' to topic '$topic'")
        kafkaTemplate.send(topic, message)
    }
}
