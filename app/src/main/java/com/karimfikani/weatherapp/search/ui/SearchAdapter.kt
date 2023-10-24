package com.karimfikani.weatherapp.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karimfikani.weatherapp.R

class SearchAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var clickListener: ItemClickListener? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val container: FrameLayout

        init {
            // Define click listener for the ViewHolder's View
            container = view.findViewById(R.id.container)
            textView = view.findViewById(R.id.address)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_response_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
        viewHolder.container.setOnClickListener {
            clickListener?.onClick(dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setOnItemClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }
}

interface ItemClickListener {

    fun onClick(text: String)
}
