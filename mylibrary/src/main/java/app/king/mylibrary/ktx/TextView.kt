package app.king.mylibrary.ktx

import android.content.Context
import android.os.Build
import android.widget.TextView

fun TextView.setAppearance(res: Int) {
    if (Build.VERSION.SDK_INT < 23) {
        @Suppress("DEPRECATION")
        setTextAppearance(this.context, res)
    } else {
        setTextAppearance(res)
    }
}