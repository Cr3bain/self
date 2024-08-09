package tr.com.gndg.self.ui.warehouses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.core.preferences.sharedPreferencesLong
import tr.com.gndg.self.core.util.Constants
import tr.com.gndg.self.core.util.warehouseZero
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.toWarehouseUiState
import tr.com.gndg.self.ui.dialogs.SelfAlertDialogScreen
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.search.SearchRow
import tr.com.gndg.self.ui.warehouses.presentation.WarehousesViewModel

object WarehouseSelectDestination : NavigationDestination {
    override val route = "warehouse_select"
    override val titleRes = R.string.warehouseSelectScreen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseSelectScreen(
    navigateBack: () -> Boolean,
    canNavigateBack: Boolean,
    viewModel: WarehousesViewModel = koinViewModel()
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.getWarehouses()
    }
    val context = LocalContext.current
    val warehouseUiState = viewModel.warehouseUiState.collectAsState()
    val warehouseList = viewModel.warehouseList.collectAsState()
    var searchVisiblePref by context.sharedPreferencesBoolean("wh_select_search")
    var searchVisible by remember { mutableStateOf(searchVisiblePref)  }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val warehouseMutableList = warehouseList.value.toMutableList()

    var warehousePreferences by context.sharedPreferencesLong("warehouse")
    val premiumUser by context.sharedPreferencesBoolean(Constants.PREMIUM)
    val openLimitAlertDialog = remember { mutableStateOf(false) }

    warehouseMutableList.add(
        warehouseZero(context)
    )
    when {
        openLimitAlertDialog.value -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = { openLimitAlertDialog.value = false },
                dialogTitle = stringResource(id = R.string.warehouseLimited),
                dialogText = stringResource(id = R.string.warehouseLimitText, Constants.WAREHOUSE_LIMIT),
                icon = Icons.Filled.Info,
                dismissButton = null
            )
        }
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                SelfTopAppBar(
                    title = stringResource(WarehouseSelectDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    scrollBehavior = scrollBehavior,
                    navigateBack = {
                        navigateBack()
                    },
                    action = {
                        IconButton(onClick = {
                            searchVisiblePref = !searchVisiblePref
                            searchVisible = !searchVisible
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )

                AnimatedVisibility(
                    visible = searchVisible,
                ) {

                    SearchRow(
                        onSearch = viewModel::searchWarehouse,
                        searchTextState = viewModel.searchText,
                        startBarcodeScanner = {},
                        showBarcodeScannerIcon = false,
                        hint = stringResource(id = R.string.searchWarehouse)
                    )

                }
            }

        },

    ) {innerPadding->

        WarehouseNameText(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .height(20.dp),
            null,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        WarehouseSelectBody(modifier = Modifier
            .padding(innerPadding)
            .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
            .fillMaxSize(),
            warehouseList = warehouseMutableList.toList(),
            selectedWarehouse = warehouseUiState.value.warehouse.id,
            setWarehouseState = {
                if (premiumUser) {
                    warehousePreferences = it.id
                    viewModel.setPrimeWarehouseUiState(it.toWarehouseUiState())
                } else {
                    if ( warehouseList.value.size >= Constants.WAREHOUSE_LIMIT && it.id != 0L) {
                        val sortList = warehouseList.value.sortedBy { i-> i.id }
                        if (sortList.indexOf(it) >= Constants.WAREHOUSE_LIMIT) {
                            //show alert
                            openLimitAlertDialog.value = true

                        } else {
                            warehousePreferences = it.id
                            viewModel.setPrimeWarehouseUiState(it.toWarehouseUiState())
                        }
                    } else {
                        warehousePreferences = it.id
                        viewModel.setPrimeWarehouseUiState(it.toWarehouseUiState())
                    }
                }

            }
            )

    }

}

@Composable
fun WarehouseSelectBody(
    modifier: Modifier,
    warehouseList: List<Warehouse>,
    selectedWarehouse: Long,
    setWarehouseState: (Warehouse) -> Unit
) {

    LazyColumn(modifier= modifier) {

        items(warehouseList.sortedBy { i-> i.id }) {
            WareHouseSelectItem(
                warehouse = it,
                setWarehouseState = setWarehouseState,
                shine = selectedWarehouse == it.id)
        }

    }
}

@Composable
fun WareHouseSelectItem(
    warehouse: Warehouse,
    setWarehouseState : (Warehouse) -> Unit,
    shine: Boolean,
) {
    Card(
        modifier = Modifier
            .clickable {
                setWarehouseState(warehouse)
            }
            .padding(dimensionResource(id = R.dimen.padding_extra_small)),

    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant),
            leadingContent = {
                         if (shine) {
                             Icon(imageVector = Icons.Filled.Done, contentDescription = "warehouse State")
                         }
            },
            headlineContent = {
                Text(
                    text = warehouse.name.uppercase())
            },

        )
    }

}