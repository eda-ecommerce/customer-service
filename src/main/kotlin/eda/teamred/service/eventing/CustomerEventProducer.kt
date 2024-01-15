package eda.teamred.service.eventing

import eda.teamred.service.model.DTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CustomerEventProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {
    @Value("\${spring.kafka.default-topic}")
    lateinit var defaultTopic: String
    fun emitEvent(operation: Operation, dto: DTO, topic: String = defaultTopic) {
        val event = GeneralEvent(operation, dto)
        kafkaTemplate.send(event.toKafkaRecord(topic))
    }

}
