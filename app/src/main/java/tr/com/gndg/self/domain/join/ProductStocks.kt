package tr.com.gndg.self.domain.join


import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.Stock

data class ProductStocks(
    val product: Product,
    val stock: List<Stock>
)