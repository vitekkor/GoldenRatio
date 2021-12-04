import kotlin.math.abs
import kotlin.math.sqrt

class Threads(maxOrMin: Boolean, a: Double, b: Double, e: Double, private val f: Func) {
    private val threads = mutableListOf<Thread>()
    private val results = mutableListOf<Pair<Double, Double>>()

    init {
        val step = (b - a) / Runtime.getRuntime().availableProcessors().toDouble()
        var start = a
        while (start + step < b) {
            threads.add(Thread { results.add(findMax(start, start + step, e)) })
            start += step
        }
        threads.add(Thread { results.add(findMax(start, b, e)) })
    }

    private val PHI = (1 + sqrt(5.0)) / 2

    fun find(): Pair<Double, Double> {
        threads.forEach(Thread::start)
        threads.forEach(Thread::join)
        Thread.yield()
        return results.maxByOrNull { it.second }!!
    }

    fun findMax(a: Double, b: Double, e: Double): Pair<Double, Double> {
        var a1 = a
        var b1 = b
        var x1: Double
        var x2: Double
        while (true) {
            x1 = b1 - (b1 - a1) / PHI
            x2 = a1 + (b1 - a1) / PHI
            if (f(x1) <= f(x2)) a1 = x1 else b1 = x2
            if (abs(b1 - a1) < e) break
        }
        return (a1 + b1) / 2 to f((a1 + b1) / 2)
    }
}