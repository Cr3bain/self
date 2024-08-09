package tr.com.gndg.self.ui.home.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.ui.home.domain.HomeRepository
import tr.com.gndg.self.ui.home.domain.model.HomeUIState

class HomeViewModel(repository: HomeRepository, private val transactionRepository: TransactionRepository) : ViewModel() {

    init {
        repository.getHomeUIState()
    }

    private val _homeUiState = MutableStateFlow(
        repository.homeState
    )

    val homeUiState: StateFlow<MutableState<HomeUIState>> get() = _homeUiState

    fun resetTransaction() = transactionRepository.resetTransactionState()

}