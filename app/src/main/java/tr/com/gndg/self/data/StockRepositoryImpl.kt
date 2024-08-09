package tr.com.gndg.self.data

import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.repo.StockRepository

class StockRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : StockRepository {
    override fun getWarehouseStocks(warehouseUUID: String): List<Stock> {
        return dataSourceRepository.getWarehouseStocks(warehouseUUID)
    }

    override fun getAllStocks(): List<Stock> {
        return dataSourceRepository.getStocks()
    }

}