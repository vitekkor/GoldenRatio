import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.sqrt

object Threads {
    //val result = mutableListOf<Double>()
    val executorService = Executors.newFixedThreadPool(2)
    private val PHI = (1 + sqrt(5.0)) / 2
    fun find(a: Double, b: Double, e: Double): Double {
        var f1: Double = 0.0
        var f2: Double = 0.0
        val t1 = executorService.submit {
            f1 = findMax(a, (b - a) / 2, e)
        }
        val t2 = executorService.submit {
            f2 = findMax((b - a) / 2, b, e)
        }
        t2.get()
        return maxOf(f1, f2)
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