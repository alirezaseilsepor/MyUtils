/*
 * *
 *  * Created by Alireza Seilsepor on 3/30/20 5:36 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 3/30/20 5:36 AM
 *
 */

@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.util.Patterns
import java.util.regex.Pattern



fun String.isAllEnglishChars() =
    Pattern.compile("^[a-zA-Z0-9@&-_. ]+\$")
        .matcher(this)
        .matches()


fun String?.isEmail(): Boolean {
    return this?.let {
        return Patterns.EMAIL_ADDRESS.matcher(it).matches()
    } ?: false
}


fun String?.isPhone(): Boolean {
    return this?.let {
        return it.matches(Regex("(\\+98|0)?9\\d{9}"))

    } ?: false
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return this != null
}


//https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
fun String.isUserName(): Boolean {
    /*return  Pattern.compile("^([a-zA-Z])+([\\w]{2,})+$")
        .matcher(this)
        .matches() */

    return  Pattern.compile("^(?=.{5,25}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
        .matcher(this)
        .matches()
}
