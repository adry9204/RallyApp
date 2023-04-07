package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.databinding.AdrressListItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddressListAdapter(
    private val context: Context,
    private var addressData: MutableList<Address<Int>>
) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    companion object {
        const val TAG = "AddressListAdapter"
    }

    inner class ViewHolder(val binding: AdrressListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION){
                    val position = adapterPosition
                    selectedIndex = position
                    notifyItemChanged(selectedIndex!!)

                    lastAddressSelectedIndex?.let { it1 ->
                        Log.i(TAG, it1.toString())
                        notifyItemChanged(it1)
                    }

                    lastAddressSelectedIndex = position
                    selectedAddressId = addressData[position].id
                }
            }

            binding.addressListAddressExpandButton.setOnClickListener{
                val position = adapterPosition
                addressData[position].expanded = !addressData[position].expanded
                notifyItemChanged(position)
            }
        }
    }


    var selectedAddressId: Int? = null
    var lastAddressSelectedIndex: Int? = null
    var selectedIndex: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdrressListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.addressListAddressLineOne.text = addressData[position].line1
        holder.binding.addressListAddressName.text = addressData[position].name
        addressData[position].line2?.let {
            holder.binding.addressListAddressLineTwo.text = it
        }
        holder.binding.addressListAddressExpandableName.text = addressData[position].name
        holder.binding.addressListAddressProvinceCountry.text =
            "${addressData[position].province}, ${addressData[position].country}"
        holder.binding.addressListAddressPostalCode.text = addressData[position].postalCode

        Log.i(TAG, "in position $position, ${addressData[position].selected}")
        holder.binding.addressListAddressSelectRadioButton.isChecked = false

        selectedIndex?.let {
            if(position == it) {
                holder.binding.addressListAddressSelectRadioButton.isChecked = true
            }
        }

        if (addressData[position].expanded) {
            expandAddressDetails(holder.binding)
        } else {
            contractAddressDetails(holder.binding)
        }

    }

    fun setData(newData: List<Address<Int>>) {
        addressData = newData.toMutableList()
        notifyDataSetChanged()
    }

    private fun expandAddressDetails(binding: AdrressListItemBinding) {
        binding.addressListAddressName.visibility = View.INVISIBLE
        binding.addressListAddressExpandable.visibility = View.VISIBLE
    }

    private fun contractAddressDetails(binding: AdrressListItemBinding) {
        binding.addressListAddressName.visibility = View.VISIBLE
        binding.addressListAddressExpandable.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return addressData.size
    }
}