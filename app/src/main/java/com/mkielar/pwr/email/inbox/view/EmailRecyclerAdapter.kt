package com.mkielar.pwr.email.inbox.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mkielar.pwr.R
import com.mkielar.pwr.email.inbox.model.Email

class EmailRecyclerAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<EmailViewHolder>(), Filterable {
    private var emails: List<Email> = emptyList()
    private var filtered: MutableList<Email> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder =
        EmailViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.email_item,
                parent,
                false
            ) as CardView
        )

    override fun getItemCount(): Int = filtered.size

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(filtered[position], onItemClick)
    }

    fun setData(emails: List<Email>) {
        this.emails = emails
        filtered = emails.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint.toString()

            filtered.clear()
            emails.forEach {
                if (it.title.contains(query, true) || it.sender.contains(query, true)) {
                    filtered.add(it)
                }
            }

            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

}