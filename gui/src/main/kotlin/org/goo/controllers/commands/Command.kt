package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper

interface Command {
    val memory: CurrentSessionMemoryWrapper

    fun execute(split: List<String> = emptyList())
}