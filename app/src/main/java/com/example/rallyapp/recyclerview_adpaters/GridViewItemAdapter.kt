package com.example.rallyapp.recyclerview_adpaters


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rallyapp.R
import com.example.rallyapp.activities.PlateDetailActivity
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.squareup.picasso.Picasso

class GridViewItemAdapter(
    private val context: Context,
    private var menuItems: List<Menu>,
) : RecyclerView.Adapter<GridViewItemAdapter.ViewHolder>() {
    companion object{
        private const val TAG = "GridItemViewAdapter"
    }

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
                Log.i(TAG, menuItems[position].id.toString())
            }
            plateDetailActivityIntent.putExtras(bundle)
            context.startActivity(plateDetailActivityIntent)
        }
    }

    fun setMenuItems(items: List<Menu>){
        menuItems = items
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }


}