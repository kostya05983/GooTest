package org.goo.interpreter.operators

import org.goo.interpreter.Interpreter

class CallOperator(private val interpreter: Interpreter) : Operator {
    override fun interpreter(vararg args: String) {
        //TODO runtime errors
        interpreter.setCurrentLine(args[0], args[1].toInt())
    }
}