package com.example.cosmeticszone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FilterActivityAdapter(var dataSet: Array<FilterDetails>, val context: Context)  : RecyclerView.Adapter<FilterActivityAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var filterNameView: TextView
        var filterCardView: CardView

        init {
            // Define click listener for the ViewHolder's View.
            filterNameView = view.findViewById(R.id.filterNameView)
            filterCardView = view.findViewById(R.id.filterCardView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FilterActivityAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.filter_row_adapter, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: FilterActivityAdapter.ViewHolder, position: Int) {

        // Get element from yo ur dataset at this position and replace the
        // contents of the view with that element
        val product = dataSet[position]

        viewHolder.filterNameView.text = product.name
        if(product.choose){
            viewHolder.filterCardView.setCardBackgroundColor("#fededf".toColorInt())
        }else{
            viewHolder.filterCardView.setCardBackgroundColor("#fefefe".toColorInt())
        }
        viewHolder.itemView.setOnClickListener{
            if (context is FilterActivity) {
                context.changeChoice(product, position)
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}