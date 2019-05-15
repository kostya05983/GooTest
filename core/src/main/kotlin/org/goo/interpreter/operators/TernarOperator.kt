package org.goo.interpreter.operators

interface TernarOperator {

    fun interpreter(token1: String, token2: String)
}