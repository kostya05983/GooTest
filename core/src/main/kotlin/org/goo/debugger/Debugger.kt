package org.goo.debugger

import org.goo.api.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token

class Debugger(private val inputStrategy: InputStrategy, private val interpreter: Interpreter) {
    val stopPoints = mutableListOf<Int>()
    private var currentDebugLine: Int? = null
    var isRunning = false


    fun debug(tokens: List<Token>) {
        interpreter.init(tokens)

        while (isRunning && interpreter.stackTrace.isNotEmpty() && !stopPoints.contains(interpreter.currentLine)
                && currentDebugLine != interpreter.currentLine) {
            interpreter.step()
        }
        currentDebugLine = interpreter.currentLine
        waitInput()
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
            }
        }, currentDebugLine!!)
    }

    private fun stepInto() {
        interpreter.step()
        currentDebugLine = interpreter.currentLine
        waitInput()
    }

    private fun stepOver() {
        currentDebugLine = interpreter.currentLine + 1
        while (isRunning && interpreter.stackTrace.isNotEmpty() && stopPoints.contains(interpreter.currentLine)
                || currentDebugLine != interpreter.currentLine) {
            interpreter.step()
        }
        waitInput()
    }

    private fun showMemory() {
        val memory = interpreter.memory
        for ((key, value) in memory) {
            println("$key -> $value")
        }
        waitInput()
    }

    private fun showStackTrace() {
        val stackTrace = interpreter.stackTrace.reversed()

        for (element in stackTrace) {
            println("Line=${element.line} name=${element.name}")
        }
        waitInput()
    }
}