package org.goo.debugger

import org.goo.InputStrategy

class InputTest(private val command: String) : InputStrategy {

    private var invoke: Boolean = false

    override fun wait(func: (input: String) -> Unit) {
        if (!invoke) {
            invoke = true
            func.invoke(command)
        }
    }
}