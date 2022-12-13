import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

data class Point(
    val x: Int,
    val y: Int,
    val elevation: Int,
    val isStart: Boolean = false,
    val isTarget: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is Point) {
            return false
        }

        return x == other.x && y == other.y
    }
}

fun toPoint(input: Char, x: Int, y: Int): Point {
    return when (input) {
        'S' -> Point(x, y, 'a' - 'a', isStart = true)
        'E' -> Point(x, y, 'z' - 'a', isTarget = true)
        else -> Point(x, y, input - 'a')
    }
}

fun canVisit(from: Point, to: Point): Boolean {
    return to.elevation <= (from.elevation + 1)
}

fun surroundingPoints(points: List<List<Point>>, target: Point): List<Point> {
    val res = mutableListOf<Point>()

    if (target.x > 0) {
        res.add(points[target.y][target.x - 1])
    }
    if (target.x < points[0].size - 1) {
        res.add(points[target.y][target.x + 1])
    }

    if (target.y > 0) {
        res.add(points[target.y - 1][target.x])
    }
    if (target.y < points.size - 1) {
        res.add(points[target.y + 1][target.x])
    }

    return res
}

// Mostly from https://www.redblobgames.com/pathfinding/a-star/introduction.html#dijkstra
fun solve(
    points: List<List<Point>>,
    initialFrom: Point,
    to: Point
): Int? {
    val bestCostSoFar = mutableMapOf(Pair(initialFrom, 0))
    val toVisit = mutableListOf(initialFrom)

    while (toVisit.isNotEmpty()) {
//        println("Work size [${toVisit.size}]")
        val current = toVisit.removeFirst()

        surroundingPoints(points, current).forEach { next ->
            val newCost = bestCostSoFar[current]!! + 1
            if (canVisit(
                    current,
                    next
                ) && (!bestCostSoFar.contains(next) || newCost < bestCostSoFar[next]!!)
            ) {
                bestCostSoFar[next] = newCost
                toVisit.add(next)
            }
        }
    }

    return bestCostSoFar[to]
}

fun main() {
    val input = parseInput(loadInput())

    var starts = mutableListOf<Point>()
    var end: Point? = null
    val points = input.withIndex().map { line ->
        line.value.withIndex().map { c ->
            val res = toPoint(c.value, c.index, line.index)
            if (res.elevation == 'a' - 'a') {
                starts.add(res)
            }
            if (res.isTarget) {
                end = res
            }
            res
        }
    }

    println(starts.minOfOrNull { start ->
        solve(points, start, end!!) ?: Int.MAX_VALUE
    })
//    val res = solve(points, start!!, end!!)
//    println("[${res}]")
}

main()

