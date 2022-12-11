import java.io.File
import kotlin.math.abs

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

enum class Direction {
    Up, Down, Left, Right
}

data class Movement(val direction: Direction, val amount: Int)

data class Position(val x: Int, val y: Int) {
    fun shifted(direction: Direction): Position {
        return fromMovement(Movement(direction, 1))
    }

    fun fromMovement(movement: Movement): Position {
        return when (movement.direction) {
            Direction.Up -> Position(x, y + movement.amount)
            Direction.Down -> Position(x, y - movement.amount)
            Direction.Left -> Position(x - movement.amount, y)
            Direction.Right -> Position(x + movement.amount, y)
        }
    }
}

fun parseMove(input: String): Movement {
    val parts = input.split(" ")
    val dir = when (parts[0]) {
        "U" -> Direction.Up
        "D" -> Direction.Down
        "L" -> Direction.Left
        "R" -> Direction.Right
        else -> throw IllegalArgumentException("Invalid value [$parts]")
    }

    return Movement(dir, parts[1].toInt(10))
}

fun areTouching(pos1: Position, pos2: Position): Boolean {
    return abs(pos1.x - pos2.x) <= 1
            && abs(pos1.y - pos2.y) <= 1
}

// NOTE(jpr): this relies on the fact that we only move once per tick
fun adjustmentsNeeded(headPos: Position, tailPos: Position): List<Movement> {
    if (headPos.y == tailPos.y) {
        if (headPos.x > tailPos.x) {
            return listOf(Movement(Direction.Right, 1))
        }
        return listOf(Movement(Direction.Left, 1))
    }

    if (headPos.x == tailPos.x) {
        if (headPos.y > tailPos.y) {
            return listOf(Movement(Direction.Up, 1))
        }
        return listOf(Movement(Direction.Down, 1))
    }

    if (headPos.y - tailPos.y == 2) {
        if (headPos.x > tailPos.x) {
            return listOf(Movement(Direction.Right, 1), Movement(Direction.Up, 1))
        }
        return listOf(Movement(Direction.Left, 1), Movement(Direction.Up, 1))
    }

    if (headPos.y - tailPos.y == -2) {
        if (headPos.x > tailPos.x) {
            return listOf(Movement(Direction.Right, 1), Movement(Direction.Down, 1))
        }
        return listOf(Movement(Direction.Left, 1), Movement(Direction.Down, 1))
    }

    if (headPos.x - tailPos.x == 2) {
        if (headPos.y > tailPos.y) {
            return listOf(Movement(Direction.Up, 1), Movement(Direction.Right, 1))
        }
        return listOf(Movement(Direction.Down, 1), Movement(Direction.Right, 1))
    }

    if (headPos.x - tailPos.x == -2) {
        if (headPos.y > tailPos.y) {
            return listOf(Movement(Direction.Up, 1), Movement(Direction.Left, 1))
        }
        return listOf(Movement(Direction.Down, 1), Movement(Direction.Left, 1))
    }

    throw IllegalArgumentException("Invalid pos [$headPos] [$tailPos]")
}

fun main() {
    val input = parseInput(loadInput())

    val moves = input.map { parseMove(it) }

    var headPos = Position(0, 0)
    var tailPos = headPos

    val tailVisited = mutableSetOf(tailPos)

    moves.forEach { move ->
        repeat(move.amount) {
            headPos = headPos.shifted(move.direction)
            if (!areTouching(tailPos, headPos)) {
                val adjustments = adjustmentsNeeded(headPos, tailPos)
                tailPos =
                    adjustments.fold(tailPos) { memo, movement -> memo.fromMovement(movement) }
                tailVisited.add(tailPos)
            }
        }
    }

    println(tailVisited.size)
}

main()
