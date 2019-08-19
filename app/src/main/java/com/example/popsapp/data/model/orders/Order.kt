package com.example.popsapp.data.model.orders

data class Order(
    //val address: Address,
    //val coupon: Coupon,
    //val couponDiscount: Int,
    val creditUsed: Float,
    val currency: String,
    //val distinctId: String,
    val email: String,
    //val metaData: MetaData,
    val orderId: String,
    val orderStatus: String,
    val paidTime: Int,
    val priceFinal: Double,
    //val priceProducts: Double,
    //val priceShipping: Double,
    //val selections: List<Selection>,
    //val shortId: String,
    //val timestamps: Timestamps,
    val userId: String
)