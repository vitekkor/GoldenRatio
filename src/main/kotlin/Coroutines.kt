import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Coroutines(maxOrMin: Boolean, a: Double, b: Double, e: Double, scope: CoroutineScope, private val f: Func) {
    private val PHI = (1 + sqrt(5.0)) / 2
    private val counters = mutableListOf<Deferred<Pair<Double, Double>>>()

    init {
        val step = (b - a) / Runtime.getRuntime().availableProcessors().toDouble()
        var start = a
        while (start + step < b) {
            counters.add(scope.async(start = CoroutineStart.LAZY) { findMax(start, start + step, e) })
            start += step
        }
        counters.add(scope.async(start = CoroutineStart.LAZY) { findMax(start, b, e) })
    }

    suspend fun find(): Pair<Double, Double> {
        counters.forEach { it.start(); yield() }
        return counters.map { it.await() }.maxByOrNull { it.second }!!
    }

    fun findMin(a: Double, b: Double, e: Double): Pair<Double, Double> {
        var a = a
        var b = b
        var x1: Double
        var x2: Double

        while (abs(b - a) > e) {
            x1 = b - (b - a) / PHI
            x2 = a + (b - a) / PHI
            val f1 = f(x1)
            val f2 = f(x2)
            if (f1 >= f2) a = x1 else b = x2
        }
        return (a + b) / 2 to f((a + b) / 2)
    }

    private fun findMax(a: Double, b: Double, e: Double): Pair<Double, Double> {
        var a = a
        var b = b
        var x1: Double
        var x2: Double
        while (abs(b - a) > e) {
            x1 = b - (b - a) / PHI
            x2 = a + (b - a) / PHI
            val f1 = f(x1)
            val f2 = f(x2)
            if (f1 <= f2) a = x1 else b = x2
        }
        return (a + b) / 2 to f((a + b) / 2)
    }
}