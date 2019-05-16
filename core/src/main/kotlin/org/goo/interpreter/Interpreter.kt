package org.goo.interpreter

import org.goo.api.OutputConsole
import org.goo.api.OutputStrategy
import org.goo.interpreter.operators.*
import org.goo.scanner.Token
import org.goo.scanner.Tokens
import java.util.*

class Interpreter(val outputStrategy: OutputStrategy) {
    private var subs: MutableMap<String, Int> = mutableMapOf()
    val memory: MutableMap<String, String> = mutableMapOf()

    val stackTrace: Stack<StackElement> = Stack()
    lateinit var executableLines: MutableList<Line>
    var currentLine: Int = 0

    private var operators: MutableMap<String, Operator> = mutableMapOf(
            Tokens.CALL.text to CallOperator(this),
            Tokens.PRINT.text to PrintOperator(memory, OutputConsole()),
            Tokens.SET.text to SetOperator(memory),
            Tokens.SUB.text to SubOperator()
    )

    fun init(tokens: List<Token>) {
        mapToSubs(tokens)

        val mainLine = subs["main"]
        //put main in stack
        setCurrentLine("main", mainLine!!)
    }

    fun interpret(tokens: List<Token>) {
        mapToSubs(tokens)

        //put main in stack
        setCurrentLine("main", -1)

        while (stackTrace.isNotEmpty()) {
//            step()
            execute()
//            if (isSubStart && executableLines[currentLine].tokens[0].token == Tokens.SUB) {
//                isSubStart = false
//                currentLine++
//                continue
//            }
//            if (currentLine >= executableLines.size || !isSubStart && executableLines[currentLine].tokens[0].token == Tokens.SUB) {
//                val pop = stackTrace.pop()
//                currentLine = pop.line
//                isSubStart = true
//                continue
//            }
//            if (executableLines[currentLine].tokens[0].token == Tokens.CALL) {
//                val line = executableLines[currentLine]
//                decompise(line.tokens)
//                isSubStart = true
//                continue
//            }
//            val line = executableLines[currentLine]
//            decompise(line.tokens)
//            currentLine++
        }
    }

    fun executeSecondVersion() {
        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line
            return
        }

        val line = executableLines[currentLine]

        if (line.tokens[0].token == Tokens.SUB && line.isExecuted) {
            val pop = stackTrace.pop()
            currentLine = pop.line
            return
        }
        if (line.isExecuted) {
            currentLine++
            return
        }
        if (line.tokens[0].token == Tokens.CALL) {
            decompise(line.tokens)
            line.isExecuted = true
            return
        }

        decompise(line.tokens)
        line.isExecuted = true
        currentLine++

        //Check post return
        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line
            return
        }
        val nextLine = executableLines[currentLine]
        if (nextLine.tokens[0].token == Tokens.SUB && nextLine.isExecuted) {
            val pop = stackTrace.pop()
            currentLine = pop.line
            return
        }
    }

    fun execute() {
        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line
            return
        }
        val line = executableLines[currentLine]
        if (!line.isExecuted) {
            if (line.tokens[0].token == Tokens.SUB) {
                line.isExecuted = true
                currentLine++
                return
            }
            decompise(line.tokens)
            line.isExecuted = true
        } else {
            if (line.tokens[0].token == Tokens.SUB) {
                val pop = stackTrace.pop()
                currentLine = pop.line
                return
            }
            currentLine++
            val nextLine = executableLines[currentLine]
            decompise(nextLine.tokens)
            nextLine.isExecuted = true
        }
    }

//    fun step() {
//        if (isSubStart && executableLines[currentLine].tokens[0].token == Tokens.SUB) {
//            isSubStart = false
//            currentLine++
//            return
//        }
//
//        if (currentLine >= executableLines.size || !isSubStart && executableLines[currentLine].tokens[0].token == Tokens.SUB) {
//            val pop = stackTrace.pop()
//            currentLine = pop.line
//            isSubStart = true
//            return
//        }
//        if (executableLines[currentLine].tokens[0].token == Tokens.CALL) {
//            val line = executableLines[currentLine]
//            decompise(line.tokens)
//            isSubStart = true
//            return
//        }
//        val line = executableLines[currentLine]
//        decompise(line.tokens)
//        currentLine++
//        if (currentLine == executableLines.size) {
//            val pop = stackTrace.pop()
//            currentLine = pop.line
//            isSubStart = true
//            return
//        }
//
//    }

    fun decompise(list: List<Token>) {
        val args = list.subList(1, list.size).map { it.text }.toTypedArray()
        operators[list[0].text]!!.interpreter(*args, list[0].line.toString())
    }

    fun setCurrentLine(name: String, callingLine: Int) {
        val line = subs[name]
        //TODO runtime errors
        stackTrace.push(StackElement(name, callingLine))
        currentLine = line!!
    }


    /**
     * Mapping code blocks
     */
    fun mapToSubs(tokens: List<Token>) {
        val subIndexes = mutableListOf<Int>()

        var lastIndex = 0

        executableLines = mutableListOf()
        for (i in 0 until tokens.size) {
            if (tokens[i].token == Tokens.SUB) {
                subIndexes.add(i)
            }

            if (tokens[i].token == Tokens.NEWLINE) {
                executableLines.add(Line(tokens[i].line, tokens.subList(lastIndex, i), false))
                lastIndex = i + 1
            }
        }

        for (i in 0 until subIndexes.size) {
            val text: String = tokens[subIndexes[i] + 1].text //key
            subs[text] = tokens[subIndexes[i]].line
        }
    }
}

fun test() {

}

fun main() {
    val a = 7
    test()
    println(a)

}