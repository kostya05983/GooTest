package org.goo.interpreter.operators

import kotlin.random.Random

class RandomOperator(private val memory: MutableMap<String, String>) : Operator {
    override fun interpreter(vararg args: String) {
        memory[args[0]] = Random.nextInt().toString()
    }
}