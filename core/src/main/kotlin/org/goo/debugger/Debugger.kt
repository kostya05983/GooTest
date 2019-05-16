package org.goo.debugger

import org.goo.api.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token

/**
 * Class of debugger contains interpreter to manage
 * and strategy to get messages from user
 * @author kostya05983
 */
class Debugger(private val inputStrategy: InputStrategy,
               private val interpreter: Interpreter) {
    val stopPoints = mutableListOf<Int>()
    private var currentDebugLine: Int? = null
    var isRunning = false

    /**
     * Debug our code until meet stopPoint or currentDebugLine
     * currentDebugLine - the line where debugger want to stop in future
     */
    fun debug(tokens: List<Token>) {
        interpreter.init(tokens)

        if (!isRunning || interpreter.stackTrace.isEmpty()) return
        while (isRunning && interpreter.stackTrace.isNotEmpty()
                && !stopPoints.contains(interpreter.currentLine)
                && currentDebugLine != interpreter.currentLine) {
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
                Commands.STEP_INTO.text -> {
                    stepInto()
                }
                Commands.STEP_OVER.text -> {
                    stepOver()
                }
                Commands.TRACE.text -> {
                    showStackTrace()
                }
                Commands.VAR.text -> {
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
        if (interpreter.stackTrace.isEmpty()) return
        interpreter.execute()
        currentDebugLine = interpreter.currentLine
        if (interpreter.stackTrace.isEmpty()) return
        waitInput()
    }

    /**
     * Step over the line, just go next and wait for intersection
     */
    private fun stepOver() {
        currentDebugLine = interpreter.currentLine + 1
        if (interpreter.stackTrace.isEmpty()) return

        val temp = mutableListOf<Int>().apply {
            addAll(stopPoints)
        }
        temp.remove(interpreter.currentLine)
        while (isRunning && interpreter.stackTrace.isNotEmpty() &&
                !temp.contains(interpreter.currentLine)
                && currentDebugLine != interpreter.currentLine) {
            interpreter.execute()
        }

        if (interpreter.stackTrace.isEmpty()) return
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