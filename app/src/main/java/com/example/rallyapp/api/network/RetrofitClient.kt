package com.example.rallyapp.api.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    private const val server = "http://192.168.2.237:8000"
    private const val mapsBaseUrl = "https://maps.googleapis.com/"

    val userRetrofit: UserService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

    val menuClient: MenuService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MenuService::class.java)

    val cartClient: CartService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CartService::class.java)

    val categoryClient: CategoryService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CategoryService::class.java)

    val orderClient: OrderService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OrderService::class.java)

    val addressClient: AddressService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AddressService::class.java)

    val voucherClient: VoucherService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VoucherService::class.java)

    val directionsClient: DirectionsClient = Retrofit.Builder()
        .baseUrl("$mapsBaseUrl")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DirectionsClient::class.java)
}