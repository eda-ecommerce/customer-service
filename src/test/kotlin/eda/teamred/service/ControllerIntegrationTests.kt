package eda.teamred.service

import eda.teamred.service.model.Address
import eda.teamred.service.model.Customer
import eda.teamred.service.model.CustomerDTO
import eda.teamred.service.repository.CustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.collections.HashMap

//@DataJpaTest
//Annotate to find config of application. TODO: Create separate test configs (maybe testcontainers?)
@ExtendWith(SpringExtension::class)
//Disable the replacement of mysql datasource with h2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class ControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var customerRepository: CustomerRepository

    final val customer = Customer(
        firstName = "Odin",
        lastName = "Hammerfall",
        address = Address(
            street = "Valhalla",
            "1",
            "50667 Heaven"
        )
    )

    final val testCustomerDTO = CustomerDTO(
        firstName = customer.firstName,
        lastName = customer.lastName,
        address = customer.address
    )

    fun equalsTestDtoValues(pDTO: CustomerDTO){
        assert(pDTO.firstName == testCustomerDTO.firstName)
        assert(pDTO.lastName == testCustomerDTO.lastName)
        assert(pDTO.address == testCustomerDTO.address)
    }

    @BeforeEach
    fun setup(){
        customerRepository.deleteAll()
    }

    @Test
    fun postCustomerDTO_returnsCustomerDTO_andCREATED(){
        val result = testRestTemplate.postForEntity("/customer", testCustomerDTO, CustomerDTO::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.CREATED)
        assert(result.hasBody())
        equalsTestDtoValues(result.body!!)
    }

    @Test
    fun getCustomerReturnsDTO_200(){
        customerRepository.save(customer)
        val result = testRestTemplate.getForEntity("/customer/${customer.id}", CustomerDTO::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.OK)
        assert(result.hasBody())
        equalsTestDtoValues(result.body!!)
    }

    @Test
    fun getAllCustomers_returnsListWithDTO_200(){
        customerRepository.save(customer)
        val result = testRestTemplate.getForEntity("/customer", List::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.OK)
        assert(result.hasBody())
        val list = result.body as List<HashMap<String,String>>?
        val hashMap = list!!.first()
        val id = hashMap.get("id")
        assert(UUID.fromString(id)==customer.id)

    }

    @Test
    fun updateCustomer_returnsNewDTO_200(){
        customerRepository.save(customer)
        val updatedCustomer = CustomerDTO(null,"TheCoolerOdin","Hammerswag", Address("Los Angeles","1","NoneOfYourbusiness"))
        val result = testRestTemplate.postForEntity("/customer/${customer.id}", updatedCustomer,CustomerDTO::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.OK)
        assert(result.hasBody())
        //TODO generalize, will be out of date on addition of new attributes
        assert(result.body!!.id == customer.id) // shouldnt change
        assert(result.body!!.firstName == updatedCustomer.firstName)
        assert(result.body!!.lastName == updatedCustomer.lastName)
        assert(result.body!!.address == updatedCustomer.address)
    }

    @Test
    fun deleteCustomer_returnsNotFound(){
        customerRepository.save(customer)
        testRestTemplate.delete("/customer/${customer.id}")
        val result4 = testRestTemplate.getForEntity("/customer/${customer.id}", CustomerDTO::class.java)
        assert(result4 != null)
        assert(result4.statusCode == HttpStatus.NOT_FOUND)
        assert(!result4.hasBody())
    }

}
