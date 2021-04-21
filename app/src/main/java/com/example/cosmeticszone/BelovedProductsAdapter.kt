package com.example.cosmeticszone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BelovedProductsAdapter(val context: Context, val dataSet: ArrayList<ProductDetails>)  : RecyclerView.Adapter<BelovedProductsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productTextView: TextView
        var brandTextView: TextView
        val productImage: ImageView
        val deleteBelovedButton : TextView

        init {
            // Define click listener for the ViewHolder's View.
            productTextView = view.findViewById(R.id.productNameListView)
            brandTextView = view.findViewById(R.id.brandNameListView)
            productImage = view.findViewById(R.id.imageProductView)
            deleteBelovedButton = view.findViewById(R.id.deleteBelovedButton)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BelovedProductsAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.beloved_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataSet.get(position)

        holder.productTextView.text = item.name
        holder.brandTextView.text = item.brand
        Glide.with(context).load(item.imageLink).into(holder.productImage);

        holder.deleteBelovedButton.setOnClickListener{
            if (context is BelovedProductsActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }
        holder.itemView.setOnClickListener {
            if (context is BelovedProductsActivity) {
                context.goToDetails(item.apiID, item.brand, item.type, item.name)
            }
        }
    }

    override fun getItemCount() = dataSet.size

}