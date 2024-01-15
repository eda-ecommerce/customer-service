package eda.teamred.service.eventing

import com.google.gson.Gson
import com.google.gson.JsonElement
import eda.teamred.service.model.DTO
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeaders
import java.time.LocalDateTime

open class GeneralEvent (val type: Operation, val payload: DTO) {
    val source: String = "customer-service"
    val timestamp = System.currentTimeMillis() / 1000

    fun toKafkaRecord(topic: String): ProducerRecord<String, String> {
        val gson = Gson()
        val payloadJson = gson.toJson(payload)
        val record = ProducerRecord<String, String>(topic, payloadJson).apply {
            headers().add("operation", type.toString().toByteArray())
            headers().add("source", source.toByteArray())
            headers().add("timestamp", timestamp.toString().toByteArray())
        }
        return record
    }
}
