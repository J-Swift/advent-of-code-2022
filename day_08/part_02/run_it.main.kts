import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

enum class Direction {
    Left, Right, Up, Down
}

data class Tree(val height: Int, var visibleTrees: HashMap<Direction, Int> = hashMapOf()) {
    fun visibilityScore(): Int {
        return Direction.values().fold(1) { memo, it ->
            (visibleTrees[it] ?: 0) * memo
        }
    }
}

fun main() {
    val input = parseInput(loadInput())

    val rows = input.map {
        it.map { c ->
            Tree(c.digitToInt())
        }
    }

    rows.forEach { row ->
        for (i in row.indices) {
            var keepGoing = true
            var score = 0
            var curI = i - 1
            while (keepGoing && curI >= 0) {
                score += 1
                keepGoing = row[curI].height < row[i].height
                curI -= 1
            }
            row[i].visibleTrees[Direction.Left] = score

            keepGoing = true
            score = 0
            curI = i + 1
            while (keepGoing && curI <= row.size - 1) {
                score += 1
                keepGoing = row[curI].height < row[i].height
                curI += 1
            }
            row[i].visibleTrees[Direction.Right] = score
        }
    }

    (rows.indices).forEach { colIdx ->
        val col = rows.map { it[colIdx] }

        for (i in col.indices) {
            var keepGoing = true
            var score = 0
            var curI = i - 1
            while (keepGoing && curI >= 0) {
                score += 1
                keepGoing = col[curI].height < col[i].height
                curI -= 1
            }
            col[i].visibleTrees[Direction.Up] = score

            keepGoing = true
            score = 0
            curI = i + 1
            while (keepGoing && curI <= col.size - 1) {
                score += 1
                keepGoing = col[curI].height < col[i].height
                curI += 1
            }
            col[i].visibleTrees[Direction.Down] = score
        }
    }

    var maxVisible: Tree = rows[0][0]
    rows.forEach { row ->
        row.forEach { tree ->
            if (tree.visibilityScore() > maxVisible.visibilityScore()) {
                maxVisible = tree
            }
        }
    }

    println(maxVisible)
    println(maxVisible.visibilityScore())
}

main()

