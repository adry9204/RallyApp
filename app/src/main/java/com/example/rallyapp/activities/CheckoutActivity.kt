package com.example.rallyapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.OrderDetail
import com.example.rallyapp.databinding.ActivityCheckoutBinding
import com.example.rallyapp.recyclerview_adpaters.AddressListAdapter
import com.example.rallyapp.recyclerview_adpaters.OrderDetailsListAdapter


class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var orderItemsAdapter: OrderDetailsListAdapter
    private lateinit var addressListAdapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOrderItemsRecyclerView()
        setFakeDataOnOrderItemsList()

        setAddressListRecyclerView()
        setFakeDataOnAddressItemsList()
    }

    private fun setAddressListRecyclerView(){
        binding.orderActivityAddressRecyclerView.layoutManager = LinearLayoutManager(this)
        addressListAdapter = AddressListAdapter(this, mutableListOf())
        binding.orderActivityAddressRecyclerView.adapter = addressListAdapter

    }

    private fun setFakeDataOnAddressItemsList(){
        val data = mutableListOf<Address<Int>>()
        val address = Address(
            id = 1,
            name = "Abraham Alfred Babu",
            line1 = "48 Scarboro Ave",
            line2 = "Basement",
            country = "Canada",
            province = "Ontario",
            postalCode = "M1C 1M3",
            userId = 1
        )
        for (i in 0..2){
            data.add(address)
        }
        addressListAdapter.setData(data.toList())
    }

    private fun setOrderItemsRecyclerView(){
        binding.orderActivityOrderItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        orderItemsAdapter = OrderDetailsListAdapter(listOf())
        binding.orderActivityOrderItemsRecyclerView.adapter = orderItemsAdapter
    }

    private fun setFakeDataOnOrderItemsList(){
        val data = mutableListOf<OrderDetail>()
        val order = OrderDetail(
            id = 1,
            menu = Menu(
                id = 1,
                name = "Name of item in this",
                description = "description",
                image = "https://images.ctfassets.net/odk340ad2lwh/ZAY43Mmno5pEM3UE77pBK/cab80a7cbbe081dd7507a7fb2a89d5af/WFC_2543_NB_ValueAdd_PizzaDominoAffect_Lifestyle_1x1.jpg",
                price = "20"
            ),
            price = "30",
            quantity = 3
        )
        for (i in 0..3){
            data.add(order)
        }
        orderItemsAdapter.setData(data.toList())
    }
}