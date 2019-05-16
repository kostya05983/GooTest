package org.goo.debugger

import org.goo.api.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token

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
            interpreter.execute()
        }
        currentDebugLine = interpreter.currentLine
        waitInput()
    }

    fun reset() {
        currentDebugLine = null
        interpreter.memory.clear()
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
        interpreter.execute()
        currentDebugLine = interpreter.currentLine
        if (interpreter.stackTrace.isEmpty()) return
        waitInput()
    }

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