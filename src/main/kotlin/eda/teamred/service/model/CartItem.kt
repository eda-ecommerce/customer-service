package eda.teamred.service.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class CartItem (
    var offeringID: UUID,
    var quantity: Int = 0,
    var price: Float,
    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun getCartItemSubtotal(): Float {
        return quantity*price
    }
    fun getCartItemAsAPair(offeringID: UUID, quantity: Int): Pair<UUID, Int> {
        return Pair(offeringID, quantity)
    }
}