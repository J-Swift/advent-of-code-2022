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

    val knotPositions = mutableListOf<Position>().also { list ->
        repeat(10) { list.add(Position(0, 0)) }
    }

    val tailVisited = mutableSetOf(knotPositions.last())

    moves.forEach { move ->
        repeat(move.amount) {
            knotPositions[0] = knotPositions[0].shifted(move.direction)
            (0 until knotPositions.size - 1).forEach { idx ->
                val headPos = knotPositions[idx]
                val tailPos = knotPositions[idx + 1]

                if (!areTouching(tailPos, headPos)) {
                    val adjustments = adjustmentsNeeded(headPos, tailPos)
                    knotPositions[idx + 1] =
                        adjustments.fold(knotPositions[idx + 1]) { memo, movement ->
                            memo.fromMovement(
                                movement
                            )
                        }

                }
            }
            tailVisited.add(knotPositions.last())
        }
    }

    println(tailVisited.size)
}

main()
