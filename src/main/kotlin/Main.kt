import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.letsPlot
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.system.measureNanoTime

suspend fun main(args: Array<String>) = withContext(Dispatchers.Default) {
    val a = 0.0 //2.0e+11
    val b = 50.0 //10.0e+21
    val e = 1e-10 //1e+7
    val maxOrMin = true

    val n = (b - a) / 0.1 //10000
    val h = (b - a) / n
    val data = mapOf("x" to List(n.toInt()) { a + it * h }, "y" to List(n.toInt()) { f(a + it * h) })
    val p = letsPlot(data) + geomLine { x = "x"; y = "y" } + ggsize(1000, 500)

    ggsave(p, "plot.png")

    var coroutineResult: Pair<Double, Double>
    val coroutines = Coroutines(maxOrMin, a, b, e, this) { x -> f(x) } // integrate(0.0, x, 10000) { f_(it) }

    var threadsResult: Pair<Double, Double>
    val threads = Threads(maxOrMin, a, b, e) { x -> f(x) }


    val timeCoroutines = measureNanoTime {
        coroutineResult = coroutines.find()
    } / 1e6


    val timeThreads = measureNanoTime {
        threadsResult = threads.find()
    } / 1e6


    var sequentialResult: Pair<Double, Double>
    val sequential = Sequential(maxOrMin, a, b, e) { x -> f(x) }

    val timeSequential = measureNanoTime {
        sequentialResult = sequential.find()
    } / 1e6

    println(
        "Sequential: ${timeSequential}ms Res: $sequentialResult\n" +
                "Coroutines: ${timeCoroutines}ms Res: $coroutineResult\n" +
                "Threads: ${timeThreads}ms Res: $threadsResult\n" +
                "Difference threads sequential: ${timeSequential - timeThreads}ms\n" +
                "Difference coroutines sequential: ${timeSequential - timeCoroutines}ms\n" +
                "Difference coroutines threads: ${timeThreads - timeCoroutines}ms"
    )
}

fun fh(x: Double): Double {
    return (x + 1).pow(100.0) * exp(-5.0 * x.pow(100)) * x.pow(2.0)
}

fun f(x: Double): Double {
    //return sin(x.pow(0.1)) * x.pow(0.9) + x.pow(10.0) / (x.pow(-100.0) - x.pow(14.0))
    return integrate(0.0, x, x.toInt() * 10000) { f2(it) }
}

fun f2(x: Double): Double {
    return sin(x) / x
}

typealias Func = (Double) -> Double

fun integrate(a: Double, b: Double, n: Int, f: Func): Double {
    val h = (b - a) / n
    var sum = f(a).notNaN() + f(b).notNaN()
    for (i in 0 until n) {
        val x = a + i * h
        val fx = f(x).notNaN()
        sum += if (i % 2 == 0) 4.0 * fx else 2.0 * fx
    }
    return (sum * h / 3.0).notNaN()
}

fun Double.notNaN(): Double = if (this.isNaN()) 0.0 else this