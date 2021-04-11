package com.example.cosmeticszone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductListAdapter(var dataSet: Array<Triple<String, String, String>>, val context: Context)  : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productTextView: TextView
        var brandTextView: TextView
        val productImage: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            productTextView = view.findViewById(R.id.productNameView)
            brandTextView = view.findViewById(R.id.brandNameView)
            productImage = view.findViewById(R.id.imageProductView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.custom_grid_product_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ProductListAdapter.ViewHolder, position: Int) {

        // Get element from yo ur dataset at this position and replace the
        // contents of the view with that element
        val product = dataSet[position]

        viewHolder.productTextView.text = product.first
        viewHolder.brandTextView.text = product.second
        viewHolder.productImage.setImageResource(android.R.drawable.ic_menu_crop)

        viewHolder.itemView.setOnClickListener { goToDetails(10) }
    }

    private fun goToDetails(productID: Int) {
        val intent = Intent(context, ProductListActivity::class.java).apply {
            putExtra("productInfo", productID)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}