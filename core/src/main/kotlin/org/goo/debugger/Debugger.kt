package org.goo.debugger

import org.goo.InputStrategy
import org.goo.interpreter.Interpreter
import org.goo.scanner.Token

class Debugger(private val inputStrategy: InputStrategy) {
    val stopPoints = mutableListOf<Int>()
    private var currentDebugLine: Int? = null

    private val interpetator: Interpreter = Interpreter()

    fun debug(tokens: List<Token>) {
        interpetator.init(tokens)

        while (interpetator.stackTrace.isNotEmpty() && !stopPoints.contains(interpetator.currentLine)
                && currentDebugLine != interpetator.currentLine) {
            interpetator.step()
        }
        currentDebugLine = interpetator.currentLine
        waitInput()
    }


    private fun waitInput() {
//        println("Stopped at ${1 + currentDebugLine!!}")
        inputStrategy.wait {
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
        }
    }

    private fun stepInto() {
        interpetator.step()
        currentDebugLine = interpetator.currentLine
        waitInput()
    }

    private fun stepOver() {
        currentDebugLine = interpetator.currentLine + 1
        while (interpetator.stackTrace.isNotEmpty() && stopPoints.contains(interpetator.currentLine)
                || currentDebugLine != interpetator.currentLine) {
            interpetator.step()
        }
        waitInput()
    }

    private fun showMemory() {
        val memory = interpetator.memory
        for ((key, value) in memory) {
            println("$key -> $value")
        }
        waitInput()
    }

    private fun showStackTrace() {
        val stackTrace = interpetator.stackTrace.reversed()

        for (element in stackTrace) {
            println("Line=${element.line} name=${element.name}")
        }
        waitInput()
    }
}