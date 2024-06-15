package fr.isen.krollak.ugodroidburger2.dataclasses.order

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val firstname: String,
    val lastname: String,
    val address: String,
    val phone: String,
    val burger: String,
    val delivery_time: String
)
