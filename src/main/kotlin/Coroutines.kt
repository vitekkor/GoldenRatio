import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.sqrt

class Coroutines(
    private val maxOrMin: Boolean,
    a: Double,
    b: Double,
    e: Double,
    scope: CoroutineScope,
    private val f: Func
) {
    private val PHI = (1 + sqrt(5.0)) / 2
    private val coroutines = mutableListOf<Deferred<Pair<Double, Double>>>()

    init {
        val processors = Runtime.getRuntime().availableProcessors()
        val step = (b - a) / processors
        for (i in 1..processors) {
            coroutines.add(scope.async(start = CoroutineStart.LAZY) { find(a + (i - 1) * step, a + (i) * step, e) })
        }
    }

    suspend fun find(): Pair<Double, Double> {
        coroutines.forEach { it.start(); yield() }
        return if (maxOrMin) coroutines.map { it.await() }
            .maxByOrNull { it.second }!! else coroutines.map { it.await() }.minByOrNull { it.second }!!
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