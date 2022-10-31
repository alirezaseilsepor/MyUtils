package app.king.mylibrary.ktx

typealias Click<T> = (T) -> Unit
typealias Click2<T,E> = (T,E) -> Unit
typealias Click3<T,E,F> = (T,E,F) -> Unit
typealias Click4<T,E,F,D> = (T,E,F,D) -> Unit
typealias SimpleClick = () -> Unit