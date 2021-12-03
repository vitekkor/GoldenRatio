import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.sqrt

object Coroutines {
    private val PHI = (1 + sqrt(5.0)) / 2

    suspend fun find(maxOrMin: Boolean, a: Double, b: Double, e: Double): Pair<Double, Double> =
        withContext(Dispatchers.IO) {
            val step = (b - a) / 4.0
            val f1 = async { findMax(a, a + step, e) }
            val f2 = async { findMax(a + step, a + step * 2, e) }
            val f3 = async { findMax(a + step * 2, a + step * 3, e) }
            val f4 = async { findMax(a + step * 3, a + step * 4, e) }
            /*val f5 = async { findMax(a + step * 4, a + step * 5, e) }
            val f6 = async { findMax(a + step * 5, a + step * 6, e) }
            val f7 = async { findMax(a + step * 6, a + step * 7, e) }
            val f8 = async { findMax(a + step * 7, b, e) }*/
            /*var start = a
            val jobs: MutableList<Deferred<Pair<Double, Double>>> = mutableListOf()
            while (start < b) {
                if (maxOrMin) {
                    jobs.add(async { findMax(start, start + step, e) })
                } else {
                    jobs.add(async { findMin(start, start + step, e) })
                }
                start += step
            }*/
            return@withContext awaitAll(f1, f2, f3, f4/*, f5, f6, f7, f8*/).maxByOrNull { it.second }!!
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

    fun findMax(a: Double, b: Double, e: Double): Pair<Double, Double> {
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