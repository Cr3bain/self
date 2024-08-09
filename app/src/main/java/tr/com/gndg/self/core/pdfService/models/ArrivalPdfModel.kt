package tr.com.gndg.self.core.pdfService.models

import java.math.BigDecimal

data class ArrivalPdfModel(
    val documentNumber: String?,
    val date: String,
    val sku: String?,
    val productName: String,
    val supplierName: String?,
    val warehouseName: String,
    val piece: Float,
    val unitPrice: BigDecimal?,
    val totalPrice : BigDecimal?,
    val discountPercent: Float?,
    val description: String?
)
