package com.mkielar.pwr.email.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mkielar.pwr.R
import com.mkielar.pwr.email.model.Email

class EmailRecyclerAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<EmailViewHolder>() {
    private var emails: List<Email> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder =
        EmailViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.email_item,
                parent,
                false
            ) as CardView
        )

    override fun getItemCount(): Int = emails.size

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emails[position], onItemClick)
    }

    fun setData(emails: List<Email>) {
        this.emails = emails
        notifyDataSetChanged()
    }

}