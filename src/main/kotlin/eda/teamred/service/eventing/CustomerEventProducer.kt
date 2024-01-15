package eda.teamred.service.eventing

import eda.teamred.service.model.DTO
import eda.teamred.service.toByteArray
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CustomerEventProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {
    fun emitEvent(operation: Operation, dto: DTO, topic: String = "\${spring.kafka.default-topic}") {
        val event = GeneralEvent(operation, dto)
        kafkaTemplate.send(event.toKafkaRecord(topic))
    }

}
