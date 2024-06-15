package fr.isen.krollak.ugodroidburger2.dataclasses.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderQuery(
    val id_shop: String,
    val id_user: Int,
    val msg: String
)
