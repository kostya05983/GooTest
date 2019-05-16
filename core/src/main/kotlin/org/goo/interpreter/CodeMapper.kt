package org.goo.interpreter

import org.goo.scanner.Token
import org.goo.scanner.Tokens

/**
 * Mapping code in interpreter to lines for interpretation
 */
class CodeMapper {
    var subs: MutableMap<String, Int> = mutableMapOf()
    lateinit var executableLines: MutableList<Line>

    /**
     * Mapping code blocks
     */
    fun mapToSubs(tokens: List<Token>) {
        val subIndexes = mutableListOf<Int>()

        var lastIndex = 0

        executableLines = mutableListOf()
        for (i in 0 until tokens.size) {
            if (tokens[i].token == Tokens.SUB) {
                subIndexes.add(i)
            }

            if (tokens[i].token == Tokens.NEWLINE) {
                executableLines.add(Line(tokens[i].line, tokens.subList(lastIndex, i), false))
                lastIndex = i + 1
            }
        }

        for (i in 0 until subIndexes.size) {
            val text: String = tokens[subIndexes[i] + 1].text //key
            subs[text] = tokens[subIndexes[i]].line
        }
    }
}