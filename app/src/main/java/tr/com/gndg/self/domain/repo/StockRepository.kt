package tr.com.gndg.self.domain.repo

import tr.com.gndg.self.domain.model.Stock

interface StockRepository {

    fun getWarehouseStocks(warehouseUUID: String): List<Stock>

    fun getAllStocks() : List<Stock>
}