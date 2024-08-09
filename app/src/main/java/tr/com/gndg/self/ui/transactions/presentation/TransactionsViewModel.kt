package tr.com.gndg.self.ui.transactions.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository

class TransactionsViewModel(private val transactionRepository: TransactionRepository, warehouseRepository: WarehouseRepository) : ViewModel() {

    private var firstTransactionList= listOf<TransactionJoinData>()
    private var isSearchStarting = true
    var searchText = mutableStateOf("")

    val warehouseUiState = warehouseRepository.warehouseUiState.asStateFlow()

    private val _transactionJoinData = MutableStateFlow<List<TransactionJoinData>>(emptyList())
    val transactionJoinDataList  get() = _transactionJoinData.asStateFlow()

    suspend fun getTransactions() {
        _transactionJoinData.value = transactionRepository.getTransactionJoinDataList()
    }

    fun searchTransaction(query : String){

        val listToSearch= if(isSearchStarting){
            _transactionJoinData.value
        } else{
            firstTransactionList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                _transactionJoinData.value=firstTransactionList
                isSearchStarting=true
                return@launch
            }
            val results=listToSearch.filter {
                it.transaction.description?.contains(query.trim(),true)?:false ||
                        DateTime(it.transaction.date).toString().contains(query.trim(),true)
            }
            if(isSearchStarting){
                firstTransactionList=_transactionJoinData.value
                isSearchStarting=false

            }
            _transactionJoinData.value= results
        }

    }

}