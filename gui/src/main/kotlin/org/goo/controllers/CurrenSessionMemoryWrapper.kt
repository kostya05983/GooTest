package org.goo.controllers

import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.view.Editor
import java.io.PipedOutputStream

class CurrentSessionMemoryWrapper(val editor: Editor,
                                  val outputStrategy: OutputToConsoleWindow) {
    var out: PipedOutputStream? = null
    val currentStopPoints = mutableListOf<Int>()
    var debugger: Debugger? = null
    var interpreter: Interpreter? = null
    var debuggerThread: Thread? = null
    var interpreterThread: Thread? = null
}