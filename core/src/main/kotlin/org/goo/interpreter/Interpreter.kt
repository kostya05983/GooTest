package org.goo.interpreter

import org.goo.OutputConsole
import org.goo.interpreter.operators.CallOperator
import org.goo.interpreter.operators.Operator
import org.goo.interpreter.operators.PrintOperator
import org.goo.interpreter.operators.SetOperator
import org.goo.scanner.Token
import org.goo.scanner.Tokens
import java.util.*

class Interpreter {
    private var subs: MutableMap<String, Int> = mutableMapOf()
    private val memory: MutableMap<String, String> = mutableMapOf()

    private val stackTrace: Stack<StackElement> = Stack()
    private lateinit var executableLines: MutableList<Line>
    private var currentLine: Int = 0

    private var operators: MutableMap<String, Operator> = mutableMapOf(
            Tokens.CALL.text to CallOperator(this),
            Tokens.PRINT.text to PrintOperator(memory, OutputConsole()),
            Tokens.SET.text to SetOperator(memory))


    fun interpret(tokens: List<Token>) {
        mapToSubs(tokens)

        //put main in stack
        setCurrentLine("main", -1)

        while (stackTrace.isNotEmpty()) {
            currentLine++
            if (currentLine == executableLines.size || executableLines[currentLine].tokens[0].token == Tokens.SUB) {
                val pop = stackTrace.pop()
                currentLine = pop.line
                continue
            }
            val line = executableLines[currentLine]
            decompise(line.tokens)
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
                executableLines.add(Line(tokens[i].line, tokens.subList(lastIndex + 1, i)))
                lastIndex = i
            }
        }

        for (i in 0 until subIndexes.size) {
            val text: String = tokens[subIndexes[i] + 1].text //key
            subs[text] = tokens[subIndexes[i]].line
        }
    }
}