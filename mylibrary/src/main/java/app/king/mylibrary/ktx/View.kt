@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.constraintlayout.widget.Group
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.king.mylibrary.R
import app.king.mylibrary.util.OnSafeClickListener
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.lang.reflect.Method


fun View.getString(stringResId: Int): String = resources.getString(stringResId)

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}

fun View.animRotate(
    value: Float = 360.0f,
    duration: Long = 500,
    repeatCount: Int = 5,
    interpolator: Interpolator = AccelerateInterpolator(),
) {
    val rotate =
        RotateAnimation(
            0f,
            value,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    rotate.duration = duration
    rotate.repeatCount = repeatCount
    rotate.interpolator = interpolator
    startAnimation(rotate)

}

fun ImageView.setTintResource(@ColorRes color: Int) {
    setColorFilter(context.getColorCompat(color), android.graphics.PorterDuff.Mode.SRC_IN)

}


fun ImageView.setTintColor(color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

fun ImageView.setTintColorAttr(attr: Int, alpha: Int = 255) {
    setColorFilter(context.getColorCompatAttr(attr, alpha), android.graphics.PorterDuff.Mode.SRC_IN)
}

fun MaterialRadioButton.setTintColorAttr(attr: Int, alpha: Int = 255) {
    buttonTintList = ColorStateList.valueOf(context.getColorCompatAttr(attr, alpha))
}

fun ProgressBar.setProgressIng(position: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setProgress(position, true)
    } else {
        progress = position
    }
}

fun View.visible(animate: Boolean = false) {
    if (animate) {
        animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else {
        visibility = View.VISIBLE
    }
}

fun View.invisible(animate: Boolean = false) {
    hide(View.INVISIBLE, animate)
}

fun View.gone(animate: Boolean = false) {
    hide(View.GONE, animate)
}

fun View.visibleOrInvisible(show: Boolean, animate: Boolean = false) {
    if (show) visible(animate) else invisible(animate)
}

fun View.visibleOrGone(show: Boolean, animate: Boolean = false) {
    if (show) visible(animate) else gone(animate)
}

private fun View.hide(hidingStrategy: Int, animate: Boolean = false) {
    if (animate) {
        animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy
            }
        })
    } else {
        visibility = hidingStrategy
    }
}


fun View.lock() {
    isEnabled = false
    isClickable = false
}

fun View.unlock() {
    isEnabled = true
    isClickable = true
}

fun View.lockOrUnlock(isLock: Boolean) {
    if (isLock) lock() else unlock()
}


fun View.slideDown() {
    val animate = TranslateAnimation(0f, 0f, 0f, height.toFloat())
    animate.duration = 200
    animate.fillAfter = false
    startAnimation(animate)
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            gone(false)
        }

        override fun onAnimationStart(animation: Animation?) {

        }
    })

}

fun View.slideUp() {
    visible(false)
    val animate = TranslateAnimation(0f, 0f, height.toFloat(), 0f)
    animate.duration = 300
    animate.fillAfter = true
    startAnimation(animate)
}


fun Snackbar.hideSnackBar() {
    if (isShown)
        dismiss()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun Snackbar.setBackGroundColor(@ColorRes color: Int) {
    view.setBackgroundColor(ContextCompat.getColor(context, color))
}


fun View.setOnSafeClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSafeClickListener(l))
}

fun View.setOnSafeClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSafeClickListener(l))
}

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
            ForegroundColorSpan(context.getColorCompat(color)),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        Log.d(
            "ViewExtensions",
            "exception in setColorOfSubstring, text=$text, substring=$substring",
            e
        )
    }
}

fun View.fadeIn(durationMillis: Long = 250) {
    this.startAnimation(AlphaAnimation(0F, 1F).also {
        it.duration = durationMillis
        it.fillAfter = true
    })
}


inline fun View.setSafeLayoutChangeListener(
    isSafe: Boolean = true,
    crossinline onLayout: (View) -> Unit,
) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int,
        ) {
            onLayout(view)
            if (isSafe) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}

fun ViewGroup.screenShot(): Bitmap? {
    /*val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    draw(c)
    return b
    */
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bgDrawable = background
    if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
    draw(canvas)
    return bitmap
}


inline fun TabLayout.setOnTabSelectListener(crossinline onLayout: (TabLayout.Tab) -> Unit) {
    clearOnTabSelectedListeners()
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            onLayout.invoke(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // Handle tab reselect
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // Handle tab unselect
        }
    })
}

fun View.setOnBackGroundColorAttr(@AttrRes attr: Int, alpha: Int = 255) {
    setBackgroundColor(
        context.getColorCompatAttr(attr, alpha)
    )
}

