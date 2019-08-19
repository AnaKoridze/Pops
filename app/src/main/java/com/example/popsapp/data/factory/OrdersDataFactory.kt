package com.example.popsapp.data.factory


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.popsapp.data.OrdersDatasource
import com.example.popsapp.data.model.orders.Order

/**
 * Creates the DataSource
 */
class OrdersDataFactory(val ordersDatasource: OrdersDatasource) : DataSource.Factory<String, Order>() {

    var datasourceLiveData = MutableLiveData<OrdersDatasource>()

    override fun create(): DataSource<String, Order> {
        datasourceLiveData.postValue(ordersDatasource)
        return ordersDatasource
    }


}
