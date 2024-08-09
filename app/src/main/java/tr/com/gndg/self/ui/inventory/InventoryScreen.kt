package tr.com.gndg.self.ui.inventory

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.ui.dialogs.TransactionProductDialog
import tr.com.gndg.self.ui.inventory.presentation.InventoryViewModel
import tr.com.gndg.self.ui.lists.ProductsJoinList
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.search.SearchRow

object InventoryDestination : NavigationDestination {
    override val route = "inventory"
    override val titleRes = R.string.inventoryScreen_title
}

object InventoryTransactionDestination : NavigationDestination {
    override val route = "inventory_transaction"
    override val titleRes = R.string.inventoryScreen_title
    const val transactionIDArg = "transactionID"
    val routeWithTransactionArgs = "$route/{$transactionIDArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    toProductScreen: (String?) -> Unit,
    canNavigateBack: Boolean = true,
    warehouseSelectScreen : () -> Unit,
    viewModel: InventoryViewModel
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.productsJoinList()
    }
    val transactionID = viewModel.transactionIDArg

    val productList : State<List<ProductsJoin>> = viewModel.productList.collectAsState()
    val errorMessage: State<Throwable?> = viewModel.errorList.collectAsState()
    val context = LocalContext.current

    var inventorySearch by context.sharedPreferencesBoolean("inventorySearch")
    var searchVisible by mutableStateOf(inventorySearch)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val warehouseUUID = if (transactionID == null || transactionID == 0L) {
        null
    } else {
        viewModel.warehouseUUID()
    }

    var selectedProductsJoin by remember {
        mutableStateOf<ProductsJoin?>(null)
    }
    var showTransactionProductDialog by remember {
        mutableStateOf(false)
    }

    when {
        showTransactionProductDialog -> {
            if (selectedProductsJoin != null) {
                TransactionProductDialog(
                    onDismissRequest = { showTransactionProductDialog = false },
                    productsJoin = selectedProductsJoin!!,
                    saved = {
                        showTransactionProductDialog = false
                        navigateBack()
                    },
                    failed = {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                SelfTopAppBar(
                    title = stringResource(InventoryDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    scrollBehavior = scrollBehavior,
                    navigateUp = onNavigateUp,
                    navigateBack = navigateBack,
                    action = {
                        IconButton(onClick = warehouseSelectScreen) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_warehouse_24),
                                contentDescription = "Change Warehouse State",
                            )
                        }

                        IconButton(onClick = {
                            searchVisible = !searchVisible
                            inventorySearch = !inventorySearch
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )

                AnimatedVisibility(
                    visible = searchVisible,
                ) {

                    SearchRow(
                        onSearch =  viewModel::searchProduct,
                        searchTextState = viewModel.searchText,
                        startBarcodeScanner = viewModel::startScanning,
                        showBarcodeScannerIcon = true,
                        hint = stringResource(id = R.string.searchProduct)
                    )

                }
            }


        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Column {

                        Text(text = stringResource(id = R.string.products, productList.value.size.toString()))

/*                        Row {
                            IconButton(onClick = { *//* do something *//* }) {
                                Icon(Icons.Filled.Check, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { *//* do something *//* }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = { *//* do something *//* }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_content_copy_24),
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = { *//* do something *//* }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_print_24),
                                    contentDescription = "Localized description",
                                )
                            }
                        }*/
                    }


                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { toProductScreen(null) },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                }
            )
        },
    ) { innerPadding->
        WarehouseNameText(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .height(20.dp),
            warehouseUUID,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        InventoryBody(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                .fillMaxSize(),
                //.verticalScroll(rememberScrollState()),
            productList = productList.value,
            selectedProductsJoin = {
                if (transactionID != null && transactionID != 0L) {
                    selectedProductsJoin = it
                    showTransactionProductDialog = true
                    //viewModel.addProductToTransactionStateList(it, "12", "")
                    //navigateBack()
                    //
                } else {
                    toProductScreen(it.product.uuid)
                }

                                   },
            errorMessage = errorMessage,
            warehouseUUID = warehouseUUID
        )
    }

}

@Composable
fun InventoryBody(
    modifier: Modifier = Modifier,
    productList: List<ProductsJoin>,
    selectedProductsJoin: (ProductsJoin) -> Unit,
    errorMessage: State<Throwable?>,
    warehouseUUID: String?,
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (productList.isEmpty() || errorMessage.value != null) { Arrangement.Center } else { Arrangement.Top },
        modifier = modifier.fillMaxSize()
    )
    {


        if (productList.isEmpty() || errorMessage.value != null) {
            Text(
                text = errorMessage.value?.message?: stringResource(R.string.noProducts),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {

            ProductsJoinList(
                productItemList = productList,
                selectedProductsJoin = selectedProductsJoin,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small)),
                warehouseUUID = warehouseUUID
            )
        }

    }

}
