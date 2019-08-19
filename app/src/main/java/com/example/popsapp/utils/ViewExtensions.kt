package com.example.popsapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Encapsulate all utils relative to OS Views
 */
fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}