package tr.com.gndg.self.ui.scopes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tr.com.gndg.self.domain.repo.StockRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository

@Composable
fun WarehouseStateStockPiece(
    modifier: Modifier,
    textStyle: TextStyle,
) {

    val warehouseRepository: WarehouseRepository = koinInject()
    val stockRepository: StockRepository = koinInject()
    val warehouseState by warehouseRepository.warehouseUiState.collectAsState()
    var piece by remember {
        mutableFloatStateOf(0F)
    }

    LaunchedEffect(key1 = warehouseState) {
        this.launch(Dispatchers.IO) {
            if (warehouseState.warehouse.id != 0L) {
                stockRepository.getWarehouseStocks(warehouseState.warehouse.uuid).forEach {
                    if (it.piece != null) {
                        piece += it.piece
                    }
                }
            } else {
                stockRepository.getAllStocks().forEach {
                    if (it.piece != null) {
                        piece += it.piece
                    }
                }
            }
        }

    }


    Text(text = piece.toString(), modifier = modifier,
        style = textStyle)


}
