package fr.isen.krollak.ugodroidburger2.dataclasses.order_history

import kotlinx.serialization.Serializable

@Serializable
data class OrderHistoryQuery(
    val id_shop: String,
    val id_user: Int
)

