package tr.com.gndg.self.domain.repo

import tr.com.gndg.self.domain.model.Customer

interface CustomerRepository {
    suspend fun getCustomers() : List<Customer>
    suspend fun getCustomerByUUID(customerUUID: String) : Customer?
    suspend fun insertCustomer(customer: Customer) : Result<Boolean>
    suspend fun deleteCustomer(customer: Customer) : Result<Boolean>
    suspend fun updateCustomer(customer: Customer) : Result<Boolean>
}