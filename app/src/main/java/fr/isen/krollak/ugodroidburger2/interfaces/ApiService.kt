package fr.isen.krollak.ugodroidburger2.interfaces

import kotlinx.serialization.json.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/user/order")
    fun postOrder(@Body query: JsonElement): Call<Void>

    @POST("/listorders")
    fun listOrders(@Body query: JsonElement): Call<JsonElement>
}
