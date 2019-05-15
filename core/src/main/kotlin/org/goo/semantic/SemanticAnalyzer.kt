package org.goo.semantic

import org.goo.scanner.Token
import org.goo.scanner.Tokens
import kotlin.Boolean

class SemanticAnalyzer {

    fun analyze(tokens: List<Token>): Boolean {
        val identifiers = tokens.filter { it.token == Tokens.IDENTIFIER }.toMutableList()
        return !findDuplicates(identifiers)
    }

    private fun findDuplicates(list: MutableList<Token>): Boolean {
        if (list.size <= 1) {
            return false
        }

        list.sortWith(Comparator { o1, o2 ->
            return@Comparator o1.text.compareTo(o2.text)
        })

        for (i in 0 until list.size - 1) {
            if (list[i] == list[i + 1]) {
                return true
            }
        }

        return false
    }
}