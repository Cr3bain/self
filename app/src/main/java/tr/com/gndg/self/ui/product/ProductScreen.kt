package tr.com.gndg.self.ui.product

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.domain.join.ProductsJoinUiState
import tr.com.gndg.self.domain.model.ProductDetails
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.toProductDetails
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.ui.dialogs.SelfAlertDialogScreen
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.product.presentation.ProductViewModel
import tr.com.gndg.self.ui.product.stateScreens.ArrivalStockScreen
import tr.com.gndg.self.ui.product.stateScreens.EditStateScreen
import tr.com.gndg.self.ui.product.stateScreens.HistoryStateScreen
import tr.com.gndg.self.ui.product.stateScreens.PictureStateScreen
import tr.com.gndg.self.ui.product.stateScreens.PriceStateScreen
import tr.com.gndg.self.ui.product.stateScreens.StockStateScreen
import java.util.UUID

object ProductDestination : NavigationDestination {
    override val route = "product"
    override val titleRes = R.string.productScreen_title
    const val uuidArg = "productUUID"
    val routeWithUUIDArgs = "$route/{$uuidArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    toSupplierScreen: (Long) -> Unit,
    toCameraScreen: (String) -> Unit,
    viewModel: ProductViewModel = koinViewModel(),
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.uuidArgCollect()
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val productJoinUiState = viewModel.productsJoinUiState
    val productScreenUiState = viewModel.productScreenUiState

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ArrivalStockScreen(
        toSupplierScreen = toSupplierScreen
    )

    var openNotSavedDialog by remember {
        mutableStateOf(false)
    }


    when{
        openNotSavedDialog -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = navigateBack,
                dialogTitle = stringResource(id = R.string.somethingChangeDialogTitle),
                dialogText = stringResource(id = R.string.somethingChangeDialogText),
                icon = Icons.Filled.Info,
                dismissButton = {
                    TextButton(
                        onClick = {
                            openNotSavedDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )
        }
        viewModel.showToast -> {
            Toast.makeText(LocalContext.current, viewModel.toastMessage, Toast.LENGTH_SHORT).show()
            viewModel.showToast = !viewModel.showToast
        }
        viewModel.openDialogDeleteProductFail -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = { viewModel.openDialogDeleteProductFail = false },
                dialogTitle = stringResource(id = R.string.cantDeleteTitle),
                dialogText = stringResource(id = R.string.cantDeleteProduct),
                icon = Icons.Filled.Info,
                dismissButton = null
            )

        }
        viewModel.openDialogDeleteProdcutConfirm -> {

            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    viewModel.openDialogDeleteProdcutConfirm = false

                    scope.launch {
                        viewModel.deleteProduct()
                            .onSuccess {
                                navigateBack()
                            }
                            .onFailure {
                                this.launch(Dispatchers.Main) {
                                    viewModel.toastMessage = it.message.toString()
                                    viewModel.showToast = true
                                }
                            }
                    }

                },
                dialogTitle = stringResource(id = R.string.areYouSure),
                dialogText = stringResource(id = R.string.canDeleteProduct),
                icon = Icons.Filled.Warning,
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.openDialogDeleteProdcutConfirm = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )

        }

    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
                SelfTopAppBar(
                    title = stringResource(ProductDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    scrollBehavior = scrollBehavior,
                    navigateUp = onNavigateUp,
                    navigateBack = {
                        if (!viewModel.somethingChanged) {
                            navigateBack()
                        } else {
                            openNotSavedDialog = true
                        }
                         },
                    action = {
                        AnimatedVisibility(visible = productJoinUiState.product.id != 0L) {
                            IconButton(onClick = {
                                if (productJoinUiState.productTransactions.isNotEmpty()) {
                                    viewModel.openDialogDeleteProductFail = true
                                    Log.v("DeleteProduct", "Transactions is Not Empty")
                                } else {
                                    viewModel.openDialogDeleteProdcutConfirm = true

                                }

                            },
                            ) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")

                            }
                        }
                        IconButton(onClick = {
                            //uiState will be save to Database
                            scope.launch {
                                viewModel.saveProduct(context).onSuccess {
                                    viewModel.toastMessage = it
                                    viewModel.showToast = true
                                }.onFailure {
                                    viewModel.toastMessage = it.message.toString()
                                    viewModel.showToast = true
                                }
                            }

                        },
                            enabled = productJoinUiState.product.name.isNotBlank()
                        ) {
                            Icon(imageVector = Icons.Filled.Done, contentDescription = "save")

                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        AnimatedVisibility(visible = productScreenUiState.showButtons ) {

                            Column {

                                Row {
                                    IconButton(onClick = {
                                        viewModel.changeProductScreenState(
                                            productScreenUiState.copy(showScreenName = "price")
                                        )
                                    }) {
                                        Icon(painter = painterResource(id = R.drawable.baseline_price_change_24),
                                            contentDescription = "Product Price")
                                    }
/*                                    IconButton(onClick = {
                                        viewModel.changeProductScreenState(
                                            productScreenUiState.copy(showScreenName = "edit")
                                        )
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_edit_note_24),
                                            contentDescription = "Product Pref",
                                        )
                                    }*/

                                    IconButton(onClick = {
                                        viewModel.changeProductScreenState(
                                            productScreenUiState.copy(showScreenName = "history")
                                        )
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_history_24),
                                            contentDescription = "Transcript History",
                                        )
                                    }

                                    IconButton(onClick = {
                                        viewModel.changeProductScreenState(
                                            productScreenUiState.copy(showScreenName = "camera")
                                        )
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_camera_24),
                                            contentDescription = "Camera",
                                        )
                                    }
                                    IconButton(onClick = {
                                        viewModel.resetTransactionState()
                                        viewModel.changeProductScreenState(
                                            productScreenUiState.copy(showScreenName = "stock")
                                        )
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_warehouse_24),
                                            contentDescription = "Stock",
                                        )
                                    }
                                }
                            }

                        }

                    },

                )
            },

        ) {innerPadding->

            ProductBody(
                warehouseList = viewModel.warehouseListState,
                startBarcodeScan = { viewModel.startScanning(context = context)} ,
                uiState = productJoinUiState,
                showScreenName = viewModel.productScreenUiState.showScreenName,
                onProductValueChange = viewModel::setProductJoinUiStateProduct,
                modifier = Modifier
                    .padding(innerPadding)
                    //.padding(top = 18.dp)
                    .fillMaxSize(),
                showArrivalStockScreen = viewModel::showArrivalScreen,
                setTransactionDetail = { viewModel.setTransactionDetail(it) },
                setTransactionData =  { viewModel.setTransactionDataListDetail(it) },
                deleteImage = viewModel::deleteImage,
                toCameraScreen = {toCameraScreen(productJoinUiState.product.uuid)}

            )
        }
    }


