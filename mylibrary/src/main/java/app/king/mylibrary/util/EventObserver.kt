package app.king.mylibrary.util

import androidx.lifecycle.Observer


/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getContentIfNotHandled()?.let { value1 ->
            onEventUnhandledContent(value1)
        }
    }
}


class EventObserverWithNull<T>(private val onEventUnhandledContent: (T?) -> Unit) :
    Observer<Event<T?>> {
    override fun onChanged(value: Event<T?>) {
        onEventUnhandledContent(value.getContentIfNotHandled())
    }
}