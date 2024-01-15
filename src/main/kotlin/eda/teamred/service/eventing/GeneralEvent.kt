package eda.teamred.service.eventing

import com.google.gson.Gson
import eda.teamred.service.model.DTO
import org.apache.kafka.clients.producer.ProducerRecord

open class GeneralEvent (val operation: Operation, val payload: DTO) {
    val source: String = "customer-service"
    val timestamp = System.currentTimeMillis() / 1000

    fun toKafkaRecord(topic: String): ProducerRecord<String, String> {
        val gson = Gson()
        val payloadJson = gson.toJson(payload)
        val record = ProducerRecord<String, String>(topic, payloadJson).apply {
            headers().add("operation", operation.toString().toByteArray())
            headers().add("source", source.toByteArray())
            headers().add("timestamp", timestamp.toString().toByteArray())
        }
        return record
    }
}