@Composable
fun ProductBody(
    modifier: Modifier,
    uiState: ProductsJoinUiState,
    onProductValueChange: (ProductDetails) -> Unit,
    showScreenName: String,
    startBarcodeScan: () -> Unit,
    warehouseList: List<Warehouse>,
    showArrivalStockScreen: () -> Unit,
    setTransactionDetail: (TransactionDetail) -> Unit,
    setTransactionData: (TransactionDataDetail) -> Unit,
    deleteImage: (String) -> Unit,
    toCameraScreen: () -> Unit
) {

    Column(
        modifier= modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {

        ProductInputForm(
            productDetails = uiState.product.toProductDetails(),
            onValueChange = onProductValueChange,
            startScan = startBarcodeScan
        )
        AnimatedVisibility(
            visible = showScreenName == "edit") {
            EditStateScreen(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
        AnimatedVisibility(visible = showScreenName == "price") {
            PriceStateScreen(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                productDetails = uiState.product.toProductDetails(),
                onValueChange = onProductValueChange,
            )
        }

        AnimatedVisibility(visible = showScreenName == "camera") {
            PictureStateScreen(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                productsJoinUiState = uiState,
                deleteImage = deleteImage,
                toCameraScreen = toCameraScreen
            )
        }

        AnimatedVisibility(visible = showScreenName == "history") {
            HistoryStateScreen(modifier = Modifier
                .padding(8.dp), productTransactionList= uiState.productTransactions)
        }
        AnimatedVisibility(visible = showScreenName == "stock") {
            StockStateScreen(
                modifier = Modifier
                    .padding(8.dp),
                stockList = uiState.stock,
                warehouseList = warehouseList,
                arrivalStockWarehouse = {
                    // New State for Arrival
                    val newDetail = TransactionDetail(
                        uuid = UUID.randomUUID().toString(),
                        transactionType = TransactionType.Arrival,
                        targetUUID = it.uuid,
                        targetID = it.id,
                    )
                    setTransactionDetail(newDetail).let {

                        TransactionDataDetail(
                            uuid = UUID.randomUUID().toString(),
                            transactionUUID = newDetail.uuid,
                            productUUID = uiState.product.uuid,
                            productID = uiState.product.id
                        ).let {data->
                            setTransactionData(data)

                        }.let {
                            showArrivalStockScreen()
                        }

                    }



            })
        }
    }
}

@Composable
fun ProductInputForm(
    productDetails: ProductDetails,
    onValueChange: (ProductDetails) -> Unit = {},
    startScan: () -> Unit
) {
    Column{

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = productDetails.name,
            onValueChange = { onValueChange(productDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.nameText)) },
            singleLine = true,
            isError = false
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1F),
                value = productDetails.barcode?:"",
                onValueChange = { onValueChange(productDetails.copy(barcode = it))  },
                label = { Text(stringResource(id = R.string.barcodeText)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = false
            )
            Spacer(modifier = Modifier.size(10.dp))
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = startScan ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "Localized description",
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = productDetails.stockCode?:"",
            onValueChange = { onValueChange(productDetails.copy(stockCode = it)) },
            label = { Text(stringResource(id = R.string.stockCodeText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier= Modifier.fillMaxWidth(),
            value = productDetails.description?:"",
            onValueChange = { onValueChange(productDetails.copy(description = it)) },
            label = { Text(stringResource(id = R.string.description)) },
            isError = false
        )

    }

}