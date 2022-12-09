import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

sealed class FSNode(open val name: String, open var size: Int?, open var parent: DirFSNode?) {
    data class FileFSNode(
        override val name: String,
        override var size: Int?,
        override var parent: DirFSNode?
    ) :
        FSNode(name, size, parent)

    data class DirFSNode(
        override val name: String,
        override var parent: DirFSNode?,
        var children: List<FSNode>
    ) :
        FSNode(name, children.sumOf { it.size ?: 0 }, parent)
}

fun printDir(node: FSNode, indentLevel: Int = 0) {
    val indent = " ".repeat(indentLevel * 2)

    when (node) {
        is FSNode.FileFSNode -> {
            println("$indent- ${node.name} (file) (${node.size})")
        }
        is FSNode.DirFSNode -> {
            println("$indent- ${node.name} (dir) (${node.size})")
            node.children.forEach {
                printDir(it, indentLevel + 1)
            }
        }
        else -> throw IllegalArgumentException()
    }
}

fun resolveSizes(node: FSNode) {
    if (node is FSNode.DirFSNode) {
        node.children.forEach { resolveSizes(it) }
        node.size = node.children.sumOf { it.size ?: 0 }
    }
}

sealed class CLICommand {
    data class LSCommand(val output: List<CLIOutput>) : CLICommand()
    data class CDCommand(val target: String) : CLICommand()
}

sealed class CLIOutput(open val name: String) {
    data class DirOutput(override val name: String) : CLIOutput(name)
    data class FileOutput(override val name: String, val size: Int) : CLIOutput(name)
}

fun parseOutput(input: String): CLIOutput {
    return when {
        input.startsWith("dir ") -> CLIOutput.DirOutput(input.substring(4))
        else -> {
            val parts = input.split(" ")
            return CLIOutput.FileOutput(parts[1], parts[0].toInt(10))
        }
    }
}

fun parseCommands(input: List<String>): List<CLICommand> {
    val res = mutableListOf<CLICommand>()
    var currentOutput = mutableListOf<CLIOutput>()
    var isParsingOutput = false

    input.forEach { line ->
        if (line.startsWith("$ ")) {
            if (isParsingOutput) {
                isParsingOutput = false
                res.add(CLICommand.LSCommand(currentOutput))
                currentOutput = mutableListOf()
            }
            when (line) {
                "$ ls" -> {
                    isParsingOutput = true
                }
                else -> {
                    res.add(CLICommand.CDCommand(line.substring(5)))
                }
            }
        } else {
            currentOutput.add(parseOutput(line))
        }
    }

    if (isParsingOutput) {
        res.add(CLICommand.LSCommand(currentOutput))
    }

    return res
}

fun getAllDirs(
    node: FSNode,
    res: MutableList<FSNode.DirFSNode> = mutableListOf()
): List<FSNode.DirFSNode> {
    if (node is FSNode.DirFSNode) {
        res.add(node)
        node.children.forEach { getAllDirs(it, res) }
    }

    return res
}

fun main() {
    val input = parseInput(loadInput())

    val commands = parseCommands(input)

    var currentDir = FSNode.DirFSNode("/", null, listOf())
    val rootDir = currentDir
    commands.subList(1, commands.size).forEach { command ->
        when (command) {
            is CLICommand.CDCommand -> {
                currentDir = if (command.target == "..") {
                    currentDir.parent!!
                } else {
                    currentDir.children.first { it is FSNode.DirFSNode && it.name == command.target } as FSNode.DirFSNode
                }
            }
            is CLICommand.LSCommand -> {
                currentDir.children = command.output.map {
                    when (it) {
                        is CLIOutput.FileOutput -> FSNode.FileFSNode(it.name, it.size, currentDir)
                        is CLIOutput.DirOutput -> FSNode.DirFSNode(it.name, currentDir, listOf())
                        else -> throw IllegalArgumentException()
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    resolveSizes(rootDir)
//    printDir(rootDir)

    val allDirs = getAllDirs(rootDir)
    val totalAvail = 70000000
    val targetSpace = 30000000
    val currentSpace = totalAvail - rootDir.size!!
    val spaceToRelease = targetSpace - currentSpace

    val targetDirs = allDirs.filter { it.size!! >= spaceToRelease }
    println(targetDirs.sortedBy { it.size!! }.first().size!!)
}

main()
