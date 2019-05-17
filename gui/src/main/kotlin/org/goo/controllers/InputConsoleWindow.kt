package org.goo.controllers

import org.goo.api.InputStrategy
import org.goo.debugger.Commands
import org.goo.view.DebugLineEvent
import org.goo.view.RestoreColor
import tornadofx.Controller
import java.io.*

/**
 * Realization of api communication with debugger on gui side
 * @author kostya05983
 */
class InputConsoleWindow : InputStrategy, Controller() {
    val input: PipedInputStream by param()

    override fun wait(func: (input: String) -> Unit, debugLine: Int) {
        fire(DebugLineEvent(debugLine)) //highlight line
        val bufferedReader = BufferedReader(InputStreamReader(input))
        try {
            val line = bufferedReader.readLine()
            restore(line)
            func.invoke(line)
        } catch (e: IOException) {
            println(e)
        }
    }

    /**
     * Restore color window if need
     */
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