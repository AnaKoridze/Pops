package com.example.popsapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.popsapp.data.Result
import com.example.popsapp.data.model.LoginResponse
import com.example.popsapp.data.repository.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val loginRepository: AuthRepository) : ViewModel() {

    val loginResult = MutableLiveData<Result<LoginResponse>>()

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun login(username: String, password: String) {
        loginResult.value = Result.Progress(true)

        compositeDisposable.add(
            loginRepository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<LoginResponse>() {
                    override fun onNext(result: LoginResponse) {
                        loginResult.value = Result.Success(result)
                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        loginResult.value = Result.Error(e)
                    }
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
