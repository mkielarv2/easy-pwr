package com.mkielar.pwr.email.view

import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mkielar.pwr.R
import com.mkielar.pwr.email.model.Email

class EmailViewHolder(private val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    fun bind(email: Email) {
        cardView.findViewById<AppCompatTextView>(R.id.title).text = email.title
        cardView.findViewById<AppCompatTextView>(R.id.content).text = email.sender
    }
}