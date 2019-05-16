package org.goo.interpreter.operators

import org.goo.api.OutputStrategy

class PrintOperator(private val memory: Map<String, String>, private val output: OutputStrategy) : Operator {
    override fun interpreter(vararg args: String) {
        val variable = memory[args[0]]
                ?: error("var= ${args[0]} Not found variable in memory")
        output.print(variable)
    }
}