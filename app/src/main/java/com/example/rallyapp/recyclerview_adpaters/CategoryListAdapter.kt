package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.viewModel.HomeActivityViewModel

class CategoryListAdapter(
    private val context: Context,
    private val viewModel: HomeActivityViewModel,
    private var categories: MutableList<Category>,
): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    var lastSelectedIndex: Int? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryLabel: TextView = itemView.findViewById(R.id.category_list_item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.categoryLabel.text = categories[position].category
        if(categories[position].selected){
            val greenColor = context.resources.getColor(R.color.green, context.theme)
            val whiteColor = context.resources.getColor(R.color.white, context.theme)
            holder.categoryLabel.setBackgroundColor(greenColor)
            holder.categoryLabel.setTextColor(whiteColor)
        }else{
            val blackColor = context.resources.getColor(R.color.black, context.theme)
            val whiteColor = context.resources.getColor(R.color.white, context.theme)
            holder.categoryLabel.setBackgroundColor(whiteColor)
            holder.categoryLabel.setTextColor(blackColor)
        }

        holder.itemView.setOnClickListener{
            viewModel.getMenuByCategory(categories[position].id)
            categories[position].selected = true
            notifyItemChanged(position)

            lastSelectedIndex?.let {
                Log.i("CartRecc", it.toString())
                categories[it].selected = false
                notifyItemChanged(it)
            }

            lastSelectedIndex = holder.adapterPosition
        }
    }

    fun setCategoryItems(newCategories: List<Category>){
        categories = newCategories.toMutableList()
    }

    override fun getItemCount(): Int {
        return categories.size
    }


}