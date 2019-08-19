package com.example.popsapp.ui.orders

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.popsapp.config.PopsApplication
import com.example.popsapp.data.OrdersDatasource
import com.example.popsapp.data.Result
import com.example.popsapp.data.factory.OrdersDataFactory
import com.example.popsapp.data.model.OrderStatus
import com.example.popsapp.data.model.orders.Order
import com.example.popsapp.data.repository.AuthRepository
import com.example.popsapp.utils.SharedPrefHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.concurrent.Executors

const val LIMIT = 25

class OrdersViewModel(
    private val application: PopsApplication,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    private var myPagingConfig: PagedList.Config? = null

    var orderList: LiveData<PagedList<Order>>? = null

    private var dataSourceFactory: OrdersDataFactory? = null

    fun loadState(): MutableLiveData<Result<List<Order>>>? {
        return dataSourceFactory?.ordersDatasource?.loadState
    }

    fun getOrders(orderStatus: OrderStatus, scanIndexForward: Boolean? = null) {
        val token = SharedPrefHelper.getSessionToken(application.applicationContext)
        if (token == null) {
            // todo error
            return
        }

        val params = hashMapOf<String, String>()
        params["orderStatus"] = orderStatus.status
        params["limit"] = LIMIT.toString()
        scanIndexForward?.let {
            params["scanIndexForward"] = if (it) "1" else "0"
        }

        dataSourceFactory = OrdersDataFactory(OrdersDatasource(token, params))

        if (myPagingConfig == null) {
            myPagingConfig = PagedList.Config.Builder()
                .setPageSize(LIMIT)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()
        }

        orderList = LivePagedListBuilder(dataSourceFactory!!, myPagingConfig!!)
            .setInitialLoadKey(null)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()
    }

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    val logoutResponse = MutableLiveData<Result<ResponseBody>>()

    fun logout() {
        compositeDisposable.add(authRepository.logout(SharedPrefHelper.getSessionToken(application.applicationContext)!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<ResponseBody>() {
                override fun onNext(result: ResponseBody) {
                    SharedPrefHelper.setSessionToken(application.applicationContext, null)
                    logoutResponse.value = Result.Success(result)
                }

                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    logoutResponse.value = Result.Error(e)
                }
            })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}