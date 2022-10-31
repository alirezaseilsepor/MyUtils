package app.king.mylibrary.ktx

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


object MoshiExtensions {
    var moshi: Moshi = Moshi.Builder()
        .build()

    fun init(moshi: Moshi) {
        this.moshi = moshi
    }
}

inline fun <reified T> String.deserialize(): T? {
    if (!canConvertTo(T::class.java))
        return null
    val jsonAdapter = MoshiExtensions.moshi.adapter(T::class.java)
    return jsonAdapter.fromJson(this)
}

inline fun <reified T> String.deserializeList(): List<T>? {
    runCatching {
        val type = Types.newParameterizedType(MutableList::class.java, T::class.java)
        val jsonAdapter: JsonAdapter<List<T>> = MoshiExtensions.moshi.adapter(type)
        return jsonAdapter.fromJson(this)
    }
    return null
}

@Suppress("CheckResult")
fun String.canConvertTo(type: Class<*>): Boolean {
    return try {
        val jsonAdapter = MoshiExtensions.moshi.adapter(type)
        jsonAdapter.fromJson(this)
        true
    } catch (exception: Exception) {
        exception.printStackTrace()
        false
    }
}

inline fun <reified T> T.serialize(): String {
    val jsonAdapter = MoshiExtensions.moshi.adapter(T::class.java)
    return jsonAdapter.toJson(this)
}

inline fun <reified T> List<T>.serializeList(): String {
    val type = Types.newParameterizedType(MutableList::class.java, T::class.java)
    val jsonAdapter: JsonAdapter<List<T>> = MoshiExtensions.moshi.adapter(type)
    return jsonAdapter.toJson(this)
}


fun SharedPreferences.Editor.putObject(key: String, obj: Any?): SharedPreferences.Editor {
    putString(key, obj?.serialize())
    return this
}

fun SharedPreferences.Editor.putObjectList(key: String, obj: List<Any>?): SharedPreferences.Editor {
    putString(key, obj?.serializeList())
    return this
}

inline fun <reified T : Any> SharedPreferences.getObject(
    key: String,
): T? {
    val string = getString(key, null)
    if (string.isNullOrEmpty())
        return null
    return string.deserialize()
}

inline fun <reified T : Any> SharedPreferences.getObjectList(
    key: String,
): List<T>? {
    val string = getString(key, null)
    if (string.isNullOrEmpty())
        return null
    return string.deserializeList()
}
