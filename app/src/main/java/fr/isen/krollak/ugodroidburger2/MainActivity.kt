package fr.isen.krollak.ugodroidburger2

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.isen.krollak.ugodroidburger2.dataclasses.order.Order
import fr.isen.krollak.ugodroidburger2.dataclasses.order.OrderQuery
import fr.isen.krollak.ugodroidburger2.interfaces.ApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private var BASE_URL: String = "http://test.api.catering.bluecodegames.com"

    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var spinner: Spinner
    private lateinit var displayTime: TextView
    private lateinit var orderButton: Button

    private lateinit var formTime: String

    private var isTimeSet: Boolean = false
    private var isAllFieldsChecked: Boolean = false

    @OptIn(ExperimentalSerializationApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createSpinner()
        timePickerHandler()

        nameInput = findViewById(R.id.name)
        surnameInput = findViewById(R.id.surname)
        addressInput = findViewById(R.id.address)
        phoneInput = findViewById(R.id.phone)
        orderButton = findViewById(R.id.order_btn)

        orderButton.setOnClickListener {
            val name: String = nameInput.text.toString()
            val surname: String = surnameInput.text.toString()
            val address: String = addressInput.text.toString()
            val phone: String = phoneInput.text.toString()
            val burger: String = spinner.selectedItem.toString()

            isAllFieldsChecked = checkAllFields()

            if (isAllFieldsChecked) {
                val contentType: MediaType = "application/json".toMediaType()
                val json = Json { ignoreUnknownKeys = true }
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .build()

                val apiService = retrofit.create(ApiService::class.java)
                val query =
                    Json.encodeToJsonElement(
                        OrderQuery(
                            "1",
                            33,
                            Json.encodeToString(
                                Order(
                                    name,
                                    surname,
                                    address,
                                    phone,
                                    burger,
                                    formTime
                                )
                            )
                        )
                    )

                val call = apiService.postOrder(query)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            val i = Intent(this@MainActivity, OrderSuccess::class.java)
                            startActivity(i)
                        } else {
                            showErrorToast()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        showErrorToast()
                    }
                })
            }
        }
    }

    private fun checkAllFields(): Boolean {
        if (nameInput.text.isEmpty()) {
            nameInput.error = "Vous devez compléter ce champ"
            return false
        }

        if (surnameInput.text.isEmpty()) {
            surnameInput.error = "Vous devez compléter ce champ"
            return false
        }

        if (addressInput.text.isEmpty()) {
            addressInput.error = "Vous devez compléter ce champ"
            return false
        }

        if (phoneInput.text.isEmpty()) {
            phoneInput.error = "Vous devez compléter ce champ"
            return false
        }

        if (!isTimeSet) {
            displayTime.error = "Vous devez choisir un horaire"
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    private fun showErrorToast() {
        runOnUiThread {

            val inflater = layoutInflater
            val layout: View =
                inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

            val text: TextView = layout.findViewById(R.id.text)
            text.text =
                "Une erreur est survenue lors de l'enregistrement de votre commande. \nVeuillez réessayer plus tard."

            with(Toast(applicationContext)) {
                setGravity(Gravity.TOP, 0, 20)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }
    }

    private fun createSpinner() {
        spinner = findViewById(R.id.burger_list)
        ArrayAdapter.createFromResource(
            this,
            R.array.burger_list,
            R.layout.spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            spinner.adapter = adapter
        }
    }

    @SuppressLint("DefaultLocale")
    private fun timePickerHandler() {
        displayTime = findViewById(R.id.displayTime)

        displayTime.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)
            val startSecond = currentTime.get(Calendar.SECOND)
            val day = currentTime.get(Calendar.DATE)
            val month = currentTime.get(Calendar.MONTH)
            val year = currentTime.get(Calendar.YEAR)

            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    formTime = String.format(
                        "%02d/%02d/%02d %02d:%02d:%02d",
                        day,
                        month,
                        year,
                        hourOfDay,
                        minute,
                        startSecond
                    )
                    displayTime.text = String.format("%02d : %02d", hourOfDay, minute)
                    isTimeSet = true
                },
                startHour,
                startMinute,
                false
            ).show()
        }
    }
}


