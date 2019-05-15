package org.goo.view

import org.goo.api.InputStrategy
import org.goo.debugger.Commands
import tornadofx.Controller
import java.io.PipedInputStream

class InputConsoleWindow() : InputStrategy, Controller() {
    private val input: PipedInputStream by param()

    override fun wait(func: (input: String) -> Unit, debugLine: Int) {
        fire(DebugLineEvent(debugLine)) //highlight line
        val line = input.readBytes().toString()
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