package org.goo.debugger

import org.goo.ConsoleCommand
import org.goo.api.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token

/**
 * Class of debugger contains interpreter to manage
 * and strategy to get messages from user
 * @param inputStrategy - strategy for wait input from user
 * @param interpreter - interpreter for interpret code
 * Stop poitns - points where debugger has to be stopped
 * @author kostya05983
 */
class Debugger(private val inputStrategy: InputStrategy,
               private val interpreter: Interpreter) {
    var stopPoints = mutableListOf<Int>()
    private var currentDebugLine: Int? = null
    var isRunning = false

    /**
     * Debug our code until meet stopPoint or currentDebugLine
     * currentDebugLine - the line where debugger want to stop in future
     * About Condition in while loop, first condition
     * isRunning - for signals from user
     * second we check that program isn't ended
     * third we check isn't currentLine is StopPoint?
     * fourth - we check do we reach next debugging line?
     * fifth - skip emptyness lines, if user set stop points on it
     */
    fun debug(tokens: List<Token>) {
        interpreter.init(tokens)

        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        while (isRunning && interpreter.stackTrace.isNotEmpty()
                && currentDebugLine != interpreter.currentLine &&
                !stopPoints.contains(interpreter.currentLine)
                || interpreter.codeMapper
                        .executableLines[interpreter.currentLine].tokens.isEmpty()) {
            interpreter.execute()
        }
        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        currentDebugLine = interpreter.currentLine
        waitInput()
    }

    /**
     * rest debugger to default state
     */
    fun reset() {
        currentDebugLine = null
        interpreter.memory.clear()
    }


    /**
     * behaviour after user input
     */
    private fun waitInput() {
        inputStrategy.wait({
            when (it) {
                ConsoleCommand.STEP_INTO.text -> {
                    stepInto()
                }
                ConsoleCommand.STEP_OVER.text -> {
                    stepOver()
                }
                ConsoleCommand.TRACE.text -> {
                    showStackTrace()
                }
                ConsoleCommand.VAR.text -> {
                    showMemory()
                }
                else -> {
                    interpreter.outputStrategy.print("No such debugger command")
                    waitInput()
                }
            }
        }, currentDebugLine!!)
    }

    /**
     * Just execute line of code and wait
     */
    private fun stepInto() {
        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        interpreter.execute()
        currentDebugLine = interpreter.currentLine
        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        waitInput()
    }

    /**
     * Step over the line, just go next and wait for intersection
     * About Condition in while loop, first condition
     * isRunning - for signals from user
     * second we check that program isn't ended
     * third we check isn't currentLine is StopPoint?
     * fourth - we check do we reach next debugging line?
     * fifth - skip emptyness lines, if user set stop points on it
     */
    private fun stepOver() {
        currentDebugLine = interpreter.findNextLine()
        if (!isRunning || interpreter.stackTrace.isEmpty()) return

        val temp = mutableListOf<Int>().apply {
            addAll(stopPoints)
        }
        temp.remove(interpreter.currentLine)
        while (isRunning && interpreter.stackTrace.isNotEmpty() &&
                !temp.contains(interpreter.currentLine)
                && currentDebugLine != interpreter.currentLine
                || interpreter.codeMapper
                        .executableLines[interpreter.currentLine].tokens.isEmpty()) {
            interpreter.execute()
        }
        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        currentDebugLine = interpreter.currentLine
        waitInput()
    }


    /**
     * Show current memory of our interpreter
     */
    private fun showMemory() {
        val memory = interpreter.memory
        for ((key, value) in memory) {
            interpreter.outputStrategy.print("$key -> $value")
        }
        waitInput()
    }

    /**
     * Just print stackTrace for command
     */
    private fun showStackTrace() {
        val stackTrace = interpreter.stackTrace.reversed()

        for (element in stackTrace) {
            interpreter.outputStrategy.print("Line=${element.line} name=${element.name}")
        }
        waitInput()
    }
}