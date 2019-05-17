package org.goo.interpreter

import org.goo.api.OutputStrategy
import org.goo.interpreter.operators.*
import org.goo.scanner.Token
import org.goo.scanner.Tokens
import java.util.*

/**
 * Interpreter implementation
 * isRunning - for interruption when interpreter cycles in recursion
 * stackTrace - current stack of our program
 * currentLine - line to execute now
 * codeMapper - contains subs with lines to jump
 * memory - memory of our program
 * @param outputStrategy - strategy for utputing information
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

    /**
     * Init interpreter for run
     * @param tokens - scanned tokens from program
     */
    internal fun init(tokens: List<Token>) {
        isRunning = true
        codeMapper.mapToSubs(tokens)

        val mainLine = codeMapper.subs["main"]
        //put main in stack
        setCurrentLine("main", mainLine!!)
        currentLine++
    }

    /**
     * Reset interpreter to default state
     */
    fun reset() {
        isRunning = false
        memory.clear()
        stackTrace.clear()
        currentLine = 0
        codeMapper.reset()
    }

    /**
     * Interpreter transferred tokens
     */
    fun interpret(tokens: List<Token>) {
        init(tokens)
        while (isRunning && stackTrace.isNotEmpty()) {
            execute()
        }
    }

    /**
     * Find next line for step_over
     * @return - next line where to stop after step_over
     */
    fun findNextLine(): Int {
        var result = currentLine + 1
        while (result < codeMapper.executableLines.size && codeMapper.executableLines[result].tokens.isEmpty()) {
            result++
        }
        return result
    }

    /**
     * Execute separate line and increment
     * First we check isn't this last execution?
     * Second we skip empty lines
     * Third we check isn't this last execution?
     * Fourth we check isn't this end of sub, cause we meet another sub?
     *
     */
    fun execute() {
        if (isRunning && currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }

        val line = codeMapper.executableLines[currentLine]
        if (line.tokens.isEmpty()) {
            currentLine++
            stackTrace.peek().line = currentLine
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
        stackTrace.peek().line = currentLine

        //Check post return
        checkPostCondition()
    }

    /**
     * Check post conditions for recursion return from calling subs
     * First we check isn't this last execution?
     * Second we skip empty lines
     * Third we check isn't this last execution?
     * Fourth we check isn't this end of sub, cause we meet another sub?
     */
    private fun checkPostCondition() {
        //Check post return
        if (isRunning && currentLine >= codeMapper.executableLines.size) {
            stackTrace.popStackElement()
            return
        }
        var nextLine = codeMapper.executableLines[currentLine]
        while (nextLine.tokens.isEmpty()) {
            currentLine++
            stackTrace.peek().line = currentLine
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

    /**
     * When we call new sub, we set new sub to stack and change line
     */
    fun setCurrentLine(name: String, callingLine: Int) {
        val line = codeMapper.subs[name] ?: error("Such sub not found name=$name")
        stackTrace.push(StackElement(name, callingLine))
        currentLine = line
    }

    /**
     * The sub is ended we pop this from stack
     */
    private fun Stack<StackElement>.popStackElement() {
        if (isNotEmpty()) {
            pop()
            if (isNotEmpty()) {
                val peek = peek()
                peek.line += 1
                currentLine = peek.line
            }
            checkPostCondition()
        }
    }
}