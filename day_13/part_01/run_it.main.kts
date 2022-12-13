import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

sealed class SignalType {
    data class IntSignal(val value: Int) : SignalType() {
        override fun toString(): String {
            return value.toString()
        }
    }

    data class ListSignal(val values: MutableList<SignalType> = mutableListOf()) : SignalType() {
        override fun toString(): String {
            return "[${values.joinToString(",")}]"
        }
    }
}

fun parseSignal(input: String): SignalType.ListSignal {
    val signalStack = mutableListOf<SignalType.ListSignal>()

    val inputStack = java.util.ArrayDeque(input.toList())
    while (inputStack.isNotEmpty()) {
        when (inputStack.peek()) {
            '[' -> {
                inputStack.pop()
                signalStack.add(SignalType.ListSignal())
            }
            ']' -> {
                inputStack.pop()
                if (signalStack.size >= 2) {
                    val res = signalStack.removeLast()
                    signalStack.last().values.add(res)
                }
            }
            ',', ' ' -> {
                inputStack.pop()
            }
            else -> {
                val buffer = StringBuilder()
                while (inputStack.peek().isDigit()) {
                    buffer.append(inputStack.pop())
                }
                val iSignal = SignalType.IntSignal(buffer.toString().toInt(10))
                signalStack.last().values.add(iSignal)
            }
        }
    }

    return signalStack.removeLast()
}

fun isInRightOrder(left: SignalType, right: SignalType, indent: Int = 0): Boolean? {
    when {
        left is SignalType.IntSignal && right is SignalType.IntSignal -> {
            return when {
                left.value == right.value -> null
                else -> left.value < right.value
            }
        }
        left is SignalType.IntSignal -> {
            return isInRightOrder(
                SignalType.ListSignal(mutableListOf(SignalType.IntSignal(left.value))),
                right,
                indent + 1
            )
        }
        right is SignalType.IntSignal -> {
            return isInRightOrder(
                left,
                SignalType.ListSignal(mutableListOf(SignalType.IntSignal(right.value))),
                indent + 1
            )
        }
        left is SignalType.ListSignal && right is SignalType.ListSignal -> {
            val wasRight = left.values.zip(right.values).dropWhile {
                isInRightOrder(it.first, it.second) == null
            }
            if (wasRight.isNotEmpty()) {
                return isInRightOrder(wasRight.first().first, wasRight.first().second)
            }
            return when {
                left.values.size == right.values.size -> null
                else -> left.values.size < right.values.size
            }
        }
        else -> throw IllegalArgumentException("Invalid value [$left] [$right]")
    }
}

fun main() {
    val input = parseInput(loadInput())

    val pairs = mutableListOf<Pair<SignalType, SignalType>>()
    input.windowed(3, 3, true).forEach { window ->
        pairs.add(Pair(parseSignal(window[0]), parseSignal(window[1])))
    }

    println(pairs.withIndex().fold(0) { memo, it ->
        memo + when (isInRightOrder(it.value.first, it.value.second)) {
            true -> it.index + 1
            else -> 0
        }
    })
}

main()