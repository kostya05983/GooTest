package org.goo.interpreter.operators

interface Operator {
    fun interpreter(vararg args: String)
}