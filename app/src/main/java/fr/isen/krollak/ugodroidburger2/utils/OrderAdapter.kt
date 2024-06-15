package fr.isen.krollak.ugodroidburger2.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.krollak.ugodroidburger2.R
import fr.isen.krollak.ugodroidburger2.dataclasses.order.Order
import fr.isen.krollak.ugodroidburger2.dataclasses.order_history.OrderHistoryElement
import kotlinx.serialization.json.Json

class OrderAdapter(private val orders: List<OrderHistoryElement>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val burgerTextView: TextView = view.findViewById(R.id.burger)
        val addressTextView: TextView = view.findViewById(R.id.address)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val phoneTextView: TextView = view.findViewById(R.id.phone)
        val deliveryTimeTextView: TextView = view.findViewById(R.id.deliveryTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history_item, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        val message = Json.decodeFromString<Order>(order.message)

        holder.burgerTextView.text = message.burger
        holder.addressTextView.text = message.address
        holder.nameTextView.text = "${message.firstname} " + "${message.lastname}"
        holder.phoneTextView.text = message.phone
        holder.deliveryTimeTextView.text = message.delivery_time
    }

    override fun getItemCount() = orders.size
}
