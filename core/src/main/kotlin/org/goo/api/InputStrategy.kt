package org.goo.api

/**
 * Api abstraction to input for communication with other parts
 */
interface InputStrategy {

    fun wait(func: (input: String) -> Unit, debugLine: Int)
}