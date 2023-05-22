package app.king.mylibrary.ktx

import java.util.ArrayList

fun <T> Collection<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}