import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.properties.Delegates

object Threads {
    fun createTreads(a: Double, b: Double, e: Double) {
        t1 = Thread {
            f1 = findMax(a, (b - a) / 2, e)
        }
        t2 = Thread {
            f2 = findMax((b - a) / 2, b, e)
        }
    }

    //val result = mutableListOf<Double>()
    private lateinit var t1: Thread
    private var f1 by Delegates.notNull<Double>()

    private lateinit var t2: Thread
    private var f2 by Delegates.notNull<Double>()

    private val PHI = (1 + sqrt(5.0)) / 2
    fun find(a: Double, b: Double, e: Double): Double {
        t1.start()
        t2.start()
        t2.join()
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