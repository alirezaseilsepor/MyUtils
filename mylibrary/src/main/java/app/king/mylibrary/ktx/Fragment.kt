@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import app.king.mylibrary.ktx.hideKeyboard


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
fun Fragment.screenOn(){
   activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}
fun Fragment.changeScreenOrientation(landscape:Boolean){
    if (landscape)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    else
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}
fun Fragment.hideStatusBar() {
    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}

fun Fragment.showStatusBar() {
    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}
fun Fragment.sensorScreenOrientation(){
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}
fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    context?.let { Toast.makeText(it, text, duration).show() }

fun Fragment.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    context?.let { Toast.makeText(it, textId, duration).show() }


fun Fragment.shareText(@StringRes text:Int){
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        resources.getString(text)
    )
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}

fun Fragment.shareText(text:String){
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        text
    )
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}