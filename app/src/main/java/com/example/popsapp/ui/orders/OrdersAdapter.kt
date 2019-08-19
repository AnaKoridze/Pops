package com.example.popsapp.ui.orders

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.popsapp.R
import com.example.popsapp.data.model.orders.Order
import com.example.popsapp.utils.inflate
import kotlinx.android.synthetic.main.order_list_content.view.*

class OrdersAdapter(private val onOrderClickListener: OnOrderClickListener) :
    ListAdapter<Order, OrdersAdapter.OrderViewHolder>(CompanyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(parent.inflate(R.layout.order_list_content))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position), onOrderClickListener)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, onOrderClickListener: OnOrderClickListener) {
            itemView.product_id.text = order.orderId
            itemView.email.text = order.email
            itemView.status.text = order.orderStatus
            itemView.setOnClickListener {
                onOrderClickListener.onOrderClicked(order.orderId)
            }
        }
    }
}

private class CompanyDiffUtil : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}