package pl.srw.billcalculator.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Add this [Disposable] to given [CompositeDisposable]
 */
fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}

/**
 * Creates a new instance of the [Lazy] that uses [LazyThreadSafetyMode.NONE] thread-safety mode.
 */
fun <T> lazyUnsafe(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
