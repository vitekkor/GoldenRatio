import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlin.math.abs
import kotlin.math.sqrt

class Threads(private val maxOrMin: Boolean, a: Double, b: Double, e: Double, private val f: Func) {
    private val threads = mutableListOf<Thread>()
    private val results = mutableListOf<Pair<Double, Double>>()

    init {
        val processors = Runtime.getRuntime().availableProcessors()
        val step = (b - a) / processors
        for (i in 1..processors) {
            threads.add(Thread { results.add(find(a + (i - 1) * step, a + (i) * step, e)) })
        }
    }

    private val PHI = (1 + sqrt(5.0)) / 2

    fun find(): Pair<Double, Double> {
        threads.forEach(Thread::start)
        threads.forEach(Thread::join)
        Thread.yield()
        return if (maxOrMin) results.maxByOrNull { it.second }!! else results.minByOrNull { it.second }!!
    }

    private fun find(a: Double, b: Double, e: Double): Pair<Double, Double> {
        var a = a
        var b = b
        var x1: Double
        var x2: Double
        while (abs(b - a) > e) {
            x1 = b - (b - a) / PHI
            x2 = a + (b - a) / PHI
            val f1 = f(x1)
            val f2 = f(x2)
            if (maxOrMin) {
                if (f1 <= f2) a = x1 else b = x2
            } else {
                if (f1 >= f2) a = x1 else b = x2
            }
        }
        return (a + b) / 2 to f((a + b) / 2)
    }
}