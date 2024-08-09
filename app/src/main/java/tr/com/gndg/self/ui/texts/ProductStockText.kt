package tr.com.gndg.self.ui.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import tr.com.gndg.self.core.util.floatToString
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.repo.WarehouseRepository

@Composable
fun ProductStockText(
    warehouseUUID: String?,
    stockList: List<Stock>
) : Float? {
    val warehouseRepository : WarehouseRepository = koinInject()
    val warehouseState by warehouseRepository.warehouseUiState.collectAsState()

    val pieceFloat = if (warehouseUUID == null) {
        //warehouse UUID null. Will take from STATE
        if (warehouseState.warehouse.id !=0L) {
            val stock = stockList.filter { i-> i.warehouseUUID == warehouseState.warehouse.uuid }
            if (stock.isNotEmpty()) {
                stock.first().piece
            } else {
                0F
            }

        } else {
            var piece = 0F
            stockList.forEach {
                piece += it.piece?:0F
            }
            piece
        }
    } else {
        //WE HAVE WAREHOUSE UUID. Probably from StockStateScreen
        stockList.find { i-> i.warehouseUUID == warehouseUUID }?.piece
    }

    Text(text = floatToString(pieceFloat),
        style = MaterialTheme.typography.bodyLarge)

    return pieceFloat
}