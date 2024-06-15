package fr.isen.krollak.ugodroidburger2.dataclasses.order_history

import kotlinx.serialization.Serializable

@Serializable
data class OrderHistoryResponse(
    val data: List<OrderHistoryElement>
)
