package com.example.bjbo.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.Transaction

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvPaymentType: TextView = itemView.findViewById(R.id.tvPaymentType)
        val tvTransactionStatus: TextView = itemView.findViewById(R.id.tvTransactionStatus)
        val tvGrossAmount: TextView = itemView.findViewById(R.id.tvGrossAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.tvOrderId.text = "Order ID: ${transaction.orderId}"
        holder.tvPaymentType.text = "Payment Type: ${transaction.paymentType}"
        holder.tvTransactionStatus.text = "Status: ${transaction.transactionStatus}"
        holder.tvGrossAmount.text = "Amount: Rp ${transaction.grossAmount}"
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
