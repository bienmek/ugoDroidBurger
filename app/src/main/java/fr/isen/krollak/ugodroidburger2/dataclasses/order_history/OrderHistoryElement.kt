package fr.isen.krollak.ugodroidburger2.dataclasses.order_history

import kotlinx.serialization.Serializable

@Serializable
data class OrderHistoryElement(
    val id_sender: String,
    val id_receiver: String,
    val sender: String,
    val receiver: String,
    val code: String,
    val type_msg: String,
    val message: String,
    val create_date: String
)
