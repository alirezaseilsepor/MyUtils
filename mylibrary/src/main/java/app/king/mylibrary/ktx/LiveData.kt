package app.king.mylibrary.ktx

import androidx.lifecycle.*
import app.king.mylibrary.util.Event

fun <T, R> LiveData<T>.map(action: (t: T) -> R): LiveData<R> =
    Transformations.map(this, action)

internal fun <T : Any?> MutableLiveData<T>.setValueIfNotEqual(arg: T) {
    @Suppress("SuspiciousEqualsCombination")
    fun objectsEquals(a: T?, b: T?): Boolean {
        return (a === b) || (a != null && a == b)
    }

    val value = value
    if (!objectsEquals(value, arg)) {
        this.value = arg
    }
}

fun <T1, T2> combineLiveData(f1: LiveData<T1>, f2: LiveData<T2>): LiveData<Pair<T1?, T2?>> =
    MediatorLiveData<Pair<T1?, T2?>>().also { mediator ->
        mediator.setValueIfNotEqual(Pair(f1.value, f2.value))

        mediator.addSource(f1) { t1: T1? ->
            val (_, t2) = mediator.value!!
            mediator.setValueIfNotEqual(Pair(t1, t2))
        }

        mediator.addSource(f2) { t2: T2? ->
            val (t1, _) = mediator.value!!
            mediator.setValueIfNotEqual(Pair(t1, t2))
        }
    }
