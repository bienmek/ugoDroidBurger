package fr.isen.krollak.ugodroidburger2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.isen.krollak.ugodroidburger2.dataclasses.order.Order
import fr.isen.krollak.ugodroidburger2.dataclasses.order_history.OrderHistoryQuery
import fr.isen.krollak.ugodroidburger2.dataclasses.order_history.OrderHistoryResponse
import fr.isen.krollak.ugodroidburger2.interfaces.ApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class OrderSuccess : AppCompatActivity() {

    private var BASE_URL: String = "http://test.api.catering.bluecodegames.com"
    private val json: Json = Json { ignoreUnknownKeys = true }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_success)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        retrieveOrders()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun retrieveOrders() {
        val contentType: MediaType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val query =
            Json.encodeToJsonElement(
                OrderHistoryQuery(
                    "1",
                    33
                )
            )

        val call = apiService.listOrders(query)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val orderResponse: OrderHistoryResponse = json.decodeFromString<OrderHistoryResponse>(response.body().toString())

                    println(json.decodeFromString<Order>(orderResponse.data[0].message).firstname)
                    println(json.decodeFromString<Order>(orderResponse.data[0].message).lastname)
                    println(json.decodeFromString<Order>(orderResponse.data[0].message).address)
                    println(json.decodeFromString<Order>(orderResponse.data[0].message).burger)
                    println(json.decodeFromString<Order>(orderResponse.data[0].message).delivery_time)

                } else {
                    Log.e("OrderSuccess", "Failed to post order: ${response.code()}")

                    showErrorToast()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("OrderSuccess", "Error: ${t.message}")

                showErrorToast()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showErrorToast() {
        runOnUiThread {

            val inflater = layoutInflater
            val layout: View =
                inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

            val text: TextView = layout.findViewById(R.id.text)
            text.text =
                "Une erreur est survenue lors de la récupération de votre historique de commandes."

            with(Toast(applicationContext)) {
                setGravity(Gravity.TOP, 0, 20)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }
    }
}

