package org.goo.debugger

import org.goo.api.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token
import org.goo.scanner.Tokens

class Debugger(private val inputStrategy: InputStrategy,
               private val interpreter: Interpreter) {
    val stopPoints = mutableListOf<Int>()
    private var currentDebugLine: Int? = null
    var isRunning = false


    fun debug(tokens: List<Token>) {
        interpreter.init(tokens)

        if (interpreter.stackTrace.isEmpty()) return
        while (isRunning && !stopPoints.contains(interpreter.currentLine)
                && currentDebugLine != interpreter.currentLine) {
            interpreter.executeSecondVersion()
        }
        currentDebugLine = interpreter.currentLine
        waitInput()
    }

    fun reset() {
        currentDebugLine = null
    }


    private fun waitInput() {
//        println("Stopped at ${1 + currentDebugLine!!}")
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

    private fun stepInto() {
        if (interpreter.stackTrace.isEmpty()) return
        val line = interpreter.executableLines[interpreter.currentLine]
        interpreter.executeSecondVersion()
//        interpreter.step()
        currentDebugLine = interpreter.currentLine
        waitInput()
    }

    private fun stepOver() {
        currentDebugLine = interpreter.currentLine + 1
        if (interpreter.stackTrace.isEmpty()) return
        while (isRunning && interpreter.stackTrace.isNotEmpty() && stopPoints.contains(interpreter.currentLine)
                || currentDebugLine != interpreter.currentLine) {
//            interpreter.step()
            interpreter.executeSecondVersion()
        }
        waitInput()
    }

    private fun showMemory() {
        val memory = interpreter.memory
        for ((key, value) in memory) {
            interpreter.outputStrategy.print("$key -> $value")
        }
        waitInput()
    }

    private fun showStackTrace() {
        val stackTrace = interpreter.stackTrace.reversed()

        for (element in stackTrace) {
            interpreter.outputStrategy.print("Line=${element.line} name=${element.name}")
        }
        waitInput()
    }
}