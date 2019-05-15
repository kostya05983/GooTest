package org.goo

interface InputStrategy {

    fun wait(func: (input: String) -> Unit)
}