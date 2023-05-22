package app.king.mylibrary.ktx

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.util.ArrayList

inline fun <reified T : Parcelable> Intent.getParcelableCompact(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.getParcelableCompact(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompact(key: String): ArrayList<T>? =
    when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

inline fun <reified T : Parcelable> Intent.getParcelableArrayListCompact(key: String): ArrayList<T>? =
    when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }