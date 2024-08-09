package tr.com.gndg.self.domain.model

data class UserDetail(
    val premium: Boolean = false,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val purchaseTime : Long? = null,
    val orderId: String? = null
)