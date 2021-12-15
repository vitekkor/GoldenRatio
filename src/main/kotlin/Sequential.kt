import kotlin.math.abs
import kotlin.math.sqrt

class Sequential(private val maxOrMin: Boolean, a: Double, b: Double, e: Double, private val f: Func) {
    private val PHI = (1 + sqrt(5.0)) / 2

    private val result = mutableListOf<Lazy<Pair<Double, Double>>>()

    init {
        val processors = Runtime.getRuntime().availableProcessors()
        val step = (b - a) / processors
        for (i in 1..processors) {
            result.add(lazy { find(a + (i - 1) * step, a + (i) * step, e) })
        }
    }

    fun find(): Pair<Double, Double> {
        return if (maxOrMin) result.maxByOrNull { it.value.second }!!.value else result.minByOrNull { it.value.second }!!.value
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