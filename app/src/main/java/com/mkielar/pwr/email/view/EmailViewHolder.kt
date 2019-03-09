package com.mkielar.pwr.email.view

import android.text.format.DateFormat
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mkielar.pwr.R
import com.mkielar.pwr.email.model.Email

class EmailViewHolder(private val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    fun bind(
        email: Email,
        onItemClick: (Int) -> Unit
    ) {
        cardView.setOnClickListener { onItemClick(email.id) }
        cardView.findViewById<AppCompatTextView>(R.id.title).text = email.title
        cardView.findViewById<AppCompatTextView>(R.id.content).text = email.sender

        val format = if (System.currentTimeMillis() - email.timestamp * 1000 > 24 * 60 * 60 * 1000) "dd.MM" else "hh:mm"
        cardView.findViewById<AppCompatTextView>(R.id.time).text = DateFormat.format(format, email.timestamp * 1000)
    }
}