package com.example.rallyapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rallyapp.dataModel.Menu
import com.squareup.picasso.Picasso

class GridViewItemAdapter(
    private val context: Context,
    private val menuItems: List<Menu>,
) : RecyclerView.Adapter<GridViewItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.gridview_image)
        val cardTitle: TextView = itemView.findViewById(R.id.gridview_image_title)
        val cardPrice: TextView = itemView.findViewById(R.id.gridview_image_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(menuItems[position].image).into(holder.cardImage)
        holder.cardTitle.text = menuItems[position].name
        holder.cardPrice.text = "$${menuItems[position].price}"

        holder.itemView.setOnClickListener {
            val plateDetailActivityIntent = Intent(context, PlateDetailActivity::class.java)
            val bundle = Bundle().apply {
                putInt(PlateDetailActivity.MENU_ITEM_ID, menuItems[position].id)
            }
            plateDetailActivityIntent.putExtras(bundle)
            context.startActivity(plateDetailActivityIntent)
        }
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }


}