package tr.com.gndg.self.core.pdfService.models

import java.math.BigDecimal

data class OutgoingPdfModel(
    val documentNumber: String?,
    val date: String,
    val sku: String?,
    val productName: String,
    val customerName: String?,
    val warehouseName: String,
    val piece: Float,
    val unitPrice: BigDecimal?,
    val totalPrice : BigDecimal?,
    val discountPercent: Float?,
    val description: String?
)
