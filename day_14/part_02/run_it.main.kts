import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

data class Point(val x: Int, val y: Int)

fun expandLineSegment(p1: Point, p2: Point): List<Point> {
    return if (p1 == p2) {
        return listOf(p1)
    } else if (p1.x == p2.x) {
        val (from, to) = if (p1.y < p2.y) Pair(p1, p2) else Pair(p2, p1)

        (from.y..to.y).map { y ->
            Point(p1.x, y)
        }
    } else if (p1.y == p2.y) {
        val (from, to) = if (p1.x < p2.x) Pair(p1, p2) else Pair(p2, p1)

        (from.x..to.x).map { x ->
            Point(x, p1.y)
        }
    } else {
        throw IllegalArgumentException("Invalid values [$p1] [$p2]")
    }
}

fun parsePoint(input: String): Point {
    val parts = input.split(",")
    return Point(parts[0].toInt(10), parts[1].toInt(10))
}

fun parseLine(input: String): List<Point> {
    val segments = input.split(" -> ")
    return segments.map { parsePoint(it) }
}

enum class Space {
    Rock, Sand
}

fun settlePoint(mapLookup: Map<Point, Space>, floorY: Int): Point {
    var curPoint = Point(500, 0)

    while (true) {
        if (floorY == curPoint.y+1) {
            return curPoint
        } else if (!mapLookup.containsKey(Point(curPoint.x, curPoint.y + 1))) {
            curPoint = Point(curPoint.x, curPoint.y + 1)
        } else if (!mapLookup.containsKey(Point(curPoint.x - 1, curPoint.y + 1))) {
            curPoint = Point(curPoint.x - 1, curPoint.y + 1)
        } else if (!mapLookup.containsKey(Point(curPoint.x + 1, curPoint.y + 1))) {
            curPoint = Point(curPoint.x + 1, curPoint.y + 1)
        } else {
            return curPoint
        }
    }
}

fun main() {
    val input = parseInput(loadInput())

    val lines = input.map { parseLine(it) }

    val maxYForX = mutableMapOf<Int, Int>()
    var floorY = -1
    val lookup = lines.fold(mutableMapOf<Point, Space>()) { memo, line ->
        line.windowed(2, 1, true) {
            expandLineSegment(it.first(), it.last()).forEach { point ->
                memo[point] = Space.Rock
                if (!maxYForX.containsKey(point.x) || maxYForX[point.x]!! < point.y) {
                    maxYForX[point.x] = point.y
                    if (point.y + 2 > floorY) {
                        floorY = point.y + 2
                    }
                }
            }
        }
        memo
    }

    var keepGoing = true
    var total = 1
    while (keepGoing) {
        val nextPoint = settlePoint(lookup, floorY)
        if (nextPoint == Point(500, 0)) {
            keepGoing = false
        } else {
            total++
            lookup[nextPoint] = Space.Sand
        }
    }
    println("Total: $total")
}

main()

