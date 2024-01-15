package eda.teamred.service.model

import com.fasterxml.jackson.annotation.JsonValue
import com.google.gson.Gson
import java.util.*


data class CustomerDTO (
    val id: UUID? = null ,
    val firstName : String? = null,
    val lastName : String? = null,
    val address : Address? = null,
    val email : String? = null,
    val phoneNumber : String? = null
): DTO{
    override fun toString(): String{
        return Gson().toJson(this)
    }
}

