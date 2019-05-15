package org.goo.interpreter.operators

import org.goo.OutputStrategy

class PrintOperator(private val memory: Map<String, String>, private val output: OutputStrategy) : Operator {
    override fun interpreter(vararg args: String) {
        output.print(memory[args[0]]!!)
    }
}