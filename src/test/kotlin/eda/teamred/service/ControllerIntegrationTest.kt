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

//Annotate to find config of application. TODO: Create separate test configs (maybe testcontainers?)
@ExtendWith(SpringExtension::class)
//Disable the replacement of mysql datasource with h2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class ControllerIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var customerRepository: CustomerRepository

    //TODO switch to new Fixtures
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
        assertDtoEqualsDto(result.body!!, testCustomerDTO)
    }

    @Test
    fun getCustomerReturnsDTO_200(){
        customerRepository.save(customer)
        val result = testRestTemplate.getForEntity("/customer/${customer.id}", CustomerDTO::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.OK)
        assert(result.hasBody())
        assertDtoEqualsDto(result.body!!, testCustomerDTO)
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
        assertDtoEqualsDto(result.body!!, updatedCustomer)
        assert(result.body!!.id == customer.id)
    }

    @Test
    fun deleteCustomer_returnsNotFound(){
        customerRepository.save(customer)
        //Cant check status of delete??
        testRestTemplate.delete("/customer/${customer.id}")
        val result = testRestTemplate.getForEntity("/customer/${customer.id}", CustomerDTO::class.java)
        assert(result != null)
        assert(result.statusCode == HttpStatus.NOT_FOUND)
        assert(!result.hasBody())
    }

}
