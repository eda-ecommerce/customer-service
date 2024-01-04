package eda.teamred.service.model

import eda.teamred.service.model.CartItem
import jakarta.persistence.Id
import java.util.UUID

class Cart(customerID: UUID) {

    val customerID = customerID
    @Id
    val id:UUID = UUID.randomUUID()

    val listOfCartItems = mutableListOf<CartItem>()

    fun addItemToCart (offeringID: UUID, offeringPrice: Float) {
        if(offeringID != null){
            var newCartItem = CartItem(offeringID, 1, offeringPrice)
            listOfCartItems.add(newCartItem)
            //TODO: send a message to the producer stating that the offering has been added to the cart
        }
        else {
            //TODO: send a message stating that the offeringID has not been provided
        }
    }

    fun removeItemFromCart (offeringID: UUID, quantityToBeRemoved: Int = 1) {
        if (offeringID != null) {
            for (item in listOfCartItems) {
                if (item.offeringID == offeringID){
                    item.quantity -= quantityToBeRemoved
                    //TODO: send a message stating that the quantity has been decreased by $quantityToBeRemoved
                }
            }
            //TODO: send a message stating that the offering is not present in the cart
        }

        //TODO: send a message stating that the offeringID has not been provided

    }

    fun modifyItemQuantity (offeringID: UUID, newQuantity: Int = 0) {
        if (offeringID != null && newQuantity != null) {
            for (item in listOfCartItems) {
                if (item.offeringID == offeringID){
                    item.quantity = newQuantity
                    //TODO: send a message stating that the quantity has been modified to $newQuantity
                }
            }
            //TODO: send a message stating that the offering is not present in the cart
        }
        //TODO: send a message stating that the offeringID has not been provided
    }

    fun totalCartValue (): Float {
        var totalCartValue = 0.0f
        for (item in listOfCartItems) totalCartValue = item.getCartItemSubtotal()
        return totalCartValue
    }

    //this returns the quantity of all offerings in the cart.
    //e.g. if the cart has 5 T-shirts and 3 Shirts, this will return 8.
    fun totalQuantityOfItemsInCart (): Int {
        var totalQuantity = 0
        for (item in listOfCartItems) totalQuantity += item.quantity
        return totalQuantity
    }

    //this returns the number of different offerings in the cart.
    //e.g. if the cart has 5 T-shirts and 3 Shirts, this will return 2.
    //there might not be a need to include this now; i added this just in case we need it later
    fun numberOfItemsInCart (): Int {
        var numberOfItems = 0
        for (item in listOfCartItems) numberOfItems ++
        return numberOfItems
    }

}