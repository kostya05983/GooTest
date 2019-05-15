package org.goo.api

interface InputStrategy {

    fun wait(func: (input: String) -> Unit, debugLine: Int)
}