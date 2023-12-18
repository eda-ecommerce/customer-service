package eda.teamred.service.eventing

import eda.teamred.service.eventing.EventType
import eda.teamred.service.eventing.GeneralEvent
import eda.teamred.service.model.Customer
import eda.teamred.service.model.DTO
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CustomerEventProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {
    fun emitEvent(eventType: EventType, dto: DTO, topic: String = "\${spring.kafka.default-topic}") {
        val event = GeneralEvent(eventType, dto)
        val eventString = event.toString()
        kafkaTemplate.send(topic, eventString)
    }
}
