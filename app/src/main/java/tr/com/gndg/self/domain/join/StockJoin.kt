package tr.com.gndg.self.domain.join

import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.Warehouse

data class StockJoin(
    val stock: Stock,
    val product: Product?,
    val warehouse: Warehouse?
)