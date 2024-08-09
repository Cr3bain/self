package tr.com.gndg.self.ui.reports.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.core.pdfService.models.ArrivalPdfModel
import tr.com.gndg.self.core.pdfService.models.OutgoingPdfModel
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.Warehouse
import java.math.BigDecimal

class ReportsViewModel(
    private val dataSourceRepository: DataSourceRepository
) : ViewModel() {

    var warehouseList by mutableStateOf<List<Warehouse>>(emptyList())
        private set

    var suppliersList by mutableStateOf<List<Supplier>>(emptyList())
        private set

    var customersList by mutableStateOf<List<Customer>>(emptyList())
        private set

    suspend fun getProductsJoinList() : List<ProductsJoin> = withContext(Dispatchers.IO) {
        return@withContext dataSourceRepository.getProductsJoinList()
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getArrivalPdf(selectedSupplier: Supplier): List<ArrivalPdfModel> = withContext(Dispatchers.IO){
        val arrivalPdfModelList = mutableListOf<ArrivalPdfModel>()
            this.launch {
                dataSourceRepository.getTransactionByType(TransactionType.Arrival, selectedSupplier.id).let { list->
                    if (list.isNotEmpty()) {
                        list.forEach {trs->
                            trs.dataList?.forEach {data->
                                val product = dataSourceRepository.getProduct(data.productUUID)
                                val supplier = trs.transaction.sourceUUID?.let {
                                    dataSourceRepository.getSupplierUUID(
                                        it
                                    )
                                }
                                val warehouse = trs.transaction.targetUUID?.let {
                                    dataSourceRepository.getWarehouseUUID(
                                        it
                                    )
                                }
                                val totalPrice = if (data.unitPrice != null) {
                                    BigDecimal.valueOf(data.piece.toLong()).times(data.unitPrice)
                                } else {
                                    BigDecimal.ZERO
                                }

                                arrivalPdfModelList.add(
                                    ArrivalPdfModel(
                                        documentNumber = trs.transaction.documentNumber,
                                        date = DateTime(trs.transaction.date).toString("dd.MM.yyyy"),
                                        sku = product?.stockCode,
                                        productName = product?.name?:"",
                                        supplierName = supplier?.name?:"",
                                        warehouseName = warehouse?.name?:"",
                                        piece = data.piece,
                                        unitPrice = data.unitPrice?: BigDecimal.ZERO,
                                        totalPrice = totalPrice,
                                        discountPercent = trs.transaction.discountPercent,
                                        description = trs.transaction.description

                                    )
                                )
                            }
                        }
                    }
                }
            }.join()

        return@withContext arrivalPdfModelList
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getOutgoingPdf(selectedCustomer: Customer) : List<OutgoingPdfModel> = withContext(Dispatchers.IO){
        val outgoingPdfModelList = mutableListOf<OutgoingPdfModel>()
        this.launch {
            dataSourceRepository.getTransactionByType(TransactionType.Outgoing, selectedCustomer.id).let { list->
                if (list.isNotEmpty()) {
                    list.forEach {trs->
                        trs.dataList?.forEach {data->
                            val product = dataSourceRepository.getProduct(data.productUUID)
                            val customer = trs.transaction.targetUUID?.let {
                                dataSourceRepository.getCustomerByUUID(
                                    it
                                )
                            }
                            val warehouse = trs.transaction.sourceUUID?.let {
                                dataSourceRepository.getWarehouseUUID(
                                    it
                                )
                            }
                            val totalPrice = if (data.unitPrice != null) {
                                BigDecimal.valueOf(data.piece.toLong()).times(data.unitPrice)
                            } else {
                                BigDecimal.ZERO
                            }

                            outgoingPdfModelList.add(
                                OutgoingPdfModel(
                                    documentNumber = trs.transaction.documentNumber,
                                    date = DateTime(trs.transaction.date).toString("dd.MM.yyyy"),
                                    sku = product?.stockCode,
                                    productName = product?.name?:"",
                                    customerName = customer?.name?:"",
                                    warehouseName = warehouse?.name?:"",
                                    piece = data.piece,
                                    unitPrice = data.unitPrice?: BigDecimal.ZERO,
                                    totalPrice = totalPrice,
                                    discountPercent = trs.transaction.discountPercent,
                                    description = trs.transaction.description

                                )
                            )
                        }
                    }
                }
            }
        }.join()

        return@withContext outgoingPdfModelList
    }

    fun getWarehouseList() {
        viewModelScope.launch(Dispatchers.IO) {
            warehouseList = dataSourceRepository.getWarehouses()
        }

    }

    fun getSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            suppliersList = dataSourceRepository.getSuppliers()
        }
    }

    fun getCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            customersList = dataSourceRepository.getCustomers()
        }
    }
}