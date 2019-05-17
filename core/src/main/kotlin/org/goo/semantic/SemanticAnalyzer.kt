package org.goo.semantic

import org.goo.scanner.Token
import org.goo.scanner.Tokens
import kotlin.Boolean

/**
 * @author kostya05983
 */
class SemanticAnalyzer {

    /**
     * Analyze code for sematic erros
     */
    fun analyze(tokens: List<Token>): Boolean {
        val identifiers = filterForIdentifiersSubs(tokens)
        return !findDuplicates(identifiers.toMutableList()) && identifiers.map { it.text }.contains("main")
    }

    /**
     * In semantic we try to find identical identifiers, to prevent bugs
     */
    private fun filterForIdentifiersSubs(tokens: List<Token>): List<Token> {
        val result = mutableListOf<Token>()
        for (i in 0 until tokens.size) {
            if (tokens[i].token == Tokens.SUB) {
                result.add(tokens[i + 1])
            }
        }
        return result
    }

    /**
     * Search duplicates for time of sort
     */
    private fun findDuplicates(list: MutableList<Token>): Boolean {
        if (list.size <= 1) {
            return false
        }

        list.sortWith(Comparator { o1, o2 ->
            return@Comparator o1.text.compareTo(o2.text)
        })

        for (i in 0 until list.size - 1) {
            if (list[i].text == list[i + 1].text) {
                return true
            }
        }

        return false
    }
}