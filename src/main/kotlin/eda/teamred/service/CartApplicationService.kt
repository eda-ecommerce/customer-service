package eda.teamred.service

import eda.teamred.service.model.Cart
import eda.teamred.service.repository.CartRepository
import java.util.UUID

class CartApplicationService (private val cartRepository: CartRepository) {

    fun createCartForACustomer (customerID: UUID) {
        // TODO: check if the customer with the $customerID already has a cart assigned to them.
        // TODO: if not, create a new cart
    }

    fun addOfferingToCart (customerID: UUID, offeringID: UUID, offeringPrice: Float, offeringQuantity: Int = 0) {

    }

}