fun TextView.setTextColorAttr(@AttrRes attr: Int, alpha: Int = 255) {
    setTextColor(
        context.getColorCompatAttr(attr, alpha)
    )
}

fun TextView.startCountAnimation(
    valueForm: Int = 100,
    valueTo: Int = 0,
    duration: Long = 1500,
    action: () -> Unit,
) {
    val animator = ValueAnimator.ofInt(valueForm, valueTo)
    animator.duration = duration
    animator.addUpdateListener { animation ->
        text = animation.animatedValue.toString()
    }
    animator.start()
    animator.addListener({
        action()

    })

}


fun View.setShapeColorFilterAttr(@AttrRes attr: Int, alpha: Int = 255) {
    val color = context.getColorCompatAttr(attr, alpha)
    val drawable = DrawableCompat.wrap(background)
    DrawableCompat.setTint(drawable, color)
    background = drawable
}

fun View.setShapeColorFilter(color: Int) {
    val drawable = DrawableCompat.wrap(background)
    DrawableCompat.setTint(drawable, color)
    background = drawable
}

fun MaterialCardView.setCardBackgroundColorAttr(@AttrRes attr: Int, alpha: Int = 255) {
    setCardBackgroundColor(context.getColorCompatAttr(attr, alpha))
}

fun View.springAnimationTrait() {
    val scaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 0.60f)
    val scaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 0.60f)
    scaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
    scaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
    scaleXAnim.start()
    scaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
    scaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
    scaleYAnim.start()
    handler.postDelayed({
        scaleXAnim.cancel()
        scaleYAnim.cancel()
        val reverseScaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 1f)
        reverseScaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
        reverseScaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        reverseScaleXAnim.start()

        val reverseScaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 1f)
        reverseScaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
        reverseScaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        reverseScaleYAnim.start()
    }, 100)

}

@SuppressLint("ClickableViewAccessibility")
fun View.implementSpringAnimationTrait() {
    val scaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 0.90f)
    val scaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 0.90f)

    setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleXAnim.start()

                scaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleYAnim.start()

            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
            -> {
                scaleXAnim.cancel()
                scaleYAnim.cancel()
                val reverseScaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 1f)
                reverseScaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleXAnim.start()

                val reverseScaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 1f)
                reverseScaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleYAnim.start()


            }
        }

        false
    }
}

fun Group.setAllOnClickListener(l: (View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(OnSafeClickListener(l))
    }
}


fun RecyclerView.smoothSnapToPosition(
    position: Int,
    snapMode: Int = LinearSmoothScroller.SNAP_TO_START,
) {
    if (position < 0)
        return
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun MaterialButton.strokeColorCompat(@AttrRes attr: Int, alpha: Int = 255) {
    strokeColor = ColorStateList.valueOf(context.getColorCompatAttr(attr, alpha))
}


fun ImageView.loadCircle(
    data: Any?,
    @DrawableRes placeholder: Int = R.drawable.shape_circle_white,
) {
    load(data) {
        error(placeholder)
        placeholder(placeholder)
        transformations(CircleCropTransformation())
    }
}

fun ImageView.loadRoundedCorners(data: Any?, radius: Float = 10f) {
    load(data) {
        crossfade(true)
        transformations(RoundedCornersTransformation(radius))
    }
}

fun View.delayOnLifecycle(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit,
): Job? {

    return findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
        lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
            delay(duration)
            block()
        }
    }
}

fun Spinner.close() {
    val method: Method =
        Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
    method.isAccessible = true
    method.invoke(this)
}

fun ViewPager2.reduceDragSensitivity(f: Int = 4) {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * f)       // "8" was obtained experimentally
}

fun RecyclerView.setSafeScrollListener(listener: RecyclerView.OnScrollListener) {
    clearOnScrollListeners()
    addOnScrollListener(listener)
}


fun TextView.setAppearance(res: Int) {
    if (Build.VERSION.SDK_INT < 23) {
        @Suppress("DEPRECATION")
        setTextAppearance(this.context, res)
    } else {
        setTextAppearance(res)
    }
}

fun TextView.setDrawableCompat(
    @Nullable left: Drawable? = null,
    @Nullable top: Drawable? = null,
    @Nullable right: Drawable? = null,
    @Nullable bottom: Drawable? = null,
) {
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

fun TextView.setDrawableCompat(
    @DrawableRes left: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes bottom: Int = 0,
) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
}

fun RadioButton.setTintColorAttr(attr: Int, alpha: Int = 255) {
    buttonTintList = ColorStateList.valueOf(context.getColorCompatAttr(attr, alpha))
}