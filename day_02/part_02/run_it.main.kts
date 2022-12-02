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
        'A' -> RPS.Rock
        'B' -> RPS.Paper
        'C' -> RPS.Scissors
        else -> throw IllegalArgumentException("Invalid value [$c]")
    }
}

fun resultFor(c: Char): RPSResult {
    return when (c) {
        'X' -> RPSResult.Lose
        'Y' -> RPSResult.Draw
        'Z' -> RPSResult.Win
        else -> throw IllegalArgumentException("Invalid value [$c]")
    }
}

//          R  P  S
// win      P  S  R
// lose     S  R  P
// draw     R  P  S
fun rpsFor(theirs: RPS, result: RPSResult): RPS {
    return when {
        result == RPSResult.Draw -> theirs
        (result == RPSResult.Win && theirs == RPS.Rock)
                || (result == RPSResult.Lose && theirs == RPS.Scissors) -> RPS.Paper
        (result == RPSResult.Win && theirs == RPS.Paper)
                || (result == RPSResult.Lose && theirs == RPS.Rock) -> RPS.Scissors
        (result == RPSResult.Win && theirs == RPS.Scissors)
                || (result == RPSResult.Lose && theirs == RPS.Paper) -> RPS.Rock
        else -> throw IllegalArgumentException("Invalid value [$theirs] [$result]")
    }
}

fun scoreFor(a: Char, b: Char): Int {
    val theirs = rpsFor(a)
    val result = resultFor(b)
    val mine = rpsFor(theirs, result)

    return result.score + mine.score
}

fun main() {
    val input = parseInput(loadInput())

    val pairs = input.map { it.split(" ") }
    val scores = pairs.map { scoreFor(it[0][0], it[1][0]) }
    println(scores.sum())
}

main()

