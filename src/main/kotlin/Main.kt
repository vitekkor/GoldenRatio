import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.system.measureTimeMillis

suspend fun main(args: Array<String>) = withContext(Dispatchers.IO) {
    val a = 0.0
    val b = 1000.0
    val e = 1e-12
    var coroutineResult: Pair<Double, Double>
    var threadsResult: Double
    val timeCoroutines = measureTimeMillis {
        coroutineResult = Coroutines.find(true, a, b, e)
    }
    val timeThreads = measureTimeMillis {
        threadsResult = Threads.find(a, b, e)
    }
    var sequentialResult: Double
    val timeSequential = measureTimeMillis {
        sequentialResult = Sequential.findMax(a, b, e)
    }

    println(
        "Sequential: $timeSequential Res: $sequentialResult\n" +
                "Coroutines: $timeCoroutines Res: $coroutineResult\n" +
                "Threads: $timeThreads Res: $threadsResult\n" +
                "Difference: ${timeSequential - timeThreads}"
    )
}

fun fh(x: Double): Double {
    return (x + 1).pow(100.0) * exp(-5.0 * x.pow(100)) * x.pow(2.0)
}

fun f(x: Double): Double {
    return exp(x * 0.0000005)
}

fun fg(x: Double): Double {
    return ln(x.pow(4.0 - cos(x))) - exp(sin(PI * x)) * Math.cbrt(x * 6.0)
}