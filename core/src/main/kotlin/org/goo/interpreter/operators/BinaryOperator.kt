package org.goo.interpreter.operators

interface BinaryOperator {
    fun interpreter(tokens: String)
}