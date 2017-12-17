package pl.srw.billcalculator.util

import timber.log.Timber
import kotlin.system.measureNanoTime

/**
 * Puts measured block execution in debug log
 */
@Suppress("MagicNumber")
inline fun debugMeasure(what: String, block: () -> Unit) {
    val time = measureNanoTime(block).toDouble() / 1_000_000
    Timber.d("$what took: %.2f ms", time)
}
