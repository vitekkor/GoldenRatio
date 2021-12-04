import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomDensity
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.letsPlot
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.system.measureNanoTime

suspend fun main(args: Array<String>) = withContext(Dispatchers.Default) {
    val a = 0.0
    val b = 1.0
    val e = 1e-10

    val n = 10000
    val h = (b - a) / n
    /*val data = mapOf("x" to List(n) { a + it * h }, "y" to List(n) { f(a + it * h) })
    var p = letsPlot(data) + geomDensity { x = "x"; y = "y" } + ggsize(500, 250)

    ggsave(p, "plot.png")*/

    var coroutineResult: Pair<Double, Double>
    val coroutines = Coroutines(true, a, b, e, this) { x -> integrate(0.0, x, 10000) { fg(it) } }

    var threadsResult: Pair<Double, Double>
    val threads = Threads(true, a, b, e) { x -> integrate(0.0, x, 10000) { fg(it) } }


    val timeCoroutines = measureNanoTime {
        coroutineResult = coroutines.find()
    } / 1e6


    val timeThreads = measureNanoTime {
        threadsResult = threads.find()
    } / 1e6


    var sequentialResult: Pair<Double, Double>
    val sequential = Sequential(true, a, b, e) { x -> integrate(0.0, x, 10000) { fg(it) } }

    val timeSequential = measureNanoTime {
        sequentialResult = sequential.find()
    } / 1e6

    println(
        "Sequential: $timeSequential Res: $sequentialResult\n" +
                "Coroutines: $timeCoroutines Res: $coroutineResult\n" +
                "Threads: $timeThreads Res: $threadsResult\n" +
                "Difference threads sequential: ${timeSequential - timeThreads}\n" +
                "Difference coroutines sequential: ${timeSequential - timeCoroutines}\n" +
                "Difference coroutines threads: ${timeThreads - timeCoroutines}"
    )
}

fun fh(x: Double): Double {
    return (x + 1).pow(100.0) * exp(-5.0 * x.pow(100)) * x.pow(2.0)
}

fun f(x: Double): Double {
    return integrate(0.0, x, 10000) { fg(it) }
}

fun fg(x: Double): Double {
    return x.pow(4)
}

typealias Func = (Double) -> Double

fun integrate(a: Double, b: Double, n: Int, f: Func): Double {
    val h = (b - a) / n
    var sum = 0.0
    for (i in 0 until n) {
        val x = a + i * h
        sum += (f(x) + 4.0 * f(x + h / 2.0) + f(x + h)) / 6.0
    }
    return sum
}