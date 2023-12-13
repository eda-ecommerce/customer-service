package eda.teamred.service.model

import com.google.gson.Gson
import java.util.*


data class CustomerDTO (
    val id: UUID? = null,
    val firstName : String?,
    val lastName : String?,
    val address : Address?
): DTO{
    override fun toString(): String{
        return Gson().toJson(this)
    }
}

