package tr.com.gndg.self.data

import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.domain.repo.CustomerRepository

class CustomerRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : CustomerRepository {

    override suspend fun getCustomers(): List<Customer> = dataSourceRepository.getCustomers()

    override suspend fun getCustomerByUUID(customerUUID: String): Customer? = dataSourceRepository.getCustomerByUUID(customerUUID)

    override suspend fun insertCustomer(customer: Customer): Result<Boolean> {
        return dataSourceRepository.insertCustomer(customer)
    }

    override suspend fun deleteCustomer(customer: Customer): Result<Boolean> {
        return dataSourceRepository.deleteCustomer(customer)
    }

    override suspend fun updateCustomer(customer: Customer): Result<Boolean> {
        return dataSourceRepository.updateCustomer(customer)
    }

}