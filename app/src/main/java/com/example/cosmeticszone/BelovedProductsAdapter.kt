package com.example.cosmeticszone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BelovedProductsAdapter(val context: Context, val dataSet: ArrayList<ProductDetails>)  : RecyclerView.Adapter<BelovedProductsAdapter.ViewHolder>() {

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


//        holder.ivDelete.setOnClickListener { view ->
//
//            if (context is MainActivity) {
//                context.deleteRecordAlertDialog(item)
//            }
//        }


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