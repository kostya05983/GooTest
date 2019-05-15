package org.goo.debugger

import org.goo.api.InputStrategy

class InputTest(private val command: String) : InputStrategy {

    private var invoke: Boolean = false

    override fun wait(func: (input: String) -> Unit, debugLine: Int) {
        if (!invoke) {
            invoke = true
            func.invoke(command)
        }
    }
}