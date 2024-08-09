package tr.com.gndg.self.ui.suppliers.presentation

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
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.SupplierDetail
import tr.com.gndg.self.domain.model.SupplierUiState
import tr.com.gndg.self.domain.model.toSupplier
import tr.com.gndg.self.domain.model.toSupplierUiState
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.repo.SupplierRepository
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.ui.suppliers.SupplierFormDestination

class SuppliersViewModel(
    savedStateHandle: SavedStateHandle,
    private val supplierRepository: SupplierRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

/*    private var _supplierUiState = supplierRepository.supplierUiState
    val supplierUiState = _supplierUiState.asStateFlow()*/

    private var _uuidArg :String? by mutableStateOf(null)

    init {
        _uuidArg = savedStateHandle[SupplierFormDestination.supplierUUIDArgs]
    }

    val transactionState = transactionRepository.transactionState

    var supplierUiState by mutableStateOf(SupplierUiState())
        private set

    var somethingChanged by mutableStateOf(false)

    private var firstSupplierList= listOf<Supplier>()
    private var isSearchStarting = true
    var searchText = mutableStateOf("")

    private var _supplierList = MutableStateFlow<List<Supplier>>(emptyList())
    val supplierList  get() = _supplierList.asStateFlow()

    fun setSourceTarget(supplier: Supplier) {
        val transactionDetail = transactionState.value.transactionDetail
        val newTransactionDetail : TransactionDetail? = when(transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                transactionDetail.copy(
                    sourceID = supplier.id,
                    sourceUUID = supplier.uuid
                )
            }
            TransactionType.Outgoing -> {
                transactionDetail.copy(
                    targetID = supplier.id,
                    targetUUID = supplier.uuid
                )
            }
            TransactionType.Transfer -> {
                transactionDetail.copy(
                    targetID = supplier.id,
                    targetUUID = supplier.uuid
                )
            }
            TransactionType.TransferTarget -> {
                transactionDetail.copy(
                    sourceID = supplier.id,
                    sourceUUID = supplier.uuid
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

    suspend fun getSupplierByUUID(supplierUUID: String) {
        supplierRepository.getSupplierUUID(supplierUUID)?.let {
            supplierUiState = it.toSupplierUiState()
        }
    }

    fun setSupplierUiState(supplierDetail: SupplierDetail) {
        somethingChanged = true
        supplierUiState = supplierUiState.copy(supplier = supplierDetail.toSupplier())
    }

    suspend fun saveSupplier() : Result<Boolean> = withContext(Dispatchers.IO) {

           return@withContext if (_uuidArg != null) {
                //update
                supplierRepository.updateSupplier(supplierUiState.supplier)
                    .onSuccess {
                        somethingChanged = false
                    }
                    .onFailure {
                    }

            } else {
                //new
                supplierRepository.insertSupplier(supplierUiState.supplier)
                    .onSuccess {
                        somethingChanged = false
                    }
                    .onFailure {

                    }
            }
    }

    suspend fun deleteSupplier() : Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext supplierRepository.deleteSupplier(supplierUiState.supplier)
    }

    suspend fun getSuppliers() = withContext(Dispatchers.IO){
        supplierRepository.getSuppliers()
    }.let {
        _supplierList.value = it
    }

    fun searchSupplier(query : String){

        val listToSearch= if(isSearchStarting){
            _supplierList.value
        } else{
            firstSupplierList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                _supplierList.value=firstSupplierList
                isSearchStarting=true
                return@launch
            }
            val results=listToSearch.filter {
                it.name.contains(query.trim(),true) ||
                        it.description?.contains(query.trim(),true) == true
            }
            if(isSearchStarting){
                firstSupplierList=_supplierList.value
                isSearchStarting=false

            }
            _supplierList.value= results
        }

    }

}