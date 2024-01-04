package eda.teamred.service.repository

import eda.teamred.service.model.Cart
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CartRepository: CrudRepository<Cart, UUID>{
}