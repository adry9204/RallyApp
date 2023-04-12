package com.example.rallyapp.utils

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.rallyapp.R


class OrderMethodSelectorBuilder(
    private val context: Context,
    private var selector: LinearLayout? = null,
    private var deliveryTextView: TextView? = null,
    private var pickUpTextView: TextView? = null
) {

    fun build(): OrderMethodSelector{
        val orderMethodSelector = OrderMethodSelector(
            selector = selector!!,
            deliveryTextView = deliveryTextView!!,
            pickUpTextView = pickUpTextView!!,
            context = context
        ).apply {
            init()
            setListeners()
        }
        return orderMethodSelector
    }

}

class OrderMethodSelector(
    private val selector: LinearLayout,
    private val deliveryTextView: TextView,
    private val pickUpTextView:TextView,
    private val context: Context
) {

    companion object {
        const val DELIVERY = "delivery"
        const val PICK_UP = "pickup"
    }

    private var selected = DELIVERY
    private var onTapMethod: ((String) -> Unit)? = null

    fun init(){
       setSelected()
    }

    fun setListeners(){
        selector.setOnClickListener{
            selected = if(selected == PICK_UP){
                DELIVERY
            }else{
                PICK_UP
            }
            setSelected()
            onTapMethod?.let {
                it(selected)
            }
        }
    }

    private fun setSelected(){
        if(selected == DELIVERY){
            setViewSelected(deliveryTextView)
            setViewDeSelected(pickUpTextView)
        }else{
            setViewSelected(pickUpTextView)
            setViewDeSelected(deliveryTextView)
        }
    }

   private fun setViewSelected(tv: TextView){
        tv.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun setViewDeSelected(tv: TextView){
        tv.backgroundTintList = ContextCompat.getColorStateList(context, R.color.minimal_light_grey)
        tv.setTextColor(ContextCompat.getColor(context, R.color.black))
    }

    fun setOnTapListener(method: (String) -> Unit){
        onTapMethod = method
    }

    fun getSelected(): String{ return selected }
}