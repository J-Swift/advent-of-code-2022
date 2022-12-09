import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

data class Tree(val height: Int, var isVisible: Boolean = false)

fun main() {
    val input = parseInput(loadInput())

    val rows = input.map {
        it.map { c ->
            Tree(c.digitToInt())
        }
    }

    rows.forEach { row ->
        var maxLeft = -1
        var maxRight = -1
        for (i in row.indices) {
            val j = row.size - 1 - i
            if (row[i].height > maxLeft) {
                row[i].isVisible = true
                maxLeft = row[i].height
            }
            if (row[j].height > maxRight) {
                row[j].isVisible = true
                maxRight = row[j].height
            }
        }
    }

    println("")
    (rows.indices).forEach { colIdx ->
        val col = rows.map { it[colIdx] }

        var maxLeft = -1
        var maxRight = -1
        for (i in col.indices) {
            val j = col.size - 1 - i
            if (col[i].height > maxLeft) {
                col[i].isVisible = true
                maxLeft = col[i].height
            }
            if (col[j].height > maxRight) {
                col[j].isVisible = true
                maxRight = col[j].height
            }
        }
    }

    var visible = 0
    rows.forEach { row ->
        row.forEach { tree ->
            if (tree.isVisible) {
                visible += 1
            }
        }
    }
    println(visible)
}

main()

