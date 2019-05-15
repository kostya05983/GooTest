package org.goo.interpreter.operators

class SetOperator(private val memory: MutableMap<String, String>) : Operator {
    override fun interpreter(vararg args: String) {
        memory[args[0]] = args[1]
    }
}