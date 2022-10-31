package app.king.mylibrary.util

import androidx.lifecycle.Observer
import app.king.mylibrary.util.Event


/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}


class EventObserverWithNull<T>(private val onEventUnhandledContent: (T?) -> Unit) : Observer<Event<T?>> {
    override fun onChanged(event: Event<T?>?) {
            onEventUnhandledContent(event?.getContentIfNotHandled())
        }
}