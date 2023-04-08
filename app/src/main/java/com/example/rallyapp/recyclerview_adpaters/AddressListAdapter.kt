package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.databinding.AdrressListItemBinding
import com.example.rallyapp.databinding.FragmentAddAddressBottomSheetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddressListAdapter(
    private val context: Context,
    private var addressData: MutableList<Address<User>>
) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    companion object {
        const val TAG = "AddressListAdapter"
    }

    inner class ViewHolder(val binding: AdrressListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if(absoluteAdapterPosition != RecyclerView.NO_POSITION){
                    val position = absoluteAdapterPosition
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

            binding.addressListAddressSelectRadioButton.setOnClickListener {
                if(absoluteAdapterPosition != RecyclerView.NO_POSITION){
                    val position = absoluteAdapterPosition
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
                val position = absoluteAdapterPosition
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

        if(addressData[position].line2 == null) hideAddressLine2(holder.binding)
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


    private fun hideAddressLine2(binding: AdrressListItemBinding){
        binding.apply {
            addressListAddressLineTwo.visibility = View.GONE
            val params = addressListAddressExpandableName.layoutParams as ConstraintLayout.LayoutParams
            params.topToBottom = addressListAddressLineOne.id
            addressListAddressExpandableName.requestLayout()
        }
    }

    fun setData(newData: List<Address<User>>) {
        addressData = newData.toMutableList()
        notifyDataSetChanged()
    }

    private fun expandAddressDetails(binding: AdrressListItemBinding) {
        binding.addressListAddressName.visibility = View.INVISIBLE
        binding.addressListAddressExpandable.visibility = View.VISIBLE
        binding.addressListAddressExpandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
    }

    private fun contractAddressDetails(binding: AdrressListItemBinding) {
        binding.addressListAddressName.visibility = View.VISIBLE
        binding.addressListAddressExpandable.visibility = View.GONE
        binding.addressListAddressExpandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
    }

    override fun getItemCount(): Int {
        return addressData.size
    }
}