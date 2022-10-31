@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import java.util.*


fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable {
    return AppCompatResources.getDrawable(this, drawableId)!!
}


fun Context.getColorCompatStateList(@ColorRes id: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, id)
}

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

@ColorInt
fun Context.getColorCompatAttr(@AttrRes color: Int, alpha: Int = 255): Int {
    val mColorByAttr = HashMap<Int, Int>()
    return mColorByAttr.getOrPut(color) {
        try {
            val colorTemp = TypedValue()
            theme.resolveAttribute(color, colorTemp, true)
            ColorUtils.setAlphaComponent(colorTemp.data, alpha)
        } catch (e: Exception) {
            ContextCompat.getColor(this, android.R.color.white)
        }
    }
}

fun Context.getHexColorCompatAttr(@AttrRes color: Int, alpha: Int = 255) =
    String.format("#%06X", 0xFFFFFF and getColorCompatAttr(color, alpha))


fun Fragment.getDrawableCompat(@DrawableRes id: Int): Drawable {
    return AppCompatResources.getDrawable(requireContext(), id)!!
}

fun Activity.getDrawableCompat(@DrawableRes id: Int): Drawable {
    return AppCompatResources.getDrawable(this, id)!!
}

fun Fragment.getBitmapCompat(@DrawableRes id: Int): Bitmap {
    return getDrawableCompat(id).toBitmap()
}

fun Activity.getBitmapCompat(@DrawableRes id: Int): Bitmap {
    return getDrawableCompat(id).toBitmap()
}

inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels

inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context?.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, textId, duration).show() }

fun Context.call(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    startActivity(intent)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION")
fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

fun Resources.getRawUri(@RawRes rawRes: Int?): String? {
    if (rawRes.isNull())
        return null
    return "%s://%s/%s/%s".format(
        ContentResolver.SCHEME_ANDROID_RESOURCE, this.getResourcePackageName(rawRes!!),
        this.getResourceTypeName(rawRes), this.getResourceEntryName(rawRes)
    )
}

fun Context.openUrl(url: String) {
    try {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    } catch (ignored: ActivityNotFoundException) {
        toast("can not open url")
    }
}

fun Context.shareText(@StringRes text: Int) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        resources.getString(text)
    )
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}

fun Context.shareText(text: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT, text)
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}

fun Context.copyToClipboard(text: String) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
    toast("Copy")
}

fun Context.getLocalizedResources(desiredLocale: Locale): Resources {
    var conf: Configuration = resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    val localizedContext = createConfigurationContext(conf)
    return localizedContext.resources
}

fun Context.isPermissionGrantedForMediaLocationAccess(): Boolean {
    val result: Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        } else {
            return true
        }
    return result == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissionForAccessMediaLocation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION),
            578
        )
    }
}

