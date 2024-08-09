package tr.com.gndg.self.ui.scopes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tr.com.gndg.self.domain.repo.WarehouseRepository

@Composable
fun WarehouseNameText(
    modifier: Modifier,
    warehouseUUID: String?,
    style: TextStyle,
    textAlign: TextAlign?
) {
    val warehouseRepository : WarehouseRepository = koinInject()
    val warehouseState by warehouseRepository.warehouseUiState.collectAsState()
    var warehouseName by remember {
        mutableStateOf("")
    }

    if (warehouseUUID == null) {
        warehouseName = "---" + warehouseState.warehouse.name.uppercase() + "---"
    } else {
        LaunchedEffect(key1 = warehouseName) {
            this.launch(Dispatchers.IO) {
                warehouseRepository.getWarehouseUUID(warehouseUUID)?.let {
                    warehouseName = it.name
                }
            }
        }
    }


    Text(modifier = modifier,
        text = warehouseName,
        textAlign = textAlign,
        style = style)

}