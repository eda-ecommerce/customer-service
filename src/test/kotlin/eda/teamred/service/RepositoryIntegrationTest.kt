package eda.teamred.service

import eda.teamred.service.model.Address
import eda.teamred.service.model.Customer
import eda.teamred.service.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

//Needs a MYSQL Container Running as configured in application.properties
@DataJpaTest
//Annotate to find config of application. TODO: Create separate test configs (maybe testcontainers?)
@ExtendWith(SpringExtension::class)
//Disable the replacement of mysql datasource with h2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryIntegrationTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun WhenFindById_thenReturnCustomer() {
        val address = Address("Streetest","69","42069")
        val customer = Customer("Thor","Heavy Hammer", address)
        entityManager.persist(customer)
        entityManager.flush()
        val customerFound = customerRepository.findByIdOrNull(customer.id)
        assert(customerFound == customer)
    }

    @Test
    fun WhenDeleteCustomer_thenReturnNull(){
        val address = Address("Streetest","69","42069")
        val customer = Customer("Thor","Heavy Hammer", address)
        entityManager.persist(customer)
        entityManager.flush()
        customerRepository.deleteById(customer.id)
        val deletedCustomer = customerRepository.findByIdOrNull(customer.id)
        assert(deletedCustomer == null)
    }

}
