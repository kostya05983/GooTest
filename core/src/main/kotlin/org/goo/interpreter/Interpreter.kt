package org.goo.interpreter

import org.goo.api.OutputStrategy
import org.goo.interpreter.operators.*
import org.goo.scanner.Token
import org.goo.scanner.Tokens
import java.util.*

/**
 * Interpreter implementation
 * @author kostya05983
 */
class Interpreter(val outputStrategy: OutputStrategy) {
    val memory: MutableMap<String, String> = mutableMapOf()

    val stackTrace: Stack<StackElement> = Stack()
    var currentLine: Int = 0
    val codeMapper: CodeMapper = CodeMapper()
    var isRunning: Boolean = false

    private var operators: MutableMap<String, Operator> = mutableMapOf(
            Tokens.CALL.text to CallOperator(this),
            Tokens.PRINT.text to PrintOperator(memory, outputStrategy),
            Tokens.SET.text to SetOperator(memory),
            Tokens.SUB.text to SubOperator(),
            Tokens.RANDOM.text to RandomOperator(memory)
    )

    fun init(tokens: List<Token>) {
        isRunning = true
        codeMapper.mapToSubs(tokens)

        val mainLine = codeMapper.subs["main"]
        //put main in stack
        setCurrentLine("main", mainLine!!)
        currentLine++
    }

    fun reset() {
        isRunning = false
        memory.clear()
        stackTrace.clear()
        currentLine = 0
        codeMapper.reset()
    }

    fun interpret(tokens: List<Token>) {
        init(tokens)
        while (isRunning && stackTrace.isNotEmpty()) {
            execute()
        }
    }

    fun findNextLine(): Int {
        var result = currentLine + 1
        while (result < codeMapper.executableLines.size && codeMapper.executableLines[result].tokens.isEmpty()) {
            result++
        }
        return result
    }

    /**
     * Execute separate line and increment
     */
    fun execute() {
        if (isRunning && currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }

        val line = codeMapper.executableLines[currentLine]
        if (line.tokens.isEmpty()) {
            currentLine++
            return
        }

        if (currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }

        if (line.tokens[0].token == Tokens.SUB) {
            stackTrace.popStackElement()
            return
        }
        executeOperator(line.tokens)
        line.isExecuted = true
        currentLine++

        //Check post return
        checkPostCondition()
    }

    private fun checkPostCondition() {
        //Check post return
        if (isRunning && currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }
        var nextLine = codeMapper.executableLines[currentLine]
        while (nextLine.tokens.isEmpty()) {
            currentLine++
            nextLine = codeMapper.executableLines[currentLine]
        }

        if (currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }

        if (nextLine.tokens[0].token == Tokens.SUB) {
            stackTrace.popStackElement()
            return
        }
    }

    /**
     * Execute command of language
     * @param list - tokens of command in language
     */
    private fun executeOperator(list: List<Token>) {
        val args = list.subList(1, list.size).map { it.text }.toTypedArray()
        operators[list[0].text]!!.interpreter(*args, list[0].line.toString())
    }

    fun setCurrentLine(name: String, callingLine: Int) {
        val line = codeMapper.subs[name] ?: error("Such sub not found name=$name")
        stackTrace.push(StackElement(name, callingLine))
        currentLine = line
    }

    private fun Stack<StackElement>.popStackElement() {
        val pop = pop()
        currentLine = pop.line + 1
        checkPostCondition()
    }
}