package com.example.popsapp.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.popsapp.api.PopsService
import com.example.popsapp.config.PopsApplication
import com.example.popsapp.data.repository.AuthRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class OrdersViewModelFactory(private val application: PopsApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(
                application = application,
                authRepository = AuthRepository(PopsService.getService())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
