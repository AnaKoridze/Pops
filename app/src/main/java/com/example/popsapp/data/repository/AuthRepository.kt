package com.example.popsapp.data.repository

import com.example.popsapp.api.PopsService
import com.example.popsapp.data.model.LoginRequestBody
import com.example.popsapp.data.model.LoginResponse
import io.reactivex.Observable
import okhttp3.ResponseBody


class AuthRepository(private val popsService: PopsService) {


    fun login(username: String, password: String): Observable<LoginResponse> {
        return popsService.login(LoginRequestBody(username, password))
    }

    fun logout(sessionToken: String): Observable<ResponseBody> {
        return popsService.logout(sessionToken)
    }
}