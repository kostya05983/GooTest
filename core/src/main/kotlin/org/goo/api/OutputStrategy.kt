package org.goo.api

/**
 * Api abstraction to output for communication with other parts
 */
interface OutputStrategy {

    fun print(s: String)
}