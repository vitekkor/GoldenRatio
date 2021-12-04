import kotlin.math.abs
import kotlin.math.sqrt

class Sequential(maxOrMin: Boolean, a: Double, b: Double, e: Double, private val f: Func) {
    private val PHI = (1 + sqrt(5.0)) / 2

    private val result = mutableListOf<Lazy<Pair<Double, Double>>>()

    init {
        val step = (b - a) / Runtime.getRuntime().availableProcessors().toDouble()
        var start = a
        while (start + step < b) {
            result.add(lazy { findMax(start, start + step, e) })
            start += step
        }
        result.add(lazy { findMax(start, b, e) })
    }

    fun find(): Pair<Double, Double> {
        return result.maxByOrNull { it.value.second }!!.value
    }

    fun findMin(a: Double, b: Double, e: Double): Double {
        var a = a
        var b = b
        var x1: Double
        var x2: Double
        while (true) {
            x1 = b - (b - a) / PHI
            x2 = a + (b - a) / PHI
            if (f(x1) >= f(x2)) a = x1 else b = x2
            if (abs(b - a) < e) break
        }
        return (a + b) / 2
    }

    fun findMax(a: Double, b: Double, e: Double): Pair<Double, Double> {
        var a = a
        var b = b
        var x1: Double
        var x2: Double
        while (true) {
            x1 = b - (b - a) / PHI
            x2 = a + (b - a) / PHI
            if (f(x1) <= f(x2)) a = x1 else b = x2
            if (abs(b - a) < e) break
        }
        return (a + b) / 2 to f((a + b) / 2)
    }
}