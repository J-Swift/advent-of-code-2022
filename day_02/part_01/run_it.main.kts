import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

enum class RPS {
    Rock {
        override val score: Int = 1
    },
    Paper {
        override val score: Int = 2
    },
    Scissors {
        override val score: Int = 3
    };

    abstract val score: Int
}

enum class RPSResult {
    Win {
        override val score: Int = 6
    },
    Lose {
        override val score: Int = 0
    },
    Draw {
        override val score: Int = 3
    };

    abstract val score: Int
}

fun rpsFor(c: Char): RPS {
    return when (c) {
        'A', 'X' -> RPS.Rock
        'B', 'Y' -> RPS.Paper
        'C', 'Z' -> RPS.Scissors
        else -> throw IllegalArgumentException("Invalid value [$c]")
    }
}

fun resultFor(mine: RPS, theirs: RPS): RPSResult {
    return when {
        mine == theirs -> RPSResult.Draw
        (mine == RPS.Rock && theirs == RPS.Scissors)
                || (mine == RPS.Paper && theirs == RPS.Rock)
                || (mine == RPS.Scissors && theirs == RPS.Paper) -> RPSResult.Win
        else -> RPSResult.Lose
    }
}

fun scoreFor(a: Char, b: Char): Int {
    val mine = rpsFor(b)
    val theirs = rpsFor(a)

    return resultFor(mine, theirs).score + mine.score
}

fun main() {
    val input = parseInput(loadInput())

    val pairs = input.map { it.split(" ") }
    val scores = pairs.map { scoreFor(it[0][0], it[1][0]) }
    println(scores.sum())
}

main()

