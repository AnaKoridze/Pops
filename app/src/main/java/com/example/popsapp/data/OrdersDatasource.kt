package com.example.popsapp.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.example.popsapp.api.PopsService
import com.example.popsapp.data.model.orders.Order
import com.example.popsapp.data.model.orders.OrdersResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Gets the data from API
 */
class OrdersDatasource(
    private val sessionToken: String,
    private val queryMap: HashMap<String, String>
) : ItemKeyedDataSource<String, Order>() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    val loadState = MutableLiveData<Result<List<Order>>>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Order>) {
        loadState.postValue(Result.Progress(true))

        compositeDisposable.add(
            PopsService.getService().fetchOrders(sessionToken, queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<OrdersResponse>() {
                    override fun onNext(orders: OrdersResponse) {
                        loadState.value = Result.Success(orders.data)

                        callback.onResult(orders.data)
                    }

                    override fun onError(e: Throwable) {
                        loadState.value = Result.Error(e)
                    }

                    override fun onComplete() {

                    }
                })
        )
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Order>) {
        // do nothing
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Order>) {
        queryMap["offsetOrderId"] = params.key

        compositeDisposable.add(
            PopsService.getService().fetchOrders(sessionToken, queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<OrdersResponse>() {
                    override fun onNext(orders: OrdersResponse) {
                        callback.onResult(orders.data)
                    }

                    override fun onError(e: Throwable) {
                        loadState.value = Result.Error(e)
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    override fun getKey(item: Order): String {
        return item.orderId
    }

}



