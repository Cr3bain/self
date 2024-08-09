package tr.com.gndg.self.ui.reports

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.itextpdf.text.pdf.PdfPTable
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.pdfService.PdfService
import tr.com.gndg.self.core.util.Constants.BACKUP_PERMISSIONS
import tr.com.gndg.self.core.util.toastMessage
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.reports.dialogs.ArrivalBySupplierDialog
import tr.com.gndg.self.ui.reports.dialogs.OutgoingByCustomerDialog
import tr.com.gndg.self.ui.reports.dialogs.StockReportDialog
import tr.com.gndg.self.ui.reports.presentation.ReportsViewModel
import java.io.File

object ReportsDestination : NavigationDestination {
    override val route = "reports"
    override val titleRes = R.string.reportsScreen_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
) {

    val viewModel: ReportsViewModel = koinViewModel()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

    topBar =  {
        SelfTopAppBar(
            title = stringResource(ReportsDestination.titleRes),
            scrollBehavior = scrollBehavior,
            canNavigateBack = canNavigateBack ,
            navigateBack = navigateBack,
            navigateUp = onNavigateUp,
            action =  {

            })
    }


) {innerPadding->
    ReportsBody(
        modifier = Modifier.padding(innerPadding),
        viewModel = viewModel)
}

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReportsBody(
    modifier: Modifier,
    viewModel: ReportsViewModel
){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var stockDialog by remember {
        mutableStateOf(false)
    }
    var arrivalBySupplierDialog by remember {
        mutableStateOf(false)
    }

    var outgoingByCustomerDialog by remember {
        mutableStateOf(false)
    }

    val permissionState = rememberMultiplePermissionsState(permissions = BACKUP_PERMISSIONS)

    fun openPdf(file: File) {
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "tr.com.gndg.self.fileprovider",
            file
        )
        val intentShareFile = Intent(Intent.ACTION_VIEW)
        intentShareFile.setDataAndType(fileUri, "application/pdf")
        intentShareFile.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intentShareFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(intentShareFile)
    }

    fun createPdf(
        context: Context,
        table : PdfPTable,
        paragraphLines : List<String>? = null,
        fileName: String,
        pageTitle : String,
        tableTitle: String,
        onFinished: (File) -> Unit
    ) {
        val onError: (Exception) -> Unit = { toastMessage(context, it.message.toString()) }

        val pdfService = PdfService()
        pdfService.createPDF(
            context= context,
            fileName = fileName,
            table =table,
            pageTitle = pageTitle,
            tableTitle = tableTitle,
            paragraphLines = paragraphLines,
            onFinish = onFinished,
            onError = onError
        )
    }


    when {
        stockDialog -> {
            StockReportDialog(
                onDismiss = {  stockDialog = false },
                warehouseList = viewModel.warehouseList,
                selectedWarehouse = {
                    scope.launch {

                        val tableHeader = listOf(
                            context.getString(R.string.sku),
                            context.getString(R.string.productName),
                            context.getString(R.string.stock)
                        )
                        val table = PdfService().createStockListTable(
                            tableHeaderContent = tableHeader,
                            data = viewModel.getProductsJoinList(),
                            warehouseID = it.id
                        )
                        createPdf(
                            context = context,
                            table = table,
                            paragraphLines = null,
                            fileName = context.getString(R.string.stockListFileName),
                            pageTitle = context.getString(R.string.app_name),
                            tableTitle = context.getString(R.string.stockList),
                            onFinished =  {
                                openPdf(it)
                            })
                    }
                }
            )
        }

        arrivalBySupplierDialog -> {
            ArrivalBySupplierDialog(
                onDismiss = {  arrivalBySupplierDialog = false },
                supplierList = viewModel.suppliersList,
                selectedSupplier = {
                    scope.launch {
                        val tableHeader = listOf(
                            context.getString(R.string.docNo),
                            context.getString(R.string.date),
                            context.getString(R.string.sku),
                            context.getString(R.string.product),
                            context.getString(R.string.supplier),
                            context.getString(R.string.warehouse),
                            context.getString(R.string.piece),
                            context.getString(R.string.unitPrice),
                            context.getString(R.string.totalPricePdf)
                        )
                        val table = PdfService().createArrivalListTable(
                            tableHeaderContent = tableHeader,
                            data = viewModel.getArrivalPdf(it)
                        )
                        createPdf(
                            context = context,
                            table = table,
                            paragraphLines = null,
                            fileName = context.getString(R.string.arrivalListFileName),
                            pageTitle = context.getString(R.string.app_name),
                            tableTitle =  context.getString(R.string.arrivalList),
                            onFinished =  {
                                openPdf(it)
                            })
                    }
                }
            )
        }

        outgoingByCustomerDialog -> {
            OutgoingByCustomerDialog(
                onDismiss = {  outgoingByCustomerDialog = false },
                customerList = viewModel.customersList,
                selectedCustomer = {
                    scope.launch {
                        val tableHeader = listOf(
                            context.getString(R.string.docNo),
                            context.getString(R.string.date),
                            context.getString(R.string.sku),
                            context.getString(R.string.product),
                            context.getString(R.string.customer),
                            context.getString(R.string.warehouse),
                            context.getString(R.string.piece),
                            context.getString(R.string.unitPrice),
                            context.getString(R.string.totalPricePdf)
                        )
                        val table = PdfService().createOutgoingListTable(
                            tableHeaderContent = tableHeader,
                            data = viewModel.getOutgoingPdf(it)
                        )
                        createPdf(
                            context = context,
                            table = table,
                            paragraphLines = null,
                            fileName = context.getString(R.string.outgoingListFileName),
                            pageTitle = context.getString(R.string.app_name),
                            tableTitle = context.getString(R.string.outgoingList),
                            onFinished =  {
                                openPdf(it)
                            })
                    }

                }
            )
        }
    }

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {

        Card(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small))
        ) {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(text = stringResource(id = R.string.productList))
                },
                trailingContent = {
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            scope.launch {
                                val tableHeader = listOf(
                                    context.getString(R.string.sku),
                                    context.getString(R.string.barcodeText),
                                    context.getString(R.string.productName),
                                    context.getString(R.string.description)
                                )
                                val table = PdfService().createProductListTable(
                                    tableHeaderContent = tableHeader,
                                    data = viewModel.getProductsJoinList()
                                )
                                createPdf(
                                    context = context,
                                    table = table,
                                    paragraphLines = null,
                                    fileName = context.getString(R.string.productListFileName),
                                    pageTitle = context.getString(R.string.app_name),
                                    tableTitle = context.getString(R.string.productList),
                                    onFinished =  {
                                        openPdf(it)
                                    })
                            }
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }

                    }) {
                        Icon(painter = painterResource(id = R.drawable.pdf_24), contentDescription = "Product List to pdf")
                    }
                }
            )

        }
        Card(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small))
        ) {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(text = stringResource(id = R.string.stockList))
                },
                trailingContent = {
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            scope.launch {
                                viewModel.getWarehouseList().let {
                                    stockDialog = true
                                }
                            }
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }

                    }) {
                        Icon(painter = painterResource(id = R.drawable.pdf_24), contentDescription = "Stock list to pdf")
                    }
                }
            )

        }
        Card(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small))
        ) {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(text = stringResource(id = R.string.arrivalBySupplier))
                },
                trailingContent = {
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            scope.launch {
                                viewModel.getSuppliers().let {
                                    arrivalBySupplierDialog = true
                                }

                            }
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }

                    }) {
                        Icon(painter = painterResource(id = R.drawable.pdf_24), contentDescription = "Arrivals to Pdf")
                    }
                }
            )
        }

        Card(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small))
        ) {

            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(text = stringResource(id = R.string.outgoingByCustomer))
                },
                trailingContent = {
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            scope.launch {
                                viewModel.getCustomers().let {
                                    outgoingByCustomerDialog = true
                                }
                            }
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }

                    }) {
                        Icon(painter = painterResource(id = R.drawable.pdf_24), contentDescription = "Outgoings to Pdf")
                    }
                }
            )
        }

    }

}