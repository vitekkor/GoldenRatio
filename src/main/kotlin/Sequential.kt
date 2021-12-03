import kotlin.math.abs
import kotlin.math.sqrt

object Sequential {
    private val PHI = (1 + sqrt(5.0)) / 2

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

    fun findMax(a: Double, b: Double, e: Double): Double {
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
        return (a + b) / 2
    }
}