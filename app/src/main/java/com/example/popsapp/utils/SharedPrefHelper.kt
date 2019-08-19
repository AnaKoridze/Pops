package com.example.popsapp.utils

import android.content.Context
import android.content.SharedPreferences


const val TOKEN = "token"

object SharedPrefHelper {

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("Preferencess", Context.MODE_PRIVATE)
    }

    fun getSessionToken(context: Context): String? {
        return getSharedPreferences(context).getString(TOKEN, null)
    }


    fun setSessionToken(context: Context, token: String?) {
        getSharedPreferences(context).edit().putString(TOKEN, token).apply()
    }


}