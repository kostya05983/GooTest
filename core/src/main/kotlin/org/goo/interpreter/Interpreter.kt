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
        init(tokens)
        while (stackTrace.isNotEmpty()) {
            execute()
        }
    }

    fun execute() {
        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }

        var line = executableLines[currentLine]
        while (line.tokens.isEmpty()) {
            currentLine++
            line = executableLines[currentLine]
        }

        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }

        if (line.tokens[0].token == Tokens.SUB && line.isExecuted) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }
        decompise(line.tokens)
        line.isExecuted = true
        currentLine++

        //Check post return
        checkPostCondition()
    }

    private fun checkPostCondition() {
        //Check post return
        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }
        var nextLine = executableLines[currentLine]
        while (nextLine.tokens.isEmpty()) {
            currentLine++
            nextLine = executableLines[currentLine]
        }

        if (currentLine >= executableLines.size) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }

        if (nextLine.tokens[0].token == Tokens.SUB) {
            val pop = stackTrace.pop()
            currentLine = pop.line + 1
            checkPostCondition()
            return
        }
    }

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