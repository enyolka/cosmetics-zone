package com.example.cosmeticszone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductTypeAdapter(var dataSet: Array<Pair<String,Int>>, val context: Context) : RecyclerView.Adapter<ProductTypeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productTypeTextView: TextView
        val productTypeImage: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            productTypeTextView = view.findViewById(R.id.productNameListView)
            productTypeImage = view.findViewById(R.id.imageProductView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductTypeAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.custom_grid_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ProductTypeAdapter.ViewHolder, position: Int) {

        // Get element from yo ur dataset at this position and replace the
        // contents of the view with that element
        val product = dataSet[position]

        viewHolder.productTypeTextView.text = product.first
        viewHolder.productTypeImage.setImageResource(product.second)

        viewHolder.itemView.setOnClickListener { goToDetails(product.first) }
    }

    private fun goToDetails(productType: String) {
        val intent = Intent(context, ProductListActivity::class.java).apply {
            putExtra("productType", productType)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}