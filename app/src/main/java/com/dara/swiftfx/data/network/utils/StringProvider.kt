package com.dara.swiftfx.data.network.utils


import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

/**
 * StringProvider can be injected into classes anywhere in the application where we
 * need a string and there isn't a context to get it from.
 */
interface StringProvider {
    fun getString(@StringRes resId: Int): String
}

class StringProviderImpl @Inject constructor(private val context: Context) : StringProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

}
