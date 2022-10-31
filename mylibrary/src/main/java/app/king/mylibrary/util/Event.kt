/*
 * *
 *  * Created by Alireza Seilsepor on 4/6/20 4:41 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/6/20 4:41 AM
 *
 */

package app.king.mylibrary.util

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}