package eda.teamred.service.eventing

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonView


enum class Operation(@JsonValue val value: String) {
    CREATED("created"),
    UPDATED("updated"),
    DELETED("deleted");
    override fun toString(): String {
        return name.lowercase()
    }
}
