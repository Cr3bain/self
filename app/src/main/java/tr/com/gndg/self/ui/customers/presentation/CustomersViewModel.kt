package tr.com.gndg.self.ui.customers.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.domain.model.CustomerDetail
import tr.com.gndg.self.domain.model.CustomerUiState
import tr.com.gndg.self.domain.model.toCustomer
import tr.com.gndg.self.domain.model.toCustomerUiState
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.repo.CustomerRepository
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.ui.customers.CustomerFormDestination

class CustomersViewModel(
    savedStateHandle: SavedStateHandle,
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private var _uuidArg :String? by mutableStateOf(null)

    init {
        _uuidArg = savedStateHandle[CustomerFormDestination.customerUUIDArgs]
    }

    val transactionState = transactionRepository.transactionState

    private var firstCustomerList= listOf<Customer>()
    private var isSearchStarting = true
    var searchText = mutableStateOf("")

    private var _customersList = MutableStateFlow<List<Customer>>(emptyList())
    val customersList  get() = _customersList.asStateFlow()

    var customerUiState by mutableStateOf(CustomerUiState())
        private set

    var somethingChanged by mutableStateOf(false)

    suspend fun getCustomers() = withContext(Dispatchers.IO){
        customerRepository.getCustomers()
    }.let {
        _customersList.value = it
    }

    suspend fun getCustomerByUUID(customerUUID: String) {
        customerRepository.getCustomerByUUID(customerUUID)?.let {
            customerUiState = it.toCustomerUiState()
        }
    }

    suspend fun deleteCustomer() : Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext customerRepository.deleteCustomer(customerUiState.customer)
    }

    suspend fun saveCustomer() : Result<Boolean> = withContext(Dispatchers.IO) {

        return@withContext if (_uuidArg != null) {
            //update
            customerRepository.updateCustomer(customerUiState.customer)
                .onSuccess {
                    somethingChanged = false
                }
                .onFailure {
                    Log.e("saveCustomer Update", it.message.toString())
                }

        } else {
            //new
            customerRepository.insertCustomer(customerUiState.customer)
                .onSuccess {
                    somethingChanged = false
                }
                .onFailure {
                    Log.e("saveCustomer save", it.message.toString())
                }
        }
    }
    fun searchCustomer(query : String){

        val listToSearch= if(isSearchStarting){
            _customersList.value
        } else{
            firstCustomerList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                _customersList.value=firstCustomerList
                isSearchStarting=true
                return@launch
            }
            val results=listToSearch.filter {
                it.name.contains(query.trim(),true) ||
                        it.description?.contains(query.trim(),true) == true
            }
            if(isSearchStarting){
                firstCustomerList=_customersList.value
                isSearchStarting=false

            }
            _customersList.value= results
        }

    }

    fun setCustomerUiState(customerDetail: CustomerDetail) {
        somethingChanged = true
        customerUiState = customerUiState.copy(customer = customerDetail.toCustomer())
    }

    fun setSourceTarget(customer: Customer) {
        val transactionDetail = transactionState.value.transactionDetail
        val newTransactionDetail : TransactionDetail? = when(transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                transactionDetail.copy(
                    sourceID = customer.id,
                    sourceUUID = customer.uuid
                )
            }
            TransactionType.Outgoing -> {
                transactionDetail.copy(
                    targetID = customer.id,
                    targetUUID = customer.uuid
                )
            }
            TransactionType.Transfer -> {
                transactionDetail.copy(
                    targetID = customer.id,
                    targetUUID = customer.uuid
                )
            }
            TransactionType.TransferTarget -> {
                transactionDetail.copy(
                    sourceID = customer.id,
                    sourceUUID = customer.uuid
                )
            }

            else -> {
                null
            }
        }
        //transactionID 0 ise stateden değişiklik yapacağız.
        if (transactionState.value.transactionDetail.transactionID == 0L ) {

            if (newTransactionDetail != null) {
                setTransactionDetail(newTransactionDetail)
            }

        } else {
            //0 gelmezse veritabanında güncellememiz lazım
            viewModelScope.launch(Dispatchers.IO) {
                newTransactionDetail?.let {
                    updateTransaction(it)
                }
            }
        }


    }

    private suspend fun updateTransaction(transactionDetail: TransactionDetail) = transactionRepository.updateTransactionDetail(transactionDetail)

    private fun setTransactionDetail(transactionDetail: TransactionDetail) = transactionRepository.setTransactionDetail(transactionDetail)

}