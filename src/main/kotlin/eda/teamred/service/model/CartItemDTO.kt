package eda.teamred.service.model

import java.util.*

data class CartItemDTO (
    val id: UUID? = null,
    val offeringID: UUID,
    val quantity: Int = 0,
    val price: Float? = null
) {}