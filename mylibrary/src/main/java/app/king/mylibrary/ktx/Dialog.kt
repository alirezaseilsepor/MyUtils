package app.king.mylibrary.ktx

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.safeDismiss() {
    if (isVisible || isAdded || isResumed)
        dismiss()
}

fun DialogFragment.safeShow(manager: FragmentManager, tag: String? = null) {
    if (!isVisible && !isAdded && !isResumed)
        show(manager, tag)
}