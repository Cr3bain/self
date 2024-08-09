package tr.com.gndg.self.ui.warehouses

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.core.util.Constants
import tr.com.gndg.self.core.util.Constants.WAREHOUSE_LIMIT
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.ui.dialogs.SelfAlertDialogScreen
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.warehouses.presentation.WarehousesViewModel

object WarehousesDestination : NavigationDestination {
    override val route = "warehouses"
    override val titleRes = R.string.warehouseScreen_title
    const val stateOrListArg = "stateOrList"
    val routeWithArgs = "${route}/{$stateOrListArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehousesScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack : Boolean = true,
    toWarehouseFormScreen: (String?) -> Unit,
    viewModel: WarehousesViewModel
) {
    LaunchedEffect(key1 = viewModel){
        viewModel.getWarehouses()
    }

    val warehouseList = viewModel.warehouseList.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val openDeleteAlertDialog = remember { mutableStateOf(false) }

    val openLimitAlertDialog = remember { mutableStateOf(false) }

    val openWarehouseDeleteFailDialog = remember { mutableStateOf(false) }

val context = LocalContext.current

    val premiumUser by context.sharedPreferencesBoolean(Constants.PREMIUM)

    val selectedWarehouse = remember {
        mutableStateOf<Warehouse?>(null)
    }

    val scope = rememberCoroutineScope()

    when {
        // ...
        openWarehouseDeleteFailDialog.value -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = { openWarehouseDeleteFailDialog.value = false },
                dialogTitle = stringResource(id = R.string.warehouseHaveTransactionTitle),
                dialogText = stringResource(id = R.string.warehouseHaveTransactionText),
                icon = Icons.Filled.Info,
                dismissButton = null
            )
        }
        openDeleteAlertDialog.value -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    if (selectedWarehouse.value != null) {
                        scope.launch {
                            viewModel.deleteWarehouseRequest(selectedWarehouse.value!!)
                                .onSuccess {zeroTransaction ->
                                if (zeroTransaction) {
                                    viewModel.deleteWarehouse(selectedWarehouse.value!!, context)
                                } else {
                                    openWarehouseDeleteFailDialog.value = true
                                }
                            }
                                .onFailure {
                                    this.launch(Dispatchers.Main) {
                                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }

                    }
                    openDeleteAlertDialog.value = false
                },
                dialogTitle = stringResource(id = R.string.areYouSure),
                dialogText = stringResource(id = R.string.warehouseDeleteText, selectedWarehouse.value?.name?:"") ,
                icon = Icons.Default.Info,
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDeleteAlertDialog.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )
}
        openLimitAlertDialog.value -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = { openLimitAlertDialog.value = false },
                dialogTitle = stringResource(id = R.string.warehouseLimited),
                dialogText = stringResource(id = R.string.warehouseLimitText, WAREHOUSE_LIMIT),
                icon = Icons.Filled.Info,
                dismissButton = null
            )
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = stringResource(WarehousesDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp,
                navigateBack = navigateBack,
                action = {

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (premiumUser) {
                    toWarehouseFormScreen(null)
                } else {
                    if ( warehouseList.value.size >= WAREHOUSE_LIMIT) {
                        openLimitAlertDialog.value = true
                    } else {
                        toWarehouseFormScreen(null)
                    }
                }

            },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()) {
                Icon(Icons.Filled.Add, "Add Warehouse")

            }
        }
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

        WarehouseBody(modifier = Modifier
            .padding(innerPadding)
            .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
            .fillMaxSize(),
            warehouseList = warehouseList,
            deleteWarehouse = {
                selectedWarehouse.value = it
                openDeleteAlertDialog.value = true
                },
            toWarehouseFormScreen = { toWarehouseFormScreen(it) }
            )

    }

}

@Composable
fun WarehouseBody(
    modifier : Modifier,
    warehouseList: State<List<Warehouse>>,
    deleteWarehouse : (Warehouse) -> Unit,
    toWarehouseFormScreen: (String?) -> Unit,
) {
    LazyColumn(
        modifier= modifier
    ) {
        
        items(warehouseList.value) {
            WareHouseListItem(warehouse = it,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)),
                deleteWarehouse = deleteWarehouse,
                toWarehouseFormScreen = toWarehouseFormScreen)
        }
    }
}

@Composable
fun WareHouseListItem(
    warehouse: Warehouse,
    modifier: Modifier,
    deleteWarehouse : (Warehouse) -> Unit,
    toWarehouseFormScreen: (String?) -> Unit,
) {
    Card(
        modifier = modifier.clickable {
            toWarehouseFormScreen(warehouse.uuid)
        }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant),
            headlineContent = {
                Text(text = warehouse.name)
            },
            supportingContent = { Text(text = warehouse.description?:"") },
            trailingContent =  {
                if (warehouse.id != 1L) {
                    IconButton(onClick = { deleteWarehouse(warehouse) }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete Warehouse")
                    }
                }

            }
        )
    }

}

