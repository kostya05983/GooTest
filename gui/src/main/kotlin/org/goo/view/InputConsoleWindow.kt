package org.goo.view

import org.goo.api.InputStrategy
import org.goo.debugger.Commands
import tornadofx.Controller
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PipedInputStream

class InputConsoleWindow() : InputStrategy, Controller() {
    val input: PipedInputStream by param()

    override fun wait(func: (input: String) -> Unit, debugLine: Int) {
        fire(DebugLineEvent(debugLine)) //highlight line
        val bufferedReader = BufferedReader(InputStreamReader(input))
        val line = bufferedReader.readLine()
        restore(line)
        func.invoke(line)
    }

    private fun restore(line: String) {
        when (line) {
            Commands.STEP_INTO.text -> {
                fire(RestoreColor())
            }
            Commands.STEP_OVER.text -> {
                fire(RestoreColor())
            }
        }
    }
}