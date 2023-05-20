package com.android.profileapplication.utility

import android.content.Context

class ResourceHelper(private val context: Context) {

    fun getString(resId: Int) = context.resources.getString(resId)

    fun getInt(resId: Int) = context.resources.getInteger(resId)
